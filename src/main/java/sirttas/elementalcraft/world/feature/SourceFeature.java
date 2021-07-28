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
	public static final String NAME_WET = NAME + "_wet";
	public static final String NAME_DRY = NAME + "_dry";
	public static final String NAME_END = NAME + "_end";
	public static final String NAME_FOREST = NAME + "_forest";
	public static final String NAME_HILL = NAME + "_hill";
	public static final String NAME_PLAIN = NAME + "_plain";
	public static final String NAME_OCEAN = NAME + "_ocean";
	public static final String NAME_FIRE = NAME + "_fire";
	public static final String NAME_WATER = NAME + "_water";
	public static final String NAME_EARTH = NAME + "_earth";
	public static final String NAME_AIR = NAME + "_air";
	public static final String SPAWN = "_spawn";
	public static final String NAME_FIRE_SPAWN = NAME_FIRE + SPAWN;
	public static final String NAME_WATER_SPAWN = NAME_WATER + SPAWN;
	public static final String NAME_EARTH_SPAWN = NAME_EARTH + SPAWN;
	public static final String NAME_AIR_SPAWN = NAME_AIR + SPAWN;

	public SourceFeature() {
		super(IElementTypeFeatureConfig.CODEC);
	}

	@Override
	public boolean place(FeaturePlaceContext<IElementTypeFeatureConfig> context) {
		ElementType type = context.config().getElementType(context.random());

		if (type != ElementType.NONE) {
			BlockState source = ECBlocks.SOURCE.defaultBlockState().setValue(ElementType.STATE_PROPERTY, type);

			context.level().setBlock(context.origin(), source, 3);
			return true;
		}
		return false;
	}
}
