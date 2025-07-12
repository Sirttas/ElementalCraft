package sirttas.elementalcraft.api.upgrade;

import com.mojang.datafixers.Products.P3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.LevelReader;
import net.neoforged.neoforge.common.util.Lazy;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.name.ECNames;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public abstract class AbstractUpgrade<T> {

	private ResourceLocation id;
	private IBlockPosPredicate predicate;
	protected int maxAmount;
	protected final Map<T, Float> bonuses;
	private Lazy<List<Component>> predicateTooltip;
	
	protected AbstractUpgrade(IBlockPosPredicate predicate, Map<T, Float> map, int maxAmount) {
		this.setPredicate(predicate);
		this.bonuses = map;
		this.maxAmount = maxAmount;
		this.id = null;
	}

	protected static <T extends StringRepresentable, U extends AbstractUpgrade<T>> P3<Mu<U>, IBlockPosPredicate, Map<T, Float>, Integer> codec(Instance<U> builder, Codec<T> bonusCodec) {
		return builder.group(
				IBlockPosPredicate.CODEC.fieldOf(ECNames.PREDICATE).forGetter(AbstractUpgrade::getPredicate),
				Codec.unboundedMap(bonusCodec, Codec.FLOAT).optionalFieldOf(ECNames.BONUSES, Map.of()).forGetter(AbstractUpgrade::getBonuses),
				Codec.INT.optionalFieldOf(ECNames.MAX_AMOUNT, 0).forGetter(u -> u.maxAmount)
		);
	}
	
	protected boolean canUpgrade(@Nonnull LevelReader level, @Nonnull BlockPos pos, @Nullable Direction direction, int amount) {
		return (maxAmount == 0 || amount < maxAmount) && predicate.test(level, pos, direction);
	}

	protected void merge(AbstractUpgrade<T> other) {
		this.setPredicate(this.predicate.or(other.predicate));
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

	public IBlockPosPredicate getPredicate() {
		return predicate;
	}

	public void setPredicate(IBlockPosPredicate predicate) {
		this.predicate = predicate.simplify();
		this.predicateTooltip = Lazy.of(predicate::getTooltip);
	}

	public List<Component> getPredicateTooltip() {
		return predicateTooltip.get();
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
		return this.id != null ? this.id.hashCode() : super.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof AbstractUpgrade && this.id != null) {
			return this.id.equals(((AbstractUpgrade<?>) other).id);
		}
		return super.equals(other);
	}

}
