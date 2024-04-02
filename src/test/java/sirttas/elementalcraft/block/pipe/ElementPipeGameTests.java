package sirttas.elementalcraft.block.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.level.block.Rotation;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class ElementPipeGameTests {

    public static final String BATCH_NAME = "pipe";


    // elementalcraft:elementpipegametests.shouldnot_transferabovemax
    @GameTest(batch = BATCH_NAME)
    public static void shouldNot_transferAboveMax(GameTestHelper helper) {
        var targetStorage = getElementStorage(helper, 1, 2, 0);
        var ticks = new AtomicInteger(0);

        helper.startSequence().thenExecute(() -> helper.pullLever(0, 2, 1))
                .thenIdle(1)
                .thenExecuteFor(10, ECGameTestHelper.fixAssertions(() -> assertThat(targetStorage.getElementAmount()).isEqualTo(100 * ticks.incrementAndGet())))
                .thenSucceed();
    }

    // elementalcraft:elementpipegametests.should_transfertomultiplestorages
    @GameTest(batch = BATCH_NAME)
    public static void should_transferToMultipleStorages(GameTestHelper helper) {
        var sourceStorage = getElementStorage(helper, 1, 2, 0);
        var targetStorage1 = getElementStorage(helper, 0, 2, 3);
        var targetStorage2 = getElementStorage(helper, 1, 2, 3);
        var targetStorage3 = getElementStorage(helper, 2, 2, 3);

        helper.startSequence().thenExecute(() -> helper.pullLever(0, 2, 1))
                .thenIdle(1)
                .thenExecuteAfter(1,  ECGameTestHelper.fixAssertions(() -> {
                    assertThat(sourceStorage.getElementAmount()).isEqualTo(98584);
                    assertThat(targetStorage1.getElementAmount()).isZero();
                    assertThat(targetStorage2.getElementAmount()).isEqualTo(1000);
                    assertThat(targetStorage3.getElementAmount()).isEqualTo(250);
                })).thenExecuteAfter(1,  ECGameTestHelper.fixAssertions(() -> {
                    assertThat(sourceStorage.getElementAmount()).isEqualTo(97167);
                    assertThat(targetStorage1.getElementAmount()).isEqualTo(500);
                    assertThat(targetStorage2.getElementAmount()).isEqualTo(1000);
                    assertThat(targetStorage3.getElementAmount()).isEqualTo(1000);
                })).thenIdle(1)
                .thenExecuteFor(10,  ECGameTestHelper.fixAssertions(() -> {
                    assertThat(sourceStorage.getElementAmount()).isEqualTo(96600);
                    assertThat(targetStorage1.getElementAmount()).isEqualTo(1000);
                    assertThat(targetStorage2.getElementAmount()).isEqualTo(1000);
                    assertThat(targetStorage3.getElementAmount()).isEqualTo(1000);
                }))
                .thenSucceed();
    }

    @GameTestGenerator
    public static List<TestFunction> should_disconnectWhenBroken() {
        var index = new AtomicInteger(0);

        return List.of(
                createTestFunction("should_disconnectWhenBroken#" + index.getAndIncrement(), "elementpipegametests.shouldnot_transferabovemax", h -> should_disconnectPipeWhenBroken(h, new BlockPos(1, 2, 1), Direction.NORTH)),
                createTestFunction("should_disconnectWhenBroken#" + index.getAndIncrement(), "elementpipegametests.shouldnot_transferabovemax", h -> should_disconnectPipeWhenBroken(h, new BlockPos(1, 2, 2), Direction.NORTH)),
                createTestFunction("should_disconnectWhenBroken#" + index.getAndIncrement(), "elementpipegametests.shouldnot_transferabovemax", h -> should_disconnectPipeWhenBroken(h, new BlockPos(1, 2, 2), Direction.SOUTH)),
                createTestFunction("should_disconnectWhenBroken#" + index.getAndIncrement(), "overclockedaccelerationshrineupgradegametests.should_allowselementtransfer", h -> should_disconnectPipeWhenBroken(h, new BlockPos(1, 2, 2), Direction.NORTH))
        );
    }

    public static void should_disconnectPipeWhenBroken(GameTestHelper helper, BlockPos pipePos, Direction direction) {
        helper.startSequence().thenExecute(() -> {
            helper.destroyBlock(pipePos.relative(direction));
        }).thenExecuteAfter(1, ECGameTestHelper.fixAssertions(() -> {
            var pipe = (ElementPipeBlockEntity) helper.getBlockEntity(pipePos);

            assertThat(pipe).isNotNull().satisfies(p -> assertThat(p.getConnection(direction)).isEqualTo(ConnectionType.NONE));
        })).thenSucceed();
    }

    private static ISingleElementStorage getElementStorage(GameTestHelper helper, int x, int y, int z) {
        var be = helper.getBlockEntity(new BlockPos(x, y, z));

        assertThat(be).isNotNull().isInstanceOf(ElementContainerBlockEntity.class);
        return ((ElementContainerBlockEntity) be).getElementStorage();
    }

    public static TestFunction createTestFunction(String name, String template, Consumer<GameTestHelper> function) {
        return ECGameTestHelper.createTestFunction(BATCH_NAME, name, template, Rotation.NONE, function);
    }

}
