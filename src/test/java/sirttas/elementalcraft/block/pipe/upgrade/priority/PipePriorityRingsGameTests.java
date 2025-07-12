package sirttas.elementalcraft.block.pipe.upgrade.priority;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeGameTests;

import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID) // TODO move to test framework
public class PipePriorityRingsGameTests {

    // elementalcraft:pipepriorityringsgametests.should_transferwestfirst
    @GameTest(batch = ElementPipeGameTests.GROUP) // TODO move to test framework
    public static void should_transferWestFirst(GameTestHelper helper) {
        var targetStorage1 = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(0, 2, 3))).getElementStorage();
        var targetStorage2 = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(1, 2, 3))).getElementStorage();
        var ticks = new AtomicInteger(0);

        helper.startSequence().thenExecute(() -> helper.pullLever(0, 2, 1))
                .thenIdle(2)
                .thenExecuteFor(10, ECGameTestUtils.fixAssertions(() -> {
                    assertThat(targetStorage1.getElementAmount()).isEqualTo(500 * ticks.incrementAndGet());
                    assertThat(targetStorage2.getElementAmount()).isZero();
                }))
                .thenSucceed();
    }

    // elementalcraft:pipepriorityringsgametests.should_transferfarfirst
    @GameTest(batch = ElementPipeGameTests.GROUP) // TODO move to test framework
    public static void should_transferFarFirst(GameTestHelper helper) {
        var targetStorage1 = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(1, 2, 5))).getElementStorage();
        var targetStorage2 = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(1, 2, 3))).getElementStorage();
        var ticks = new AtomicInteger(0);

        helper.startSequence().thenExecute(() -> helper.pullLever(0, 2, 1))
                .thenIdle(2)
                .thenExecuteFor(10, ECGameTestUtils.fixAssertions(() -> {
                    assertThat(targetStorage1.getElementAmount()).isEqualTo(500 * ticks.incrementAndGet());
                    assertThat(targetStorage2.getElementAmount()).isZero();
                }))
                .thenSucceed();
    }
}
