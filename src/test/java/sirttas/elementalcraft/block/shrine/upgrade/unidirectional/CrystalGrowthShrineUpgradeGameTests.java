package sirttas.elementalcraft.block.shrine.upgrade.unidirectional;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineGameUpgradeTests;

@ForEachTest(groups = ShrineGameUpgradeTests.GROUP)
public class CrystalGrowthShrineUpgradeGameTests {

    public static final String TEMPLATE = "elementalcraft:crystalgrowthshrineupgradegametests.should_growamethyst";

    @TestHolder
    @GameTest(template = TEMPLATE)
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
