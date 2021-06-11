package sirttas.elementalcraft.particle.element;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;

import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import sirttas.elementalcraft.api.element.ElementType;

public class ElementTypeParticleData implements IParticleData {

	private ElementType elementType;
	private ParticleType<ElementTypeParticleData> type;

	@SuppressWarnings("deprecation")
	public static final IParticleData.IDeserializer<ElementTypeParticleData> DESERIALIZER = new IParticleData.IDeserializer<ElementTypeParticleData>() {
		@Override
		public ElementTypeParticleData fromCommand(ParticleType<ElementTypeParticleData> type, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			return new ElementTypeParticleData(type, ElementType.byName(reader.readString()));
		}

		@Override
		public ElementTypeParticleData fromNetwork(ParticleType<ElementTypeParticleData> type, PacketBuffer buffer) {
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
	public void writeToNetwork(PacketBuffer buffer) {
		buffer.writeUtf(elementType.getSerializedName());
	}

	@Override
	public String writeToString() {
		return getType().getRegistryName().toString() + " " + getElementType().getSerializedName();
	}

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