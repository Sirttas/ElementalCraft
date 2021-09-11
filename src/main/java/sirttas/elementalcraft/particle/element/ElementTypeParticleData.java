package sirttas.elementalcraft.particle.element;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;

public class ElementTypeParticleData implements ParticleOptions, IElementTypeProvider {

	private ElementType elementType;
	private ParticleType<ElementTypeParticleData> type;

	@SuppressWarnings("deprecation")
	public static final ParticleOptions.Deserializer<ElementTypeParticleData> DESERIALIZER = new ParticleOptions.Deserializer<>() {
		@Override
		public ElementTypeParticleData fromCommand(ParticleType<ElementTypeParticleData> type, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			return new ElementTypeParticleData(type, ElementType.byName(reader.readString()));
		}

		@Override
		public ElementTypeParticleData fromNetwork(ParticleType<ElementTypeParticleData> type, FriendlyByteBuf buffer) {
			return new ElementTypeParticleData(type, ElementType.byName(buffer.readUtf()));
		}
	};

	public ElementTypeParticleData(ParticleType<ElementTypeParticleData> type, ElementType elementType) {
		this.elementType = elementType;
		this.type = type;
	}

	@Override
	public ParticleType<ElementTypeParticleData> getType() {
		return type;
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		buffer.writeUtf(elementType.getSerializedName());
	}

	@Override
	public String writeToString() {
		return getType().getRegistryName().toString() + " " + getElementType().getSerializedName();
	}

	@Override
	public ElementType getElementType() {
		return this.elementType;
	}

	public static Codec<ElementTypeParticleData> getCodec(ParticleType<ElementTypeParticleData> particleType) {
		return ElementType.CODEC.xmap(e -> new ElementTypeParticleData(particleType, e), ElementTypeParticleData::getElementType);
	}

	public static ParticleType<ElementTypeParticleData> createParticeLtype(boolean alwaysShow) {
		return new ParticleType<ElementTypeParticleData>(alwaysShow, DESERIALIZER) {
			@Override
			public Codec<ElementTypeParticleData> codec() {
				return getCodec(this);
			}
		};
	}
}