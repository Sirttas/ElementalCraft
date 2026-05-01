package sirttas.elementalcraft.block.pipe.upgrade.beam;

import net.minecraft.core.BlockPos;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeGameTests;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@ForEachTest(groups = ElementPipeGameTests.GROUP)
public class ElementBeamGameTests {

    @TestHolder(description = "Checks that a beam allow element flow.")
    @GameTest(template = "elementalcraft:elementbeamgametests.should_transferelements")
    public static void should_transferElements(ECGameTestHelper helper) {
        var ticks = new AtomicInteger(0);

        helper.startSequence()
                .thenExecute(() -> helper.pullLever(1, 1, 0))
                .thenIdle(2)
                .thenExecuteFor(10, () -> {
                    var targetStorage = helper.getBlockEntity(new BlockPos(12, 1, 1), ElementContainerBlockEntity.class).getElementStorage();

                    assertThat(targetStorage.getElementAmount()).isEqualTo(500 * ticks.incrementAndGet());
                })
                .thenSucceed();
    }

    @TestHolder(description = "Checks that a beam does not allow element flow when out of range.")
    @GameTest(template = "elementalcraft:elementbeamgametests.shouldnot_transferelements_when_outofrange")
    public static void shouldNot_transferElements_when_outOfRange(ECGameTestHelper helper) {
        helper.startSequence()
                .thenExecute(() -> helper.pullLever(1, 1, 0))
                .thenIdle(1)
                .thenExecuteFor(10, () -> {
                    var targetStorage = helper.getBlockEntity(new BlockPos(13, 1, 1), ElementContainerBlockEntity.class).getElementStorage();

                    assertThat(targetStorage.getElementAmount()).isZero();
                })
                .thenSucceed();
    }

}
