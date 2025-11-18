package sirttas.elementalcraft.block.shrine.upgrade.directional;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.block.ECBlocks;

import java.util.function.Supplier;

public class RangeShrineUpgradeTemplates {

    private RangeShrineUpgradeTemplates() {}

    public static final Supplier<StructureTemplateBuilder> HARVEST_SHRINE_WITH_1_RANGE_TEMPLATE = Lazy.of(() -> StructureTemplateBuilder.withSize(1, 2, 1)
            .set(0, 1, 0, ECBlocks.HARVEST_SHRINE.get().defaultBlockState())
            .set(0, 0, 0, ECBlocks.RANGE_SHRINE_UPGRADE.get().defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP)));

}
