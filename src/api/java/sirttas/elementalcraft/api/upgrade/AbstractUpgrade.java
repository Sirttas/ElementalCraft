package sirttas.elementalcraft.api.upgrade;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Products.P3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.LevelReader;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.name.ECNames;

public abstract class AbstractUpgrade<T> {

	private ResourceLocation id;
	protected IBlockPosPredicate predicate;
	protected int maxAmount;
	protected final Map<T, Float> bonuses;
	
	protected AbstractUpgrade(IBlockPosPredicate predicate, Map<T, Float> map, int maxAmount) {
		this.predicate = predicate;
		this.bonuses = map;
		this.maxAmount = maxAmount;
		this.id = null;
	}

	protected static <T extends StringRepresentable, U extends AbstractUpgrade<T>> P3<Mu<U>, IBlockPosPredicate, Map<T, Float>, Integer> codec(Instance<U> builder, Codec<T> bonusCodec) {
		return builder.group(
				IBlockPosPredicate.CODEC.fieldOf(ECNames.PREDICATE).forGetter(u -> u.predicate),
				Codec.unboundedMap(bonusCodec, Codec.FLOAT).optionalFieldOf(ECNames.BONUSES, ImmutableMap.of()).forGetter(AbstractUpgrade::getBonuses),
				Codec.INT.optionalFieldOf(ECNames.MAX_AMOUNT, 0).forGetter(u -> u.maxAmount)
		);
	}

	
	protected boolean canUpgrade(LevelReader world, BlockPos pos, int amount) {
		return (maxAmount == 0 || amount < maxAmount) && predicate.test(world, pos);
	}

	protected void merge(AbstractUpgrade<T> other) {
		this.predicate = this.predicate.or(other.predicate).simplify();
		other.bonuses.forEach((bonus, value) -> {
			if (bonuses.containsKey(bonus)) {
				bonuses.put(bonus, bonuses.get(bonus) * value);
			} else {
				bonuses.put(bonus, value);
			}
		});
		if (this.maxAmount == 0) {
			this.maxAmount = other.maxAmount;
		}
	}
	
	public final Map<T, Float> getBonuses() {
		return bonuses;
	}

	public ResourceLocation getId() {
		return id;
	}

	public final void setId(ResourceLocation id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return id != null ? id.toString() : super.toString();
	}

	@Override
	public int hashCode() {
		return this.id != null ? this.id.hashCode() : 1;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof AbstractUpgrade && this.id != null) {
			return this.id.equals(((AbstractUpgrade<?>) other).id);
		}
		return super.equals(other);
	}

}
