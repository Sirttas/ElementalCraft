package sirttas.elementalcraft.api.source.trait;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValueProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SourceTrait {
	
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
	public ISourceTraitValue roll(Level level, BlockPos pos, float luck) {
		return valueProvider.roll(new SourceTraitRollContext(this, level.random, luck), level, pos);
	}

	@Nullable
	public ISourceTraitValue breed(RandomSource random, float luck, ISourceTraitValue value1, ISourceTraitValue value2) {
		return valueProvider.breed(new SourceTraitRollContext(this, random, luck),  value1, value2);
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

	public enum Type implements StringRepresentable {
		NONE(ECNames.NONE),
		CAPACITY(ECNames.ELEMENT_CAPACITY),
		RECOVER_RATE(ECNames.RECOVER_RATE),
		EXTRACTION_SPEED(ECNames.EXTRACTION_SPEED),
		PRESERVATION(ECNames.ELEMENT_PRESERVATION),
		BREEDING_COST(ECNames.BREEDING_COST);

		public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
		public static final Codec<Map<Type, Float>> VALUE_CODEC = valueCodec(Codec.FLOAT);

		public static <T> Codec<Map<Type, T>> valueCodec(Codec<T> valueCodec) {
			return Codec.unboundedMap(Type.CODEC, valueCodec);
		}

		private final String name;

		Type(String name) {
			this.name = name;
		}

		@Nonnull
		@Override
		public String getSerializedName() {
			return this.name;
		}

		public static Type byName(String name) {
			for (Type type : values()) {
				if (type.name.equals(name)) {
					return type;
				}
			}
			return NONE;
		}
	}

	public static class Builder {

		private static final AtomicInteger ORDER_INCREMENT = new AtomicInteger(0);

		public static final Encoder<Builder> ENCODER = SourceTrait.CODEC.comap(builder -> new SourceTrait(builder.order, builder.valueProvider));

		private int order;
		private ISourceTraitValueProvider valueProvider;
		
		private Builder() {
			order = ORDER_INCREMENT.getAndIncrement();
		}
		
		public Builder order(int order) {
			this.order = order;
			if (order >= ORDER_INCREMENT.get()) {
				ORDER_INCREMENT.set(order + 1);
			}
			return this;
		}
		
		public Builder value(ISourceTraitValueProvider valueProvider) {
			this.valueProvider = valueProvider;
			return this;
		}
	}
}
