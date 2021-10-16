package sirttas.elementalcraft.data.predicate.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.dpanvil.api.predicate.block.BlockPosPredicateType;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;

public class RangeFromSpawnPredicate implements IBlockPosPredicate {

	public static final String NAME = "range_from_spawn";
	@ObjectHolder(ElementalCraftApi.MODID + ":" + NAME) public static final BlockPosPredicateType<RangeFromSpawnPredicate> TYPE = null;
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
		return TYPE;
	}

	@Override
	public boolean test(LevelReader level, BlockPos pos) {
		BlockPos spawn = level instanceof ServerLevel serverLevel ? serverLevel.getSharedSpawnPos() : BlockPos.ZERO;
		
		return new BlockPos(spawn.getX(), 0, spawn.getZ()).distSqr(new BlockPos(pos.getX(), 0, pos.getZ())) > rangeSq;
	}
}
