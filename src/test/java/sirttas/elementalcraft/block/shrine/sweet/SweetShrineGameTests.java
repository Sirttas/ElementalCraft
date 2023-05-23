package sirttas.elementalcraft.block.shrine.sweet;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;

import static org.assertj.core.api.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class SweetShrineGameTests {

    // elementalcraft:sweetshrinegametests.should_feedplayer
    @GameTest(batch = ShrineGameTestHelper.BATCH_NAME)
    public static void should_feedPlayer(GameTestHelper helper) {
        var player = helper.makeMockPlayer();

        helper.getLevel().addFreshEntity(player);
        helper.startSequence().thenExecute(() -> {
            player.moveTo(helper.absoluteVec(new Vec3(0.5, 0.5, 1.5)));
            player.getFoodData().setFoodLevel(5);
            player.getFoodData().setSaturation(0);
        }).thenExecuteAfter(1, () -> {
            ShrineGameTestHelper.forcePeriod(helper, new BlockPos(0, 1, 0));
        }).thenExecuteAfter(1, () -> {
            assertThat(player.getFoodData().getFoodLevel()).isEqualTo(6);
            assertThat(player.getFoodData().getSaturationLevel()).isEqualTo(0.2F);
            player.discard();
        }).thenSucceed();
    }

}
