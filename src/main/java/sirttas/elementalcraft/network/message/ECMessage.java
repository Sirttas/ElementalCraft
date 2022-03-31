package sirttas.elementalcraft.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.tag.ECTags;

import java.util.function.Supplier;

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
		EntityHelper.handStream(player).filter(i -> i.is(ECTags.Items.SPELL_CAST_TOOLS)).findFirst().ifPresent(i -> SpellHelper.moveSelected(i, delta));
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();

			switch (type) {
				case SCROLL_BACKWORD -> handelScroll(player, -1);
				case SCROLL_FORWARD -> handelScroll(player, 1);
			}
		});
		ctx.get().setPacketHandled(true);
	}

	public void send() {
		MessageHandler.CHANNEL.sendToServer(this);
	}
}
