package sirttas.elementalcraft.loot;

import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID)
public final class LootHandler {

	@SubscribeEvent
	public static void lootLoad(LootTableLoadEvent evt) {
		String prefix = "minecraft:chests/";
		String name = evt.getName().toString();

		if (name.startsWith(prefix)) {
			String file = name.substring(name.indexOf(prefix) + prefix.length());

			switch (file) {
			case "abandoned_mineshaft":
			case "desert_pyramid":
			case "jungle_temple":
			case "simple_dungeon":
			case "stronghold_corridor":
			case "village_blacksmith":
				evt.getTable().addPool(getInjectPool(file));
				break;
			default:
				break;
			}
		}
	}

	public static LootPool getInjectPool(String entryName) {
		return LootPool.builder().addEntry(getInjectEntry(entryName, 1)).bonusRolls(0, 1).name("ec_inject").build();
	}

	private static LootEntry.Builder<?> getInjectEntry(String name, int weight) {
		ResourceLocation table = new ResourceLocation(ElementalCraft.MODID, "inject/" + name);

		return TableLootEntry.builder(table).weight(weight);
	}

}