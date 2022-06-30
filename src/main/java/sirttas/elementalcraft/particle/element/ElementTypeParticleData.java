package sirttas.elementalcraft.particle.element;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;

import javax.annotation.Nonnull;

public class ElementTypeParticleData implements ParticleOptions, IElementTypeProvider {

	private final ElementType elementType;
	private final ParticleType<ElementTypeParticleData> type;

	private ResourceLocation key;

	@SuppressWarnings("deprecation")
	public static final ParticleOptions.Deserializer<ElementTypeParticleData> DESERIALIZER = new ParticleOptions.Deserializer<>() {
		@Nonnull
        @Override
		public ElementTypeParticleData fromCommand(@Nonnull ParticleType<ElementTypeParticleData> type, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			return new ElementTypeParticleData(type, ElementType.byName(reader.readString()));
		}

		@Nonnull
        @Override
		public ElementTypeParticleData fromNetwork(@Nonnull ParticleType<ElementTypeParticleData> type, FriendlyByteBuf buffer) {
			return new ElementTypeParticleData(type, ElementType.byName(buffer.readUtf()));
		}
	};

	public ElementTypeParticleData(ParticleType<ElementTypeParticleData> type, ElementType elementType) {
		this.elementType = elementType;
		this.type = type;
	}

	@Nonnull
    @Override
	public ParticleType<ElementTypeParticleData> getType() {
		return type;
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		buffer.writeUtf(elementType.getSerializedName());
	}

	@Nonnull
    @Override
	public String writeToString() {
		return getKey().toString() + " " + getElementType().getSerializedName();
	}

	private ResourceLocation getKey() {
		if (key == null) {
			key = ForgeRegistries.PARTICLE_TYPES.getKey(getType());
		}
		return key;
	}

	@Override
	public ElementType getElementType() {
		return this.elementType;
	}

	public static Codec<ElementTypeParticleData> getCodec(ParticleType<ElementTypeParticleData> particleType) {
		return ElementType.CODEC.xmap(e -> new ElementTypeParticleData(particleType, e), ElementTypeParticleData::getElementType);
	}

	public static ParticleType<ElementTypeParticleData> createParticleType(boolean alwaysShow) {
		return new ParticleType<>(alwaysShow, DESERIALIZER) {
			@Nonnull
            @Override
			public Codec<ElementTypeParticleData> codec() {
				return getCodec(this);
			}
		};
	}
}
