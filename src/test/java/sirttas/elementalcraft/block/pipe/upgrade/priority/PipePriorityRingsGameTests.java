package sirttas.elementalcraft.block.pipe.upgrade.priority;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeGameTests;

import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = ElementPipeGameTests.GROUP)
public class PipePriorityRingsGameTests {

    @TestHolder(description = "Checks that the rigs make the west container fill first.")
    @GameTest(template = "elementalcraft:pipepriorityringsgametests.should_transferwestfirst")
    public static void should_transferWestFirst(GameTestHelper helper) {
        var targetStorage1 = helper.getBlockEntity(new BlockPos(0, 2, 3), ElementContainerBlockEntity.class).getElementStorage();
        var targetStorage2 = helper.getBlockEntity(new BlockPos(1, 2, 3), ElementContainerBlockEntity.class).getElementStorage();
        var ticks = new AtomicInteger(0);

        helper.startSequence().thenExecute(() -> helper.pullLever(0, 2, 1))
                .thenIdle(2)
                .thenExecuteFor(10, () -> {
                    assertThat(targetStorage1.getElementAmount()).isEqualTo(500 * ticks.incrementAndGet());
                    assertThat(targetStorage2.getElementAmount()).isZero();
                })
                .thenSucceed();
    }
    
    @TestHolder(description = "Checks that the rigs make the far container fill first.")
    @GameTest(template = "elementalcraft:pipepriorityringsgametests.should_transferfarfirst")
    public static void should_transferFarFirst(GameTestHelper helper) {
        var targetStorage1 = helper.getBlockEntity(new BlockPos(1, 2, 5), ElementContainerBlockEntity.class).getElementStorage();
        var targetStorage2 = helper.getBlockEntity(new BlockPos(1, 2, 3), ElementContainerBlockEntity.class).getElementStorage();
        var ticks = new AtomicInteger(0);

        helper.startSequence().thenExecute(() -> helper.pullLever(0, 2, 1))
                .thenIdle(2)
                .thenExecuteFor(10, () -> {
                    assertThat(targetStorage1.getElementAmount()).isEqualTo(500 * ticks.incrementAndGet());
                    assertThat(targetStorage2.getElementAmount()).isZero();
                })
                .thenSucceed();
    }
}
