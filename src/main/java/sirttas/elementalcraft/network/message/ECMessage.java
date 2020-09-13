package sirttas.elementalcraft.network.message;

import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import sirttas.elementalcraft.infusion.InfusionHelper;

public final class ECMessage {

	public static final ECMessage AIR_INFUSION = new ECMessage(MessageType.AIR_INFUSION);

	private MessageType type = null;

	private ECMessage() {
	}

	private ECMessage(MessageType type) {
		this.type = type;
	}

	public enum MessageType {
		AIR_INFUSION
	}

	// message handling

	public static ECMessage decode(PacketBuffer buf) {
		return new ECMessage(buf.readEnumValue(MessageType.class));
	}

	public void encode(PacketBuffer buf) {
		buf.writeEnumValue(type);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayerEntity player = ctx.get().getSender();

			switch (type) { // NOSONAR
			case AIR_INFUSION:
				if (InfusionHelper.canAirInfusionFly(player)) {
					player.startFallFlying();
				} else {
					player.stopFallFlying();
				}
				break;
			}
		});
		ctx.get().setPacketHandled(true);
	}

	public void send() {
		MessageHandler.CHANNEL.sendToServer(this);
	}
}