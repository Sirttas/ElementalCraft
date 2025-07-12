package sirttas.elementalcraft.block.pipe.upgrade.pump;

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
public class ElementPumpGameTests {

    // elementalcraft:elementpumpgametests.should_transfer2500elements
    @GameTest(batch = ElementPipeGameTests.GROUP) // TODO move to test framework
    public static void should_transfer2500Elements(GameTestHelper helper) {
        var ticks = new AtomicInteger(0);

        helper.startSequence()
                .thenExecute(() -> {
                    helper.pullLever(1, 2, 1);
                })
                .thenIdle(1)
                .thenExecuteFor(10, ECGameTestUtils.fixAssertions(() -> {
                    var sourceStorage = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(0, 2, 0))).getElementStorage();
                    var targetStorage = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(0, 2, 2))).getElementStorage();
                    var i = ticks.incrementAndGet();

                    assertThat(targetStorage.getElementAmount()).isEqualTo(2500 * i);
                    assertThat(sourceStorage.getElementAmount()).isLessThan(100000 - (2500 * i));
                }))
                .thenSucceed();
    }

    // elementalcraft:elementpumpgametests.should_transfer6250elements
    @GameTest(batch = ElementPipeGameTests.GROUP) // TODO move to test framework
    public static void should_transfer6250Elements(GameTestHelper helper) {
        var ticks = new AtomicInteger(0);

        helper.startSequence()
                .thenExecute(() -> {
                    helper.pullLever(1, 2, 1);
                })
                .thenIdle(1)
                .thenExecuteFor(10, ECGameTestUtils.fixAssertions(() -> {
                    var sourceStorage = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(0, 2, 0))).getElementStorage();
                    var targetStorage = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(0, 2, 2))).getElementStorage();
                    var i = ticks.incrementAndGet();

                    assertThat(targetStorage.getElementAmount()).isEqualTo(6250 * i);
                    assertThat(sourceStorage.getElementAmount()).isLessThan(100000 - (6250 * i));
                }))
                .thenSucceed();
    }
}
