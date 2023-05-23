package sirttas.elementalcraft.block.shrine.upgrade.translocation;


import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.CropBlock;
import net.minecraftforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.entity.BlockEntityGameTestHelper;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

import java.util.List;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class TranslocationShrineUpgradeGameTests {

    private static final List<BlockPos> CROPS = List.of(
            new BlockPos(9, 2, 1),
            new BlockPos(9, 2, 2),
            new BlockPos(9, 2, 3),
            new BlockPos(9, 2, 5),
            new BlockPos(9, 2, 6),
            new BlockPos(9, 2, 7),
            new BlockPos(10, 2, 1),
            new BlockPos(10, 2, 2),
            new BlockPos(10, 2, 3),
            new BlockPos(10, 2, 5),
            new BlockPos(10, 2, 6),
            new BlockPos(10, 2, 7),
            new BlockPos(11, 2, 1),
            new BlockPos(11, 2, 2),
            new BlockPos(11, 2, 3),
            new BlockPos(11, 2, 4),
            new BlockPos(11, 2, 5),
            new BlockPos(11, 2, 6),
            new BlockPos(11, 2, 7)
    );

    // elementalcraft:translocationshrineupgradegametests.should_growcropsaroundanchor
    @GameTest(batch = ShrineGameTestHelper.BATCH_NAME)
    public static void should_growCropsAroundAnchor(GameTestHelper helper) {
        var upgrade = (TranslocationShrineUpgradeBlockEntity) BlockEntityGameTestHelper.getBlockEntity(helper, new BlockPos(5, 2, 4));
        var shrine = ShrineGameTestHelper.getShrine(helper, new BlockPos(4, 2, 4));
        var targetPos = helper.absolutePos(new BlockPos(9, 2, 4));

        upgrade.setTarget(targetPos);
        shrine.refresh();

        assertThat(upgrade.getTarget()).isEqualTo(targetPos);
        assertThat(shrine.getTargetPos()).isEqualTo(targetPos);
        assertThat(shrine.getUpgradeCount(ShrineUpgrades.TRANSLOCATION)).isEqualTo(1);

        ShrineGameTestHelper.forcePeriods(shrine, 100);
        helper.succeedIf(() -> CROPS.forEach(pos -> helper.assertBlockState(pos, b -> b.getValue(CropBlock.AGE) == 7, () -> "Crop has not been grown")));
    }
}
