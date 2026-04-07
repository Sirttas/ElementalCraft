package sirttas.elementalcraft.block.shrine.upgrade.vertical;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.neoforged.testframework.DynamicTest;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.ExtendedGameTestHelper;
import net.neoforged.testframework.gametest.GameTest;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;
import sirttas.elementalcraft.block.shrine.harvest.HarvestShrineGameTests;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineGameUpgradeTests;
import sirttas.elementalcraft.block.shrine.upgrade.VerticalShrineUpgradeBlock;

@ForEachTest(groups = ShrineGameUpgradeTests.GROUP)
public class PlantingShrineUpgradeGameTests {

    // elementalcraft:plantingshrineupgradegametests.should_plantwheat
    @TestHolder(description = "Checks if the planting shrine upgrade plants wheat when used with a harvest shrine.")
    @GameTest(templateNamespace = ElementalCraftApi.MODID, template = "plantingshrineupgradegametests.should_plantwheat")
    public static void should_plantWheat(GameTestHelper helper) {
        helper.startSequence().thenExecute(() -> {
            verifyUpgradeIsPresent(helper, new BlockPos(3, 3, 3), Direction.UP);
        }).thenExecuteAfter(1, () -> {
            ShrineGameTestHelper.forcePeriods(helper, new BlockPos(3, 4, 3), HarvestShrineGameTests.POSES.size());
        }).thenExecuteAfter(1, () -> {
            HarvestShrineGameTests.POSES.forEach(p -> helper.assertBlockState(p, b -> b.is(Blocks.WHEAT) && !((CropBlock) b.getBlock()).isMaxAge(b), () -> "Block has not been harvested or planted back"));
            helper.assertItemEntityCountIs(Items.WHEAT, new BlockPos(3, 2, 3), 3, HarvestShrineGameTests.POSES.size());
        }).thenSucceed();
    }

    @TestHolder(description = "Checks if the planting shrine upgrade plants saplings when used with a lumber shrine.")
    @GameTest
    public static void should_plantSaplings(DynamicTest test) {
        test.registerGameTestTemplate(() -> StructureTemplateBuilder.withSize(11, 5, 11)
                .fill(0, 0, 0, 10, 0, 10, ECBlocks.WHITE_ROCK_BRICK.get())
                .fill(1, 0, 1, 9, 0, 9, Blocks.DIRT)
                .fill(1, 1, 1, 9, 1, 9, Blocks.OAK_LOG)
                .fill(1, 2, 1, 9, 4, 9, Blocks.OAK_LEAVES)
                .set(5, 1, 5, ECBlocks.LUMBER_SHRINE.get().defaultBlockState())
                .set(5, 2, 5, ECBlocks.PLANTING_SHRINE_UPGRADE.get().defaultBlockState().setValue(VerticalShrineUpgradeBlock.FACING, Direction.DOWN)));

        test.onGameTest(helper -> helper.startSequence().thenExecute(() -> {
            verifyUpgradeIsPresent(helper, new BlockPos(5, 3, 5), Direction.DOWN);
        }).thenExecuteAfter(1, () -> {
            ShrineGameTestHelper.forcePeriods(helper, new BlockPos(5, 2, 5), 350);
        }).thenExecuteAfter(1, () -> {
            assertSaplingPlanted(helper);
            helper.assertItemEntityCountIs(Items.OAK_LOG, new BlockPos(5, 2, 5), 6, 80);
        }).thenSucceed());
    }

    private static void assertSaplingPlanted(ExtendedGameTestHelper helper) {
        for (int x = 1; x < 10; x++) {
            for (int z = 1; z < 10; z++) {
                if (helper.getBlockState(new BlockPos(x, 2, z)).is(Blocks.OAK_SAPLING)) {
                    return;
                }
            }
        }
        throw new GameTestAssertException("No sapling has been planted.");
    }

    private static void verifyUpgradeIsPresent(GameTestHelper helper, BlockPos pos, Direction up) {
        if (helper.getBlockState(pos).isAir()) {
            helper.setBlock(pos, ECBlocks.PLANTING_SHRINE_UPGRADE.get().defaultBlockState().setValue(VerticalShrineUpgradeBlock.FACING, up));
            ElementalCraftApi.LOGGER.info("Planting shrine upgrade was not placed, placing it now.");
        }
    }
}
