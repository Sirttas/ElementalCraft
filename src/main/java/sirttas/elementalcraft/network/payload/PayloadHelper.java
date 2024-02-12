package sirttas.elementalcraft.network.payload;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.ConfigurationPayloadContext;

public class PayloadHelper {

	private PayloadHelper() {}

	public static <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer serverPlayer, T payload) {
		PacketDistributor.PLAYER.with(serverPlayer).send(payload);
	}

	public static <T extends CustomPacketPayload> void sendToAllPlayers(ConfigurationPayloadContext ctx, T payload) {
		PacketDistributor.ALL.noArg().send(payload);
	}

	public static <T extends CustomPacketPayload> void sendToServer(T payload) {
		PacketDistributor.SERVER.noArg().send(payload);
	}
}
