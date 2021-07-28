package sirttas.elementalcraft.world.feature.placement;

import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.DecorationContext;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;

@SuppressWarnings("deprecation")
public class SourcePlacement extends FeatureDecorator<NoneDecoratorConfiguration> {

	public static final String NAME = "source";

	public SourcePlacement() {
		super(NoneDecoratorConfiguration.CODEC);
	}


	private int getHeight(DecorationContext helper, int x, int z) {
		Predicate<BlockState> predicate = Types.MOTION_BLOCKING_NO_LEAVES.isOpaque();

		for (int y = helper.getLevel().getSeaLevel(); y < helper.getGenDepth(); y++) {
			if (!predicate.test(helper.getBlockState(new BlockPos(x, y, z)))) {
				return y;
			}
		}
		return 0;
	}

	@Override
	public Stream<BlockPos> getPositions(DecorationContext helper, Random rand, NoneDecoratorConfiguration config, BlockPos pos) {
		int x = pos.getX();
		int z = pos.getZ();
		int y = getHeight(helper, x, z) + 2;

		return y <= helper.getLevel().getSeaLevel() || y >= 125 ? Stream.of() : Stream.of(new BlockPos(x, y, z)); // TODO use bedrock celing
	}

}
