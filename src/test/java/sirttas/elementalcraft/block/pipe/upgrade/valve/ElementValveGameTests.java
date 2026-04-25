package sirttas.elementalcraft.block.pipe.upgrade.valve;

import net.minecraft.core.BlockPos;
import sirttas.elementalcraft.ECGameTestHelper;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeGameTests;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@ForEachTest(groups = ElementPipeGameTests.GROUP)
public class ElementValveGameTests {

    @TestHolder(description = "Checks that a pipe with a valve can transfer elements if it is powered.")
    @GameTest(template = "elementalcraft:elementvalvegametests.valve")
    public static void should_transferElements_when_powered(ECGameTestHelper helper) {
        var ticks = new AtomicInteger(0);

        helper.startSequence()
                .thenExecute(() -> {
                    helper.setBlock(new BlockPos(1, 2, 0), ECBlocks.CONTAINER.get());
                    helper.pullLever(0, 2, 1);
                })
                .thenIdle(1)
                .thenExecuteFor(10, () -> {
                    var targetStorage = helper.getBlockEntity(new BlockPos(1, 2, 0), ElementContainerBlockEntity.class).getElementStorage();

                    assertThat(targetStorage.getElementAmount()).isEqualTo(500 * ticks.incrementAndGet());
                })
                .thenSucceed();
    }

    @TestHolder(description = "Checks that a pipe with a valve cannot transfer elements if it is not powered.")
    @GameTest(template = "elementalcraft:elementvalvegametests.valve")
    public static void shouldNot_transferElements_when_notPowered(ECGameTestHelper helper) {
        helper.startSequence()
                .thenExecute(() -> helper.setBlock(new BlockPos(1, 2, 0), ECBlocks.CONTAINER.get()))
                .thenIdle(1)
                .thenExecuteFor(10, () -> {
                    var targetStorage = helper.getBlockEntity(new BlockPos(1, 2, 0), ElementContainerBlockEntity.class).getElementStorage();

                    assertThat(targetStorage.getElementAmount()).isZero();
                })
                .thenSucceed();
    }
}
