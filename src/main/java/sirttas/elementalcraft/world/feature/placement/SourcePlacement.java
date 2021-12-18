package sirttas.elementalcraft.world.feature.placement;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public class SourcePlacement extends PlacementModifier {

	public static final String NAME = "source";

	private static final SourcePlacement INSTANCE = new SourcePlacement();
	public static final Codec<SourcePlacement> CODEC = Codec.unit(INSTANCE);

	public static int getHeight(LevelAccessor level, int x, int z) {
		Predicate<BlockState> predicate = Types.MOTION_BLOCKING_NO_LEAVES.isOpaque();

		for (int y = level.getSeaLevel(); y < level.getMaxBuildHeight(); y++) {
			if (!predicate.test(level.getBlockState(new BlockPos(x, y, z)))) {
				return y + 2;
			}
		}
		return 0;
	}

	private int getHeight(PlacementContext helper, int x, int z) {
		var h = getHeight(helper.getLevel(), x, z);

		return Math.max(h, helper.getGenDepth());
	}

	@Nonnull
	@Override
	public Stream<BlockPos> getPositions(PlacementContext helper, @Nonnull Random rand, BlockPos pos) {
		var level = helper.getLevel();
		int x = pos.getX();
		int z = pos.getZ();
		int y = getHeight(helper, x, z);

		return y <= level.getSeaLevel() || y >= level.dimensionType().logicalHeight() ? Stream.of() : Stream.of(new BlockPos(x, y, z));
	}

	@Nonnull
	@Override
	public PlacementModifierType<?> type() {
		return ECPlacements.SOURCE;
	}

}
