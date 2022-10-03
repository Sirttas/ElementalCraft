package sirttas.elementalcraft.world.feature;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.world.feature.config.IElementTypeFeatureConfig;

public class SourceFeature extends Feature<IElementTypeFeatureConfig> {

	public static final String NAME = "source";
	public static final String NAME_ICY = NAME + "_icy";
	public static final String NAME_JUNGLE = NAME + "_jungle";
	public static final String NAME_MUSHROOM = NAME + "_mushroom";
	public static final String NAME_NETHER = NAME + "_nether";
	public static final String NAME_NETHER_ALL = NAME_NETHER + "_all";
	public static final String NAME_NETHER_FOREST = NAME_NETHER + "_forest";

	public static final String NAME_WET = NAME + "_wet";
	public static final String NAME_DRY = NAME + "_dry";
	public static final String NAME_END = NAME + "_end";
	public static final String NAME_FOREST = NAME + "_forest";
	public static final String NAME_HILL = NAME + "_hill";
	public static final String NAME_MOUNTAIN = NAME + "_mountain";
	public static final String NAME_PLAIN = NAME + "_plain";
	public static final String NAME_OCEAN = NAME + "_ocean";
	public static final String NAME_LUSH_CAVE = NAME + "_lush_cave";
	public static final String NAME_DRIPSTONE_CAVE = NAME + "_dripstone_cave";
	public static final String NAME_DEEP_DARK = NAME + "_deep_dark";
	public static final String NAME_UNDERGROUND = NAME + "_underground";
	public static final String NAME_SKY = NAME + "_sky";

	public SourceFeature() {
		super(IElementTypeFeatureConfig.CODEC);
	}

	@Override
	public boolean place(FeaturePlaceContext<IElementTypeFeatureConfig> context) {
		var level = context.level();
		var pos = context.origin();
		var type = context.config().getElementType(context.random());

		if (!level.isEmptyBlock(pos) || type == ElementType.NONE) {
			return false;
		}
		BlockState source = ECBlocks.SOURCE.get().defaultBlockState().setValue(ElementType.STATE_PROPERTY, type);

		level.setBlock(pos, source, 3);
		return true;
	}
}
