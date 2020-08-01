package sirttas.elementalcraft.loot;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.Lists;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID)
public final class LootHandler {

	public static final Collection<String> INJECTED_TABLES = Collections
			.unmodifiableCollection(Lists.newArrayList("abandoned_mineshaft", "desert_pyramid", "jungle_temple", "simple_dungeon", "stronghold_corridor", "village_blacksmith"));

	@SubscribeEvent
	public static void lootLoad(LootTableLoadEvent evt) {
		String prefix = "minecraft:chests/";
		String name = evt.getName().toString();

		if (name.startsWith(prefix)) {
			String file = name.substring(name.indexOf(prefix) + prefix.length());

			if (INJECTED_TABLES.contains(file)) {
				evt.getTable().addPool(getInjectPool(file));
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