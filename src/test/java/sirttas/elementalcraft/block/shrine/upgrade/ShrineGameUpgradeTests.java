package sirttas.elementalcraft.block.shrine.upgrade;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.testframework.Test;
import net.neoforged.testframework.annotation.ForEachTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;
import sirttas.elementalcraft.block.shrine.melting.MeltingShrineGameTests;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.overclocked.OverclockedAccelerationShrineUpgradeGameTests;
import sirttas.elementalcraft.block.shrine.upgrade.fortune.greater.GreaterFortuneShrineUpgradeGameTests;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.CrystalGrowthShrineUpgradeGameTests;

import java.util.List;

@ForEachTest(groups = ShrineGameUpgradeTests.GROUP)
public class ShrineGameUpgradeTests {

    public static final String GROUP = ShrineGameTestHelper.GROUP + ".upgrade";

    public static List<Test> should_breakUpgradesWhenBroken() {
        var i = 0;

        return List.of(
                should_breakUpgradesWhenBroken(i++, MeltingShrineGameTests.MELTING_SHRINE_WITH_FILLING_TEMPLATE_NAME, new BlockPos(1, 0, 1), new BlockPos(2, 0, 1)),
                should_breakUpgradesWhenBroken(i++, OverclockedAccelerationShrineUpgradeGameTests.TEMPLATE, new BlockPos(1, 1, 0), new BlockPos(1, 1, 1), new BlockPos(1, 2, 1)),
                should_breakUpgradesWhenBroken(i++, GreaterFortuneShrineUpgradeGameTests.TEMPLATE, new BlockPos(12, 1, 12), new BlockPos(12, 1, 13)),
                should_breakUpgradesWhenBroken(i++, CrystalGrowthShrineUpgradeGameTests.TEMPLATE, new BlockPos(1, 1, 3), new BlockPos(1, 2, 3))
        );
    }

    private static Test should_breakUpgradesWhenBroken(int index, String template, BlockPos shrinePos, BlockPos ...upgradePos) {
        return ECGameTestUtils.createTest(
                GROUP,
                "should_breakUpgradesWhenBroken_" + index++,
                "Check that shrine upgrades are broken when the shrine is broken",
                template,
                h -> should_breakUpgradesWhenBroken(h, shrinePos, upgradePos));
    }

    private static void should_breakUpgradesWhenBroken(ECGameTestHelper helper, BlockPos shrinePos, BlockPos ...upgradePos) {
        helper.startSequence()
                .thenExecute(() -> helper.getLevel().destroyBlock(helper.absolutePos(shrinePos), true))
                .thenExecuteAfter(5, () -> {
                    helper.assertBlockPresent(Blocks.AIR, shrinePos);
                    for (var upgrade : upgradePos) {
                        helper.assertBlockPresent(Blocks.AIR, upgrade);
                    }
                })
                .thenExecute(() -> helper.discardItems(shrinePos, 2))
                .thenSucceed();
    }
}
