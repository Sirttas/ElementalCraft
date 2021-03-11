package sirttas.elementalcraft.item.spell.book;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public final class SpellBookMessage {

	private ItemStack book;

	private SpellBookMessage() {
	}

	public SpellBookMessage(ItemStack book) {
		this.book = book;
	}

	// message handling

	public static SpellBookMessage decode(PacketBuffer buf) {
		SpellBookMessage message = new SpellBookMessage();

		message.book = buf.readItemStack();
		return message;
	}

	public void encode(PacketBuffer buf) {
		buf.writeItemStack(book);
	}


	@SuppressWarnings("resource")
	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
				Container container = Minecraft.getInstance().player.openContainer;

				if (container instanceof SpellBookContainer) {
					((SpellBookContainer) container).setBook(book);
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}
}