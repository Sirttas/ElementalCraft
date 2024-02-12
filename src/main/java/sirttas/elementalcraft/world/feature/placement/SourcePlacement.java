package sirttas.elementalcraft.world.feature.placement;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public class SourcePlacement extends PlacementModifier {

	public static final String NAME = "source";

	public static final SourcePlacement INSTANCE = new SourcePlacement();
	public static final Codec<SourcePlacement> CODEC = Codec.unit(INSTANCE);

	private SourcePlacement() {}

	public static int getHeight(LevelAccessor level, int x, int z) {
		var y = level.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, x, z);

		if (y < level.getMaxBuildHeight()) {
			return Math.max(level.getSeaLevel(), y) + level.getRandom().nextInt(4);
		}
		return 0;
	}

	private int getHeight(PlacementContext helper, int x, int z) {
		var h = getHeight(helper.getLevel(), x, z);

		return Math.min(h, helper.getGenDepth());
	}

	@Nonnull
	@Override
	public Stream<BlockPos> getPositions(PlacementContext helper, @Nonnull RandomSource rand, BlockPos pos) {
		var level = helper.getLevel();
		int x = pos.getX();
		int z = pos.getZ();
		int y = getHeight(helper, x, z);

		return y <= level.getSeaLevel() || y >= level.dimensionType().logicalHeight() ? Stream.of() : Stream.of(new BlockPos(x, y, z));
	}

	@Nonnull
	@Override
	public PlacementModifierType<?> type() {
		return ECPlacements.SOURCE.get();
	}

}
