package sirttas.elementalcraft.network.message;

import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.spell.SpellHelper;

public final class ECMessage {

	public static final ECMessage SCROLL_FORWARD = new ECMessage(MessageType.SCROLL_FORWARD);
	public static final ECMessage SCROLL_BACKWORD = new ECMessage(MessageType.SCROLL_BACKWORD);

	private MessageType type = null;

	private ECMessage() {
	}

	private ECMessage(MessageType type) {
		this.type = type;
	}

	public enum MessageType {
		SCROLL_FORWARD, SCROLL_BACKWORD
	}

	public static ECMessage decode(PacketBuffer buf) {
		return new ECMessage(buf.readEnumValue(MessageType.class));
	}

	public void encode(PacketBuffer buf) {
		buf.writeEnumValue(type);
	}

	private void handelScroll(ServerPlayerEntity player, int delta) {
		EntityHelper.handStream(player).filter(i -> i.getItem() == ECItems.FOCUS).findFirst().ifPresent(i -> SpellHelper.moveSelected(i, delta));
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayerEntity player = ctx.get().getSender();

			switch (type) {
			case SCROLL_BACKWORD:
				handelScroll(player, -1);
				break;
			case SCROLL_FORWARD:
				handelScroll(player, 1);
				break;
			}
		});
		ctx.get().setPacketHandled(true);
	}

	public void send() {
		MessageHandler.CHANNEL.sendToServer(this);
	}
}