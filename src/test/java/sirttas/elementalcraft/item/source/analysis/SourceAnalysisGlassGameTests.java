package sirttas.elementalcraft.item.source.analysis;

import net.neoforged.testframework.gametest.GameTest;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.EmptyTemplate;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.item.ECItems;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = SourceAnalysisGlassGameTests.GROUP)
public class SourceAnalysisGlassGameTests {

    public static final String GROUP = "stacks.source_analysis_glass";

    @GameTest
    @EmptyTemplate(floor = true)
    @TestHolder(description = "Check that a player can analyze receptacles in their inventory with the source analysis glass.")
    public static void should_analyzeReceptaclesInInventory(ECGameTestHelper helper) {
        var player = helper.mockReceptaclePlayer(ElementType.FIRE);

        player.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ECItems.SOURCE_ANALYSIS_GLASS));

        helper.startSequence().thenExecute(ECGameTestUtils.fixAssertions(() -> {
                    var result = helper.useItem(player, InteractionHand.OFF_HAND);

                    assertThat(result.getResult()).isEqualTo(InteractionResult.SUCCESS);
                }))
                .thenExecuteAfter(1, ECGameTestUtils.fixAssertions(() -> {
                    var receptacle = player.getItemInHand(InteractionHand.MAIN_HAND);

                    assertThat(receptacle).hasDataComponentSatisfying(ECDataComponents.SOURCE_ANALYZED, analyzed -> assertThat(analyzed)
                            .as("Source receptacle should be analyzed")
                            .isTrue());
                }))
                .thenExecuteAfter(1, player::discard)
                .thenSucceed();
    }

}
