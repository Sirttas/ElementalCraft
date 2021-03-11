package sirttas.elementalcraft.network.message;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;

public class MessageHelper {

	private MessageHelper() {}
	
	public static <T> void sendToPlayer(ServerPlayerEntity serverPlayer, T message) {
		MessageHandler.CHANNEL.sendTo(message, serverPlayer.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
	}

	public static <T> void sendToAllPlayers(T message) {
		MessageHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), message);
	}
}
