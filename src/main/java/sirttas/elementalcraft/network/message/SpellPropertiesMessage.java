package sirttas.elementalcraft.network.message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.spell.properties.SpellProperties;
import sirttas.elementalcraft.spell.properties.SpellPropertiesManager;

public final class SpellPropertiesMessage {

	private Map<ResourceLocation, SpellProperties> properties;

	private SpellPropertiesMessage() {
	}

	public SpellPropertiesMessage(SpellPropertiesManager manager) {
		this.properties = manager.getProperties();
	}

	// message handling

	public static SpellPropertiesMessage decode(PacketBuffer buf) {
		SpellPropertiesMessage message = new SpellPropertiesMessage();
		int mapSize = buf.readInt();

		message.properties = new HashMap<>(mapSize);
		for (int i = 0; i < mapSize; i++) {
			message.properties.put(buf.readResourceLocation(), SpellProperties.SEZRIALIZER.read(buf));
		}
		return message;
	}

	public void encode(PacketBuffer buf) {
		buf.writeInt(properties.size());
		properties.forEach((loc, prop) -> {
			buf.writeResourceLocation(loc);
			SpellProperties.SEZRIALIZER.write(prop, buf);
		});
	}


	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> ElementalCraft.SPELL_PROPERTIES_MANAGER.setProperties(properties));
		ctx.get().setPacketHandled(true);
	}
}