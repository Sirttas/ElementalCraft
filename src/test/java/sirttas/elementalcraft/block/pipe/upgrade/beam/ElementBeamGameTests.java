package sirttas.elementalcraft.block.pipe.upgrade.beam;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeGameTests;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class ElementBeamGameTests {

    // elementalcraft:elementbeamgametests.should_transferelements
    @GameTest(batch = ElementPipeGameTests.BATCH_NAME)
    public static void should_transferElements(GameTestHelper helper) {
        var ticks = new AtomicInteger(0);

        helper.startSequence()
                .thenExecute(() -> {
                    helper.pullLever(1, 2, 0);
                })
                .thenIdle(2)
                .thenExecuteFor(10, ECGameTestHelper.fixAssertions(() -> {
                    var targetStorage = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(12, 2, 1))).getElementStorage();

                    assertThat(targetStorage.getElementAmount()).isEqualTo(100 * ticks.incrementAndGet());
                }))
                .thenSucceed();
    }

    // elementalcraft:elementbeamgametests.shouldnot_transferelements_whenoutofrange
    @GameTest(batch = ElementPipeGameTests.BATCH_NAME)
    public static void shouldNot_transferElements_whenOutOfRange(GameTestHelper helper) {
        helper.startSequence()
                .thenExecute(() -> {
                    helper.pullLever(1, 2, 0);
                })
                .thenIdle(1)
                .thenExecuteFor(10, ECGameTestHelper.fixAssertions(() -> {
                    var targetStorage = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(13, 2, 1))).getElementStorage();

                    assertThat(targetStorage.getElementAmount()).isZero();
                }))
                .thenSucceed();
    }

}
