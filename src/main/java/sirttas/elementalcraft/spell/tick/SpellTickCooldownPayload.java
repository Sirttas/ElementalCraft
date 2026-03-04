package sirttas.elementalcraft.spell.tick;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.network.payload.PayloadHelper;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.Spells;

public record SpellTickCooldownPayload(Spell spell) implements CustomPacketPayload {

	public static final CustomPacketPayload.Type<SpellTickCooldownPayload> TYPE = PayloadHelper.createType("spell_tick_cooldown");
	public static final StreamCodec<FriendlyByteBuf, SpellTickCooldownPayload> STREAM_CODEC = StreamCodec.of((b, p) -> p.write(b), SpellTickCooldownPayload::new);

	public SpellTickCooldownPayload(FriendlyByteBuf buf) {
		this(Spells.REGISTRY.get(buf.readIdentifier()));
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeIdentifier(spell.getKey());
	}

	@Override
	public @NotNull Type<SpellTickCooldownPayload> type() {
		return TYPE;
	}

	public void handle(IPayloadContext payloadContext) {
		payloadContext.enqueueWork(() -> SpellTickHelper.startCooldown(payloadContext.player(), this.spell));
	}
}
