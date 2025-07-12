package sirttas.elementalcraft.item.spell.book;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.network.payload.IMenuPayload;
import sirttas.elementalcraft.network.payload.PayloadHelper;

public record SpellBookPayload(
		ItemStack book
) implements IMenuPayload<SpellBookMenu> {

	public static final CustomPacketPayload.Type<SpellBookPayload> TYPE = PayloadHelper.createType("spell_book");
	public static final StreamCodec<RegistryFriendlyByteBuf, SpellBookPayload> STREAM_CODEC = ItemStack.STREAM_CODEC.map(SpellBookPayload::new, SpellBookPayload::book);

	@Override
	public Class<? extends SpellBookMenu> getMenuType() {
		return SpellBookMenu.class;
	}

	@Override
	public @NotNull Type<SpellBookPayload> type() {
		return TYPE;
	}

	@Override
	public void handleOnMenu(IPayloadContext payloadContext, SpellBookMenu menu) {
		menu.setBook(book);
	}
}
