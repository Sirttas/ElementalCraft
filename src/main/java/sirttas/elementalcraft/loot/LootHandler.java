package sirttas.elementalcraft.loot;

import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID)
public final class LootHandler {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void lootLoad(LootTableLoadEvent evt) {
		String prefix = "minecraft:chests/";
		String name = evt.getName().toString();

		if (name.startsWith(prefix)) {
			evt.getTable().addPool(getInjectPool("chests/inject"));
		}
	}

	public static LootPool getInjectPool(String entryName) {
		return LootPool.builder().addEntry(getInjectEntry(entryName, 1)).bonusRolls(0, 1).name("elementalcraft_inject").build();
	}

	private static LootEntry.Builder<?> getInjectEntry(String name, int weight) {
		ResourceLocation table = ElementalCraft.createRL(name);

		return TableLootEntry.builder(table).weight(weight);
	}

}