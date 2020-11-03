package sirttas.elementalcraft.network.message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgradeManager;

public final class ShrineUpgradesMessage {

	private Map<ResourceLocation, ShrineUpgrade> upgrades;

	private ShrineUpgradesMessage() {
	}

	public ShrineUpgradesMessage(ShrineUpgradeManager manager) {
		this.upgrades = manager.getUpgrades();
	}

	// message handling

	public static ShrineUpgradesMessage decode(PacketBuffer buf) {
		ShrineUpgradesMessage message = new ShrineUpgradesMessage();
		int mapSize = buf.readInt();

		message.upgrades = new HashMap<>(mapSize);
		for (int i = 0; i < mapSize; i++) {
			message.upgrades.put(buf.readResourceLocation(), ShrineUpgrade.SEZRIALIZER.read(buf));
		}
		return message;
	}

	public void encode(PacketBuffer buf) {
		buf.writeInt(upgrades.size());
		upgrades.forEach((loc, upgrade) -> {
			buf.writeResourceLocation(loc);
			ShrineUpgrade.SEZRIALIZER.write(upgrade, buf);
		});
	}


	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> ElementalCraft.SHRINE_UPGRADE_MANAGER.setUpgrades(upgrades));
		ctx.get().setPacketHandled(true);
	}
}