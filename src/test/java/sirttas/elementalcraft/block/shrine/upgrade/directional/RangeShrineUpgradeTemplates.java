package sirttas.elementalcraft.block.shrine.upgrade.directional;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.testframework.annotation.RegisterStructureTemplate;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.block.ECBlocks;

import java.util.function.Supplier;

public class RangeShrineUpgradeTemplates {

    private RangeShrineUpgradeTemplates() {}

    public static final String HARVEST_SHRINE_WITH_1_RANGE_TEMPLATE_NAME = "elementalcraft:harvest_shrine_with_1_range";

    @RegisterStructureTemplate(HARVEST_SHRINE_WITH_1_RANGE_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> HARVEST_SHRINE_WITH_1_RANGE_TEMPLATE =  StructureTemplateBuilder.lazy(1, 2, 1, builder -> builder
            .set(0, 1, 0, ECBlocks.HARVEST_SHRINE.get().defaultBlockState())
            .set(0, 0, 0, ECBlocks.RANGE_SHRINE_UPGRADE.get().defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP)));

}
