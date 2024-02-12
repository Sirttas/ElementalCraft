package sirttas.elementalcraft.item.spell.book;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.network.payload.IMenuPayload;

import javax.annotation.Nonnull;

public record SpellBookPayload(
		ItemStack book
) implements IMenuPayload<SpellBookMenu> {

	public static final ResourceLocation ID = ElementalCraftApi.createRL("spell_book");

	public SpellBookPayload(FriendlyByteBuf buf) {
		this(buf.readItem());
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeItem(book);
	}

	@Override
	@Nonnull
	public ResourceLocation id() {
		return ID;
	}

	@Override
	public Class<? extends SpellBookMenu> getMenuType() {
		return SpellBookMenu.class;
	}

	@Override
	public void handleOnMenu(PlayPayloadContext ctx, SpellBookMenu menu) {
		menu.setBook(book);
	}
}
