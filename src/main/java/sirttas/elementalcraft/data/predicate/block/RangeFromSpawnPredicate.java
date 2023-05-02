package sirttas.elementalcraft.data.predicate.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import sirttas.dpanvil.api.predicate.block.BlockPosPredicateType;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.name.ECNames;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RangeFromSpawnPredicate implements IBlockPosPredicate {

	public static final String NAME = "range_from_spawn";
	public static final Codec<RangeFromSpawnPredicate> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Codec.INT.fieldOf(ECNames.RANGE).forGetter(p -> p.range)
	).apply(builder, RangeFromSpawnPredicate::new));

	private final int range;
	private final int rangeSq;
	
	public RangeFromSpawnPredicate(int range) {
		this.range = range;
		this.rangeSq = range * range;
	}
	
	@Override
	public BlockPosPredicateType<RangeFromSpawnPredicate> getType() {
		return ECBlockPosPredicateTypes.RANGE_FROM_SPAWN.get();
	}

	@Override
	public boolean test(@Nonnull LevelReader level, @Nonnull BlockPos pos, @Nullable Direction direction) {
		BlockPos spawn = level instanceof ServerLevelAccessor accessor ? accessor.getLevel().getSharedSpawnPos() : BlockPos.ZERO;
		
		return new BlockPos(spawn.getX(), 0, spawn.getZ()).distSqr(new BlockPos(pos.getX(), 0, pos.getZ())) > rangeSq;
	}
}
