package sirttas.elementalcraft.block.pipe.upgrade.pump;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeGameTests;

import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;
@GameTestHolder(ElementalCraftApi.MODID)
public class ElementPumpGameTests {

    // elementalcraft:elementpumpgametests.should_transfer500elements
    @GameTest(batch = ElementPipeGameTests.BATCH_NAME)
    public static void should_transfer500Elements(GameTestHelper helper) {
        var ticks = new AtomicInteger(0);

        helper.startSequence()
                .thenExecute(() -> {
                    helper.pullLever(1, 2, 1);
                })
                .thenIdle(1)
                .thenExecuteFor(10, ECGameTestHelper.fixAssertions(() -> {
                    var sourceStorage = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(0, 2, 0))).getElementStorage();
                    var targetStorage = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(0, 2, 2))).getElementStorage();
                    var i = ticks.incrementAndGet();

                    assertThat(targetStorage.getElementAmount()).isEqualTo(500 * i);
                    assertThat(sourceStorage.getElementAmount()).isLessThan(100000 - (500 * i));
                }))
                .thenSucceed();
    }

    // elementalcraft:elementpumpgametests.should_transfer1250elements
    @GameTest(batch = ElementPipeGameTests.BATCH_NAME)
    public static void should_transfer1250Elements(GameTestHelper helper) {
        var ticks = new AtomicInteger(0);

        helper.startSequence()
                .thenExecute(() -> {
                    helper.pullLever(1, 2, 1);
                })
                .thenIdle(1)
                .thenExecuteFor(10, ECGameTestHelper.fixAssertions(() -> {
                    var sourceStorage = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(0, 2, 0))).getElementStorage();
                    var targetStorage = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(0, 2, 2))).getElementStorage();
                    var i = ticks.incrementAndGet();

                    assertThat(targetStorage.getElementAmount()).isEqualTo(1250 * i);
                    assertThat(sourceStorage.getElementAmount()).isLessThan(100000 - (1250 * i));
                }))
                .thenSucceed();
    }
}
