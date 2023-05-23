package sirttas.elementalcraft.block.shrine.upgrade.unidirectional;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;

@GameTestHolder(ElementalCraftApi.MODID)
public class CrystalGrowthShrineUpgradeGameTests {

    // elementalcraft:crystalgrowthshrineupgradegametests.should_growamethyst
    @GameTest(batch = ShrineGameTestHelper.BATCH_NAME)
    public static void should_growAmethyst(GameTestHelper helper) {


        helper.startSequence().thenExecuteAfter(1, () -> {
            ShrineGameTestHelper.forcePeriods(helper, new BlockPos(1, 2, 3), 36);
        }).thenExecuteAfter(1, () -> {
            helper.assertBlockPresent(Blocks.AMETHYST_CLUSTER, 1, 2, 2);
            helper.assertBlockPresent(Blocks.AMETHYST_CLUSTER, 1, 2, 0);
            helper.assertBlockPresent(Blocks.AMETHYST_CLUSTER, 1, 3, 1);
            helper.assertBlockPresent(Blocks.AMETHYST_CLUSTER, 1, 1, 1);
            helper.assertBlockPresent(Blocks.AMETHYST_CLUSTER, 2, 2, 1);
            helper.assertBlockPresent(Blocks.AMETHYST_CLUSTER, 0, 2, 1);
        }).thenSucceed();
    }
}
