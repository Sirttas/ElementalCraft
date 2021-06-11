package sirttas.elementalcraft.world.feature.placement;

import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.HeightmapBasedPlacement;
import net.minecraft.world.gen.placement.NoPlacementConfig;

public class SourcePlacement extends HeightmapBasedPlacement<NoPlacementConfig> {

	public static final String NAME = "source";

	public SourcePlacement() {
		super(NoPlacementConfig.CODEC);
	}

	@Override
	protected Type type(NoPlacementConfig config) {
		return Type.MOTION_BLOCKING_NO_LEAVES;
	}

	private int getHeight(WorldDecoratingHelper helper, NoPlacementConfig config, int x, int z) {
		Predicate<BlockState> predicate = type(config).isOpaque();

		for (int y = helper.getSeaLevel(); y < helper.getGenDepth(); y++) {
			if (!predicate.test(helper.getBlockState(new BlockPos(x, y, z)))) {
				return y;
			}
		}
		return 0;
	}

	@Override
	public Stream<BlockPos> getPositions(WorldDecoratingHelper helper, Random rand, NoPlacementConfig config, BlockPos pos) {
		int x = pos.getX();
		int z = pos.getZ();
		int y = getHeight(helper, config, x, z) + 2;

		return y <= helper.getSeaLevel() || y >= 125 ? Stream.of() : Stream.of(new BlockPos(x, y, z));
	}

}
