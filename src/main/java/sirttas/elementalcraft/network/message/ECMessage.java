package sirttas.elementalcraft.network.message;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.tag.ECTags;

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

	public static ECMessage decode(FriendlyByteBuf buf) {
		return new ECMessage(buf.readEnum(MessageType.class));
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeEnum(type);
	}

	private void handelScroll(ServerPlayer player, int delta) {
		EntityHelper.handStream(player).filter(i -> ECTags.Items.SPELL_CAST_TOOLS.contains(i.getItem())).findFirst().ifPresent(i -> SpellHelper.moveSelected(i, delta));
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();

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