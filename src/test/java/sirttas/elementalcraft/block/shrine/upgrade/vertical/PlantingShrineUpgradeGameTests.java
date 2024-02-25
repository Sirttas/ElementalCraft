package sirttas.elementalcraft.block.shrine.upgrade.vertical;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;
import sirttas.elementalcraft.block.shrine.harvest.HarvestShrineGameTests;

@GameTestHolder(ElementalCraftApi.MODID)
public class PlantingShrineUpgradeGameTests {


    // elementalcraft:plantingshrineupgradegametests.should_plantwheat
    @GameTest(batch = ShrineGameTestHelper.BATCH_NAME)
    public static void should_plantWheat(GameTestHelper helper) {
        helper.startSequence().thenExecute(() -> {
            if (helper.getBlockState(new BlockPos(3, 3, 3)).isAir()) {
                helper.setBlock(new BlockPos(3, 3, 3), ECBlocks.PLANTING_SHRINE_UPGRADE.get().defaultBlockState().setValue(AbstractVerticalShrineUpgradeBlock.FACING, Direction.UP));
                ElementalCraftApi.LOGGER.info("Planting shrine upgrade was not placed, placing it now.");
            }
        }).thenExecuteAfter(1, () -> {
            ShrineGameTestHelper.forcePeriods(helper, new BlockPos(3, 4, 3), HarvestShrineGameTests.POSES.size());
        }).thenExecuteAfter(1, () -> {
            HarvestShrineGameTests.POSES.forEach(p -> helper.assertBlockState(p, b -> b.is(Blocks.WHEAT) && !((CropBlock) b.getBlock()).isMaxAge(b), () -> "Block has not been harvested or planted back"));
            helper.assertItemEntityCountIs(Items.WHEAT, new BlockPos(3, 2, 3), 3, HarvestShrineGameTests.POSES.size());
        }).thenSucceed();
    }
}
