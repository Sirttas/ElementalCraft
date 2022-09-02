package sirttas.elementalcraft.network.message;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.vortex.VortexPullPlayerMessage;
import sirttas.elementalcraft.item.source.analysis.SourceAnalysisGlassMessage;
import sirttas.elementalcraft.item.spell.book.SpellBookMessage;
import sirttas.elementalcraft.jewel.handler.ActiveJewelsMessage;
import sirttas.elementalcraft.spell.tick.SpellTickCooldownMessage;

public class MessageHandler {

	private static final String PROTOCOL_VERSION = "11";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(ElementalCraft.createRL("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals);

	private MessageHandler() {}
	
	public static void setup() {
		int id = 0;

		CHANNEL.registerMessage(id++, ECMessage.class, ECMessage::encode, ECMessage::decode, ECMessage::handle);
		CHANNEL.registerMessage(id++, SpellBookMessage.class, SpellBookMessage::encode, SpellBookMessage::decode, SpellBookMessage::handle);
		CHANNEL.registerMessage(id++, SpellTickCooldownMessage.class, SpellTickCooldownMessage::encode, SpellTickCooldownMessage::decode, SpellTickCooldownMessage::handle);
		CHANNEL.registerMessage(id++, SourceAnalysisGlassMessage.class, SourceAnalysisGlassMessage::encode, SourceAnalysisGlassMessage::decode, SourceAnalysisGlassMessage::handle);
		CHANNEL.registerMessage(id++, ActiveJewelsMessage.class, ActiveJewelsMessage::encode, ActiveJewelsMessage::decode, ActiveJewelsMessage::handle);
		CHANNEL.registerMessage(id++, VortexPullPlayerMessage.class, VortexPullPlayerMessage::encode, VortexPullPlayerMessage::decode, VortexPullPlayerMessage::handle);
	}
}
