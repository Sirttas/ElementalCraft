package sirttas.elementalcraft.spell;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public class SpellTickCooldownMessage {

	private final Spell spell;

	public SpellTickCooldownMessage(Spell spell) {
		this.spell = spell;
	}

	// message handling

	public static SpellTickCooldownMessage decode(PacketBuffer buf) {
		return new SpellTickCooldownMessage(Spell.REGISTRY.getValue(buf.readResourceLocation()));
	}

	public void encode(PacketBuffer buf) {
		buf.writeResourceLocation(spell.getRegistryName());
	}

	@SuppressWarnings("resource")
	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
				SpellTickManager.CLIENT_INSTANCE.setCooldown(Minecraft.getInstance().player, this.spell);
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
