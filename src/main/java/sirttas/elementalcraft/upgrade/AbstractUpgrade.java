package sirttas.elementalcraft.upgrade;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Products.P3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import sirttas.dpanvil.api.codec.Codecs;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.name.ECNames;

public abstract class AbstractUpgrade<T extends IStringSerializable> {

	private ResourceLocation id;
	protected IBlockPosPredicate predicate;
	protected int maxAmount;
	protected final Map<T, Float> bonuses;

	protected static <T extends IStringSerializable, U extends AbstractUpgrade<T>> P3<Mu<U>, IBlockPosPredicate, Map<T, Float>, Integer> codec(Instance<U> builder, Codec<T> bonusCodec) {
		return builder.group(
				Codecs.BLOCK_PREDICATE.fieldOf(ECNames.PREDICATE).forGetter(u -> u.predicate),
				Codec.unboundedMap(bonusCodec, Codec.FLOAT).optionalFieldOf(ECNames.BONUSES, ImmutableMap.of()).forGetter(AbstractUpgrade::getBonuses),
				Codec.INT.optionalFieldOf(ECNames.MAX_AMOUNT, 0).forGetter(u -> u.maxAmount)
		);
	}

	protected AbstractUpgrade(IBlockPosPredicate predicate, Map<T, Float> map, int maxAmount) {
		this.predicate = predicate;
		this.bonuses = map;
		this.maxAmount = maxAmount;
		this.id = null;
	}

	protected boolean canUpgrade(IWorldReader world, BlockPos pos, int amount) {
		return predicate.test(world, pos) && (maxAmount == 0 || amount < maxAmount);
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

}
