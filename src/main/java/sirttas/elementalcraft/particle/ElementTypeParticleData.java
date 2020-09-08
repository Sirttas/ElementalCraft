package sirttas.elementalcraft.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementType;

@OnlyIn(Dist.CLIENT)
public class ElementTypeParticleData implements IParticleData {

	private ElementType elementType;
	private ParticleType<ElementTypeParticleData> type;

	static final IParticleData.IDeserializer<ElementTypeParticleData> DESERIALIZER = new IParticleData.IDeserializer<ElementTypeParticleData>() {
		@Override
		public ElementTypeParticleData deserialize(ParticleType<ElementTypeParticleData> type, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			return new ElementTypeParticleData(type, ElementType.byName(reader.readString()));
		}

		@Override
		public ElementTypeParticleData read(ParticleType<ElementTypeParticleData> type, PacketBuffer buffer) {
			return new ElementTypeParticleData(type, ElementType.byName(buffer.readString()));
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
	public void write(PacketBuffer buffer) {
		buffer.writeString(elementType.getName());
	}

	@Override
	public String getParameters() {
		return getType().getRegistryName().toString() + " " + getElementType().getName();
	}

	public ElementType getElementType() {
		return this.elementType;
	}
}