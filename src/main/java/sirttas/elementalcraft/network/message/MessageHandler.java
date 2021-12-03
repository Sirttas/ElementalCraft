package sirttas.elementalcraft.network.message;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.item.source.analysis.SourceAnalysisGlassMessage;
import sirttas.elementalcraft.item.spell.book.SpellBookMessage;
import sirttas.elementalcraft.spell.SpellTickCooldownMessage;

public class MessageHandler {

	private static final String PROTOCOL_VERSION = "9";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(ElementalCraft.createRL("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals);

	private MessageHandler() {}
	
	public static void setup() {
		int id = 0;

		CHANNEL.registerMessage(id++, ECMessage.class, ECMessage::encode, ECMessage::decode, ECMessage::handle);
		CHANNEL.registerMessage(id++, SpellBookMessage.class, SpellBookMessage::encode, SpellBookMessage::decode, SpellBookMessage::handle);
		CHANNEL.registerMessage(id++, SpellTickCooldownMessage.class, SpellTickCooldownMessage::encode, SpellTickCooldownMessage::decode, SpellTickCooldownMessage::handle);
		CHANNEL.registerMessage(id++, SourceAnalysisGlassMessage.class, SourceAnalysisGlassMessage::encode, SourceAnalysisGlassMessage::decode, SourceAnalysisGlassMessage::handle);
	}
}
