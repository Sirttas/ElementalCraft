package sirttas.elementalcraft.network.message;

import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgradesMessage;
import sirttas.elementalcraft.item.spell.book.SpellBookMessage;
import sirttas.elementalcraft.spell.properties.SpellPropertiesMessage;

public class MessageHandler {

	private static final String PROTOCOL_VERSION = "6";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(ElementalCraft.createRL("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals);

	public static void setup() {
		int id = 0;

		CHANNEL.registerMessage(id++, ECMessage.class, ECMessage::encode, ECMessage::decode, ECMessage::handle);
		CHANNEL.registerMessage(id++, ShrineUpgradesMessage.class, ShrineUpgradesMessage::encode, ShrineUpgradesMessage::decode, ShrineUpgradesMessage::handle);
		CHANNEL.registerMessage(id++, SpellPropertiesMessage.class, SpellPropertiesMessage::encode, SpellPropertiesMessage::decode, SpellPropertiesMessage::handle);
		CHANNEL.registerMessage(id++, SpellBookMessage.class, SpellBookMessage::encode, SpellBookMessage::decode, SpellBookMessage::handle);// NOSONAR - sonar don't like the i++ in the last registration
	}
}
