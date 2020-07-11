package sirttas.elementalcraft.world;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.property.ECProperties;

public class SourceFeature extends Feature<NoFeatureConfig> {

	public SourceFeature(Codec<NoFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean func_230362_a_/* place */(ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random rand, BlockPos pos, NoFeatureConfig config) {
		int x = pos.getX() + rand.nextInt(16);
		int z = pos.getZ() + rand.nextInt(16);
		int y = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z) + 2;
		BlockPos pos2 = new BlockPos(x, y, z);
		BlockState source = ECBlocks.source.getDefaultState().with(ECProperties.ELEMENT_TYPE, ElementType.random());

		world.setBlockState(pos2, source, 3);
		return true;
	}

}
