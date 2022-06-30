package sirttas.elementalcraft.api.source.trait;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValueProvider;

import javax.annotation.Nullable;

public class SourceTrait implements Comparable<SourceTrait> {
	
	public static final String NAME = "source_traits";
	public static final String FOLDER = ElementalCraftApi.MODID + '/' + NAME;
	public static final Codec<SourceTrait> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Codec.INT.fieldOf(ECNames.ORDER).forGetter(SourceTrait::getOrder),
			ISourceTraitValueProvider.CODEC.fieldOf(ECNames.VALUE).forGetter(t -> t.valueProvider)
	).apply(builder, SourceTrait::new));
	
	private ResourceLocation id;
	private final int order;
	private final ISourceTraitValueProvider valueProvider;
	
	public static Builder builder() {
		return new Builder();
	}
	
	public SourceTrait(int order, ISourceTraitValueProvider valueProvider) {
		this.order = order;
		this.valueProvider = valueProvider;
	}
	
	public ResourceLocation getId() {
		return id;
	}

	public void setId(ResourceLocation id) {
		this.id = id;
	}
	
	public int getOrder() {
		return order;
	}
	
	@Nullable
	public ISourceTraitValue roll(Level level, BlockPos pos) {
		return valueProvider.roll(this, level, pos);
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
		
		public static final Encoder<Builder> ENCODER = SourceTrait.CODEC.comap(builder -> new SourceTrait(builder.order, builder.valueProvider));

		private int order;
		private ISourceTraitValueProvider valueProvider;
		
		private Builder() {
			order = 0;
		}
		
		public Builder order(int order) {
			this.order = order;
			return this;
		}
		
		public Builder value(ISourceTraitValueProvider valueProvider) {
			this.valueProvider = valueProvider;
			return this;
		}
	}
}
