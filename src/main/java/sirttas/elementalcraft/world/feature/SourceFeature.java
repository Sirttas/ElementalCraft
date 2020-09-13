package sirttas.elementalcraft.world.feature;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.property.ECProperties;

public class SourceFeature extends Feature<NoFeatureConfig> {

	public static final String NAME = "source";

	public SourceFeature() {
		super(NoFeatureConfig::deserialize);
	}

	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		int x = pos.getX() + rand.nextInt(16);
		int z = pos.getZ() + rand.nextInt(16);
		int y = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z) + 2;
		BlockPos pos2 = new BlockPos(x, y, z);
		BlockState source = ECBlocks.source.getDefaultState().with(ECProperties.ELEMENT_TYPE, ElementType.random(rand));

		world.setBlockState(pos2, source, 3);
		return true;
	}

}
