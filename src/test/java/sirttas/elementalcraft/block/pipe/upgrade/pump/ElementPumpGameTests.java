package sirttas.elementalcraft.block.pipe.upgrade.pump;

import net.minecraft.core.BlockPos;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeGameTests;

import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = ElementPipeGameTests.GROUP)
public class ElementPumpGameTests {

    @TestHolder(description = "Checks that a pipe with a pump transfer 2500 element without runes.")
    @GameTest(template = "elementalcraft:elementpumpgametests.should_transfer2500elements")
    public static void should_transfer2500Elements(ECGameTestHelper helper) {
        var ticks = new AtomicInteger(0);

        helper.startSequence()
                .thenExecute(() -> helper.pullLever(1, 1, 1))
                .thenIdle(1)
                .thenExecuteFor(10, () -> {
                    var sourceStorage = helper.getBlockEntity(new BlockPos(0, 1, 0), ElementContainerBlockEntity.class).getElementStorage();
                    var targetStorage = helper.getBlockEntity(new BlockPos(0, 1, 2), ElementContainerBlockEntity.class).getElementStorage();
                    var i = ticks.incrementAndGet();

                    assertThat(targetStorage.getElementAmount()).isEqualTo(2500 * i);
                    assertThat(sourceStorage.getElementAmount()).isLessThan(100000 - (2500 * i));
                })
                .thenSucceed();
    }

    @TestHolder(description = "Checks that a pipe with a pump transfer 6250 element with runes.")
    @GameTest(template = "elementalcraft:elementpumpgametests.should_transfer6250elements")
    public static void should_transfer6250Elements(ECGameTestHelper helper) {
        var ticks = new AtomicInteger(0);

        helper.startSequence()
                .thenExecute(() -> helper.pullLever(1, 1, 1))
                .thenIdle(1)
                .thenExecuteFor(10, () -> {
                    var sourceStorage = helper.getBlockEntity(new BlockPos(0, 1, 0), ElementContainerBlockEntity.class).getElementStorage();
                    var targetStorage = helper.getBlockEntity(new BlockPos(0, 1, 2), ElementContainerBlockEntity.class).getElementStorage();
                    var i = ticks.incrementAndGet();

                    assertThat(targetStorage.getElementAmount()).isEqualTo(6250 * i);
                    assertThat(sourceStorage.getElementAmount()).isLessThan(100000 - (6250 * i));
                })
                .thenSucceed();
    }
}
