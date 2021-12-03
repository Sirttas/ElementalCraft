package sirttas.elementalcraft.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MessageHelper {

	private MessageHelper() {}
	
	public static <T> void sendToPlayer(ServerPlayer serverPlayer, T message) {
		MessageHandler.CHANNEL.sendTo(message, serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
	}

	public static <T> void sendToAllPlayers(T message) {
		MessageHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), message);
	}
	
	@SuppressWarnings("resource")
	public static <T extends AbstractContainerMenu> void handleMenuMessage(Supplier<NetworkEvent.Context> ctx, Class<? extends T> type, Consumer<? super T> handler) {
		ctx.get().enqueueWork(() -> {
			if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
				AbstractContainerMenu menu = Minecraft.getInstance().player.containerMenu;

				if (type.isInstance(menu)) {
					handler.accept(type.cast(menu));
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
