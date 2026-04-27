package sirttas.elementalcraft.block.shrine.upgrade.horizontal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.RegisterStructureTemplate;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineGameUpgradeTests;

import java.util.function.Supplier;

@ForEachTest(groups = ShrineGameUpgradeTests.GROUP)
public class CrystalHarvestShrineUpgradeGameTests {

    public static final String TEMPLATE_NAME = "elementalcraft:crystalharvest";

    @RegisterStructureTemplate(TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> TEMPLATE = StructureTemplateBuilder.lazy(23, 4, 23, b -> b
            .fill(0, 0, 0, 22, 0, 22, Blocks.AMETHYST_BLOCK.defaultBlockState())
            .set(13, 1, 11, Blocks.AMETHYST_CLUSTER.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP))
            .set(11, 1, 11, ECBlocks.ORE_SHRINE.get().defaultBlockState())
            .set(12, 1, 11, ECBlocks.CRYSTAL_HARVEST_SHRINE_UPGRADE.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST)));

    @TestHolder(description = "Check that the Crystal Harvest Shrine Upgrade can harvest Amethyst Clusters")
    @GameTest(template =  TEMPLATE_NAME)
    public static void should_harvestAmethystClusters(ECGameTestHelper helper) {
        helper.startSequence().thenExecute(() -> {
            ShrineGameTestHelper.forcePeriods(helper, new BlockPos(11, 1, 11), 1);
        }).thenExecuteAfter(1, () -> {
            helper.assertBlockNotPresent(Blocks.AMETHYST_CLUSTER, new BlockPos(13, 1, 11));
            helper.assertItemEntityPresent(Items.AMETHYST_SHARD, new BlockPos(13, 1, 11), 3);
        }).thenSucceed();
    }
}
