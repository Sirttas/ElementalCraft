package sirttas.elementalcraft.network.message;

import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.item.spell.book.SpellBookMessage;

public class MessageHandler {

	private static final String PROTOCOL_VERSION = "7";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(ElementalCraft.createRL("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals);

	private MessageHandler() {}
	
	public static void setup() {
		int id = 0;

		CHANNEL.registerMessage(id++, ECMessage.class, ECMessage::encode, ECMessage::decode, ECMessage::handle);
		CHANNEL.registerMessage(id++, SpellBookMessage.class, SpellBookMessage::encode, SpellBookMessage::decode, SpellBookMessage::handle);
	}
}
