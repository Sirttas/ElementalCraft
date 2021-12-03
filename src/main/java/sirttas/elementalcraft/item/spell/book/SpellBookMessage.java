package sirttas.elementalcraft.item.spell.book;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import sirttas.elementalcraft.network.message.MessageHelper;

import java.util.function.Supplier;

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


	public void handle(Supplier<NetworkEvent.Context> ctx) {
		MessageHelper.handleMenuMessage(ctx, SpellBookMenu.class, m -> m.setBook(book));
	}
}
