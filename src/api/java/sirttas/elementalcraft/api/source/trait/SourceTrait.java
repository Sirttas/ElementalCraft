package sirttas.elementalcraft.api.source.trait;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValueProvider;

import javax.annotation.Nullable;

public class SourceTrait implements Comparable<SourceTrait> {
	
	public static final String NAME = "source_traits";
	public static final String FOLDER = ElementalCraftApi.MODID + '_' + NAME;
	public static final Codec<SourceTrait> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Codec.FLOAT.optionalFieldOf(ECNames.CHANCE, 1F).forGetter(SourceTrait::getChance),
			Codec.INT.fieldOf(ECNames.ORDER).forGetter(SourceTrait::getOrder),
			IBlockPosPredicate.CODEC.optionalFieldOf(ECNames.PREDICATE, IBlockPosPredicate.any()).forGetter(SourceTrait::getPredicate),
			ISourceTraitValueProvider.CODEC.fieldOf(ECNames.VALUE).forGetter(t -> t.valueProvider)
	).apply(builder, SourceTrait::new));
	
	private ResourceLocation id;
	private final float chance;
	private final int order;
	private final IBlockPosPredicate predicate;
	private final ISourceTraitValueProvider valueProvider;
	
	public static Builder builder() {
		return new Builder();
	}
	
	public SourceTrait(float chance, int order, IBlockPosPredicate predicate, ISourceTraitValueProvider valueProvider) {
		this.chance = chance;
		this.order = order;
		this.predicate = predicate;
		this.valueProvider = valueProvider;
	}
	
	public ResourceLocation getId() {
		return id;
	}

	public void setId(ResourceLocation id) {
		this.id = id;
	}
	
	public float getChance() {
		return chance;
	}
	
	public int getOrder() {
		return order;
	}
	
	public IBlockPosPredicate getPredicate() {
		return predicate;
	}
	
	@Nullable
	public ISourceTraitValue roll(Level level, BlockPos pos) {
		if (predicate.test(level, pos) && level.random.nextDouble() < chance) {
			return valueProvider.roll(this, level, pos);
		}
		return null;
	}

	@Nullable
	public ISourceTraitValue load(Tag tag) {
		return valueProvider.load(tag);
	}

	@Nullable
	public Tag save(ISourceTraitValue value) {
		return valueProvider.save(value);
	}
	
	@Override
	public int compareTo(SourceTrait other) {
		return this.order - other.order;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		} else if (other == this) {
			return true;
		}
		return other instanceof SourceTrait trait && this.id != null && this.id.equals(trait.id);
	}
	
	@Override
	public int hashCode() {
		return this.id != null ? this.id.hashCode() : 0;
	}
	
	public static class Builder {
		
		public static final Encoder<Builder> ENCODER = SourceTrait.CODEC.comap(builder -> new SourceTrait(builder.chance, builder.order, builder.predicate, builder.valueProvider));

		private int order;
		private float chance;
		private IBlockPosPredicate predicate;
		private ISourceTraitValueProvider valueProvider;
		
		private Builder() {
			chance = 1F;
			order = 0;
			predicate = IBlockPosPredicate.any();
		}
		
		public Builder chance(float chance) {
			this.chance = chance;
			return this;
		}
		
		public Builder order(int order) {
			this.order = order;
			return this;
		}
		
		public Builder predicate(IBlockPosPredicate predicate) {
			this.predicate = predicate;
			return this;
		}
		
		public Builder value(ISourceTraitValueProvider valueProvider) {
			this.valueProvider = valueProvider;
			return this;
		}
	}
}
