package sirttas.elementalcraft.network.message;

import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import sirttas.elementalcraft.ElementalCraft;

public class MessageHandler {

	private static final String PROTOCOL_VERSION = "2";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(ElementalCraft.createRL("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals);

	public static void setup() {
		int id = 0;

		CHANNEL.<ECMessage>registerMessage(id++, ECMessage.class, ECMessage::encode, ECMessage::decode, ECMessage::handle); // NOSONAR - sonar don't like the i++ in the last registration
	}
}
