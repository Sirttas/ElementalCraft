package sirttas.elementalcraft.block.shrine.sweet;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.GameType;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;

import static org.assertj.core.api.Assertions.assertThat;

@ForEachTest(groups = ShrineGameTestHelper.GROUP)
public class SweetShrineGameTests {

    private static final String TEMPLATE = "elementalcraft:sweetshrinegametests.should_feedplayer";

    @TestHolder
    @GameTest(template = TEMPLATE)
    public static void should_feedPlayer(ECGameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);

        helper.moveEntityToOrigin(player);
        helper.getLevel().addFreshEntity(player);
        helper.startSequence().thenExecute(() -> {
            player.getFoodData().setFoodLevel(5);
            player.getFoodData().setSaturation(0);
        }).thenExecuteAfter(1, () -> {
            ShrineGameTestHelper.forcePeriod(helper, new BlockPos(0, 0, 0));
        }).thenExecuteAfter(1, () -> {
            assertThat(player.getFoodData().getFoodLevel()).isEqualTo(6);
            assertThat(player.getFoodData().getSaturationLevel()).isEqualTo(0.2F);
            player.discard();
        }).thenSucceed();
    }

}
