package sirttas.elementalcraft.spell.tick;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.Spells;

import javax.annotation.Nonnull;

public record SpellTickCooldownPayload(Spell spell) implements CustomPacketPayload {

	public static final ResourceLocation ID = ElementalCraftApi.createRL("spell_tick_cooldown");

	public SpellTickCooldownPayload(FriendlyByteBuf buf) {
		this(Spells.REGISTRY.get(buf.readResourceLocation()));
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeResourceLocation(spell.getKey());
	}

	public void handle(PlayPayloadContext ctx) {
		ctx.workHandler().execute(() -> ctx.player()
				.ifPresent(player -> SpellTickHelper.startCooldown(player, this.spell)));
	}

	@Override
	@Nonnull
	public ResourceLocation id() {
		return ID;
	}
}
