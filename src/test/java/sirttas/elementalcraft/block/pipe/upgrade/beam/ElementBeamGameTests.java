package sirttas.elementalcraft.block.pipe.upgrade.beam;

import net.minecraft.core.BlockPos;
import net.neoforged.testframework.gametest.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeGameTests;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@ForEachTest(groups = ElementPipeGameTests.GROUP)
public class ElementBeamGameTests {

    @TestHolder(description = "Checks that a beam allow element flow.")
    @GameTest(templateNamespace = ElementalCraftApi.MODID, template = "elementbeamgametests.should_transferelements")
    public static void should_transferElements(GameTestHelper helper) {
        var ticks = new AtomicInteger(0);

        helper.startSequence()
                .thenExecute(() -> {
                    helper.pullLever(1, 2, 0);
                })
                .thenIdle(2)
                .thenExecuteFor(10, ECGameTestUtils.fixAssertions(() -> {
                    var targetStorage = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(12, 2, 1))).getElementStorage();

                    assertThat(targetStorage.getElementAmount()).isEqualTo(500 * ticks.incrementAndGet());
                }))
                .thenSucceed();
    }

    @TestHolder(description = "Checks that a beam does not allow element flow when out of range.")
    @GameTest(templateNamespace = ElementalCraftApi.MODID, template = "elementbeamgametests.shouldnot_transferelements_when_outofrange")
    public static void shouldNot_transferElements_when_outOfRange(GameTestHelper helper) {
        helper.startSequence()
                .thenExecute(() -> {
                    helper.pullLever(1, 2, 0);
                })
                .thenIdle(1)
                .thenExecuteFor(10, ECGameTestUtils.fixAssertions(() -> {
                    var targetStorage = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(13, 2, 1))).getElementStorage();

                    assertThat(targetStorage.getElementAmount()).isZero();
                }))
                .thenSucceed();
    }

}
