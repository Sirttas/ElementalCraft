package sirttas.elementalcraft.block.pipe.upgrade.valve;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeGameTests;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class ElementValveGameTests {

    // elementalcraft:elementvalvegametests.valve
    @GameTest(template = "valve", batch = ElementPipeGameTests.BATCH_NAME)
    public static void should_transferElements_whenPowered(GameTestHelper helper) {
        var ticks = new AtomicInteger(0);

        helper.startSequence()
                .thenExecute(() -> {
                    helper.setBlock(new BlockPos(1, 2, 0), ECBlocks.CONTAINER.get());
                    helper.pullLever(0, 2, 1);
                })
                .thenIdle(1)
                .thenExecuteFor(10, ECGameTestHelper.fixAssertions(() -> {
                    var targetStorage = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(1, 2, 0))).getElementStorage();

                    assertThat(targetStorage.getElementAmount()).isEqualTo(100 * ticks.incrementAndGet());
                }))
                .thenSucceed();
    }

    // elementalcraft:elementvalvegametests.valve
    @GameTest(template = "valve", batch = ElementPipeGameTests.BATCH_NAME)
    public static void shouldNot_transferElements_whenNotPowered(GameTestHelper helper) {
        helper.startSequence()
                .thenExecute(() -> helper.setBlock(new BlockPos(1, 2, 0), ECBlocks.CONTAINER.get()))
                .thenIdle(1)
                .thenExecuteFor(10, ECGameTestHelper.fixAssertions(() -> {
                    var targetStorage = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(1, 2, 0))).getElementStorage();

                    assertThat(targetStorage.getElementAmount()).isZero();
                }))
                .thenSucceed();
    }
}
