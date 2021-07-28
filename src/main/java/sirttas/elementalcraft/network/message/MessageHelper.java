package sirttas.elementalcraft.network.message;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

public class MessageHelper {

	private MessageHelper() {}
	
	public static <T> void sendToPlayer(ServerPlayer serverPlayer, T message) {
		MessageHandler.CHANNEL.sendTo(message, serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
	}

	public static <T> void sendToAllPlayers(T message) {
		MessageHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), message);
	}
}
