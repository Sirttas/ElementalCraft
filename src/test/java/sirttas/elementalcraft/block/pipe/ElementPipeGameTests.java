package sirttas.elementalcraft.block.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.testframework.Test;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@ForEachTest(groups = ElementPipeGameTests.GROUP)
public class ElementPipeGameTests {

    public static final String GROUP = "level.blocks.pipe";

    @TestHolder(description = "Checks if the pipe does not transfer above max.")
    @GameTest(template = "elementalcraft:elementpipegametests.shouldnot_transferabovemax")
    public static void shouldNot_transferAboveMax(ECGameTestHelper helper) {
        var targetStorage = getElementStorage(helper, 1, 1, 0);
        var ticks = new AtomicInteger(0);

        helper.startSequence().thenExecute(() -> helper.pullLever(0, 1, 1))
                .thenIdle(1)
                .thenExecuteFor(10, () -> assertThat(targetStorage.getElementAmount()).isEqualTo(500 * ticks.incrementAndGet()))
                .thenSucceed();
    }

    @TestHolder(description = "Checks if the pipe transfers to multiple storages in the same tick.")
    @GameTest(template = "elementalcraft:elementpipegametests.should_transfertomultiplestorages")
    public static void should_transferToMultipleStorages(ECGameTestHelper helper) {
        var sourceStorage = getElementStorage(helper, 1, 1, 0);
        var targetStorage1 = getElementStorage(helper, 0, 1, 3);
        var targetStorage2 = getElementStorage(helper, 1, 1, 3);
        var targetStorage3 = getElementStorage(helper, 2, 1, 3);

        helper.startSequence().thenExecute(() -> helper.pullLever(0, 1, 1))
                .thenExecuteAfter(1,  () -> {
                    assertThat(sourceStorage.getElementAmount()).isEqualTo(99500);
                    assertThat(targetStorage1.getElementAmount()).isZero();
                    assertThat(targetStorage2.getElementAmount()).isEqualTo(500);
                    assertThat(targetStorage3.getElementAmount()).isZero();
                }).thenExecuteAfter(1,  () -> {
                    assertThat(sourceStorage.getElementAmount()).isEqualTo(99000);
                    assertThat(targetStorage1.getElementAmount()).isZero();
                    assertThat(targetStorage2.getElementAmount()).isEqualTo(1000);
                    assertThat(targetStorage3.getElementAmount()).isZero();
                }).thenExecuteAfter(1,  () -> {
                    assertThat(sourceStorage.getElementAmount()).isEqualTo(98500);
                    assertThat(targetStorage1.getElementAmount()).isZero();
                    assertThat(targetStorage2.getElementAmount()).isEqualTo(1000);
                    assertThat(targetStorage3.getElementAmount()).isEqualTo(500);
                }).thenExecuteAfter(1,  () -> {
                    assertThat(sourceStorage.getElementAmount()).isEqualTo(98000);
                    assertThat(targetStorage1.getElementAmount()).isZero();
                    assertThat(targetStorage2.getElementAmount()).isEqualTo(1000);
                    assertThat(targetStorage3.getElementAmount()).isEqualTo(1000);
                }).thenExecuteAfter(1,  () -> {
                    assertThat(sourceStorage.getElementAmount()).isEqualTo(97500);
                    assertThat(targetStorage1.getElementAmount()).isEqualTo(500);
                    assertThat(targetStorage2.getElementAmount()).isEqualTo(1000);
                    assertThat(targetStorage3.getElementAmount()).isEqualTo(1000);
                }).thenExecuteAfter(1,  () -> {
                    assertThat(sourceStorage.getElementAmount()).isEqualTo(97000);
                    assertThat(targetStorage1.getElementAmount()).isEqualTo(1000);
                    assertThat(targetStorage2.getElementAmount()).isEqualTo(1000);
                    assertThat(targetStorage3.getElementAmount()).isEqualTo(1000);
                })
                .thenSucceed();
    }

    public static List<Test> should_disconnectWhenBroken() {
        var i = 0;

        return List.of(
                createTest(
                        "should_disconnectWhenBroken_" + i++,
                        "Check if a pipe disconnects when the connected block is broken.",
                        "elementpipegametests.shouldnot_transferabovemax",
                        h -> should_disconnectPipeWhenBroken(h, new BlockPos(1, 1, 1), Direction.NORTH)),
                createTest(
                        "should_disconnectWhenBroken_" + i++,
                        "Check if a pipe disconnects when the connected block is broken.",
                        "elementpipegametests.shouldnot_transferabovemax",
                        h -> should_disconnectPipeWhenBroken(h, new BlockPos(1, 1, 2), Direction.NORTH)),
                createTest(
                        "should_disconnectWhenBroken_" + i++,
                        "Check if a pipe disconnects when the connected block is broken.",
                        "elementpipegametests.shouldnot_transferabovemax",
                        h -> should_disconnectPipeWhenBroken(h, new BlockPos(1, 1, 2), Direction.SOUTH)),
                createTest(
                        "should_disconnectWhenBroken_" + i++,
                        "Check if a pipe disconnects when the connected block is broken.",
                        "overclockedaccelerationshrineupgradegametests.should_allowselementtransfer",
                        h -> should_disconnectPipeWhenBroken(h, new BlockPos(1, 1, 2), Direction.NORTH))
        );
    }

    public static void should_disconnectPipeWhenBroken(ECGameTestHelper helper, BlockPos pipePos, Direction direction) {
        helper.startSequence().thenExecute(() -> {
            helper.destroyBlock(pipePos.relative(direction));
        }).thenExecuteAfter(1, () -> {
            var pipe = helper.getBlockEntity(pipePos, ElementPipeBlockEntity.class);

            assertThat(pipe).isNotNull().satisfies(p -> assertThat(p.getConnection(direction)).isEqualTo(ConnectionType.NONE));
        }).thenSucceed();
    }

    public static ISingleElementStorage getElementStorage(GameTestHelper helper, int x, int y, int z) {
        return  helper.getBlockEntity(new BlockPos(x, y, z), ElementContainerBlockEntity.class).getElementStorage();
    }

    public static Test createTest(String name, String description, String template, Consumer<ECGameTestHelper> function) {
        return ECGameTestUtils.createTest(GROUP, name, description, template, function);
    }

}
