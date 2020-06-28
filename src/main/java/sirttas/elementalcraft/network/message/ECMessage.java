package sirttas.elementalcraft.network.message;

import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import sirttas.elementalcraft.infusion.InfusionHelper;

public final class ECMessage {

	public static final ECMessage AIR_INFUSION = create(MessageType.AIR_INFUSION);

	private MessageType type = null;

	private ECMessage() {
	}

	private static ECMessage create(MessageType type) {
		ECMessage msg = new ECMessage();

		msg.type = type;
		return msg;
	}

	public MessageType getType() {
		return type;
	}

	public enum MessageType {
		AIR_INFUSION
	}

	// message handling

	public static ECMessage decode(PacketBuffer buf) {
		return ECMessage.create(buf.readEnumValue(MessageType.class));
	}

	public static void encode(ECMessage msg, PacketBuffer buf) {
		buf.writeEnumValue(msg.getType());
	}

	public static void handle(ECMessage msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayerEntity player = ctx.get().getSender();

			switch (msg.getType()) {
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
}