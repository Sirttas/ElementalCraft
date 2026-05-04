package sirttas.elementalcraft.block.shrine.upgrade.translocation;


import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.CropBlock;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.block.anchor.TranslocationAnchors;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineGameUpgradeTests;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

import java.util.List;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = ShrineGameUpgradeTests.GROUP)
public class TranslocationShrineUpgradeGameTests {

    private static final List<BlockPos> CROPS = List.of(
            new BlockPos(9, 1, 1),
            new BlockPos(9, 1, 2),
            new BlockPos(9, 1, 3),
            new BlockPos(9, 1, 5),
            new BlockPos(9, 1, 6),
            new BlockPos(9, 1, 7),
            new BlockPos(10, 1, 1),
            new BlockPos(10, 1, 2),
            new BlockPos(10, 1, 3),
            new BlockPos(10, 1, 5),
            new BlockPos(10, 1, 6),
            new BlockPos(10, 1, 7),
            new BlockPos(11, 1, 1),
            new BlockPos(11, 1, 2),
            new BlockPos(11, 1, 3),
            new BlockPos(11, 1, 4),
            new BlockPos(11, 1, 5),
            new BlockPos(11, 1, 6),
            new BlockPos(11, 1, 7)
    );
    public static final String TEMPLATE = "elementalcraft:translocationshrineupgradegametests.should_growcropsaroundanchor";

    @TestHolder(description = "Checks if the translocation shrine upgrade grows crops around the anchor")
    @GameTest(template = TEMPLATE)
    public static void should_growCropsAroundAnchor(GameTestHelper helper) {
        var upgrade = helper.getBlockEntity(new BlockPos(5, 1, 4), TranslocationShrineUpgradeBlockEntity.class);
        var shrine = ShrineGameTestHelper.getShrine(helper, new BlockPos(4, 1, 4));
        var targetPos = helper.absolutePos(new BlockPos(9, 1, 4));

        TranslocationAnchors.get(helper.getLevel()).add(targetPos);
        upgrade.setTarget(targetPos);
        shrine.refresh();

        assertThat(upgrade.getTarget()).isEqualTo(targetPos);
        assertThat(shrine.getTargetPos()).isEqualTo(targetPos);
        assertThat(shrine.getUpgradeCount(ShrineUpgrades.TRANSLOCATION)).isEqualTo(1);

        ShrineGameTestHelper.forcePeriods(shrine, CROPS.size() * 7);
        helper.succeedIf(() -> CROPS.forEach(pos -> helper.assertBlockState(pos, b -> b.getValue(CropBlock.AGE) == 7, _ -> Component.literal("Crop has not been grown"))));
    }
}
