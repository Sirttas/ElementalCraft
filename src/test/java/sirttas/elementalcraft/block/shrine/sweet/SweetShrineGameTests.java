package sirttas.elementalcraft.block.shrine.sweet;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;

import static org.assertj.core.api.Assertions.assertThat;

@ForEachTest(groups = ShrineGameTestHelper.GROUP)
public class SweetShrineGameTests {

    private static final String TEMPLATE = "elementalcraft:sweetshrinegametests.should_feedplayer";

    @TestHolder
    @GameTest(template = TEMPLATE)
    public static void should_feedPlayer(ECGameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);

        helper.getLevel().addFreshEntity(player);
        helper.startSequence().thenExecute(() -> {
            player.moveTo(helper.absoluteVec(new Vec3(0.5, 0.5, 1.5)));
            player.getFoodData().setFoodLevel(5);
            player.getFoodData().setSaturation(0);
        }).thenExecuteAfter(1, () -> {
            ShrineGameTestHelper.forcePeriod(helper, new BlockPos(0, 1, 0));
        }).thenExecuteAfter(1, ECGameTestUtils.fixAssertions(() -> {
            assertThat(player.getFoodData().getFoodLevel()).isEqualTo(6);
            assertThat(player.getFoodData().getSaturationLevel()).isEqualTo(0.2F);
            player.discard();
        })).thenSucceed();
    }

}
