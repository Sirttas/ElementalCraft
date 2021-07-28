package sirttas.elementalcraft.item.spell.book;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

public final class SpellBookMessage {

	private ItemStack book;

	private SpellBookMessage() {
	}

	public SpellBookMessage(ItemStack book) {
		this.book = book;
	}

	// message handling

	public static SpellBookMessage decode(FriendlyByteBuf buf) {
		SpellBookMessage message = new SpellBookMessage();

		message.book = buf.readItem();
		return message;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeItem(book);
	}


	@SuppressWarnings("resource")
	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
				AbstractContainerMenu container = Minecraft.getInstance().player.containerMenu;

				if (container instanceof SpellBookContainer) {
					((SpellBookContainer) container).setBook(book);
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}
}