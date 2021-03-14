package sirttas.elementalcraft.world.feature;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
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
	public static final String NAME_FIRE_SPAWN = NAME_FIRE + "_spawn";
	public static final String NAME_WATER_SPAWN = NAME_WATER + "_spawn";
	public static final String NAME_EARTH_SPAWN = NAME_EARTH + "_spawn";
	public static final String NAME_AIR_SPAWN = NAME_AIR + "_spawn";

	public SourceFeature() {
		super(IElementTypeFeatureConfig.CODEC);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator structureManager, Random rand, BlockPos pos, IElementTypeFeatureConfig config) {
		ElementType type = config.getElementType(rand);

		if (type != ElementType.NONE) {
			BlockState source = ECBlocks.SOURCE.getDefaultState().with(ElementType.STATE_PROPERTY, type);

			world.setBlockState(pos, source, 3);
			return true;
		}
		return false;
	}
}
