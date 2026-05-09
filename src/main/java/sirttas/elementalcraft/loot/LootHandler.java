package sirttas.elementalcraft.loot;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;

import java.util.List;

@EventBusSubscriber(modid = ElementalCraftApi.MODID)
public final class LootHandler {

	private static final ResourceKey<LootTable> CHEST_INJECT_KEY = ResourceKey.create(Registries.LOOT_TABLE, ElementalCraftApi.identifier("chests/inject"));
	private static final LootPool CHEST_INJECT = LootPool.lootPool()
			.add(NestedLootTable.lootTableReference(CHEST_INJECT_KEY).setWeight(1))
			.setBonusRolls(UniformGenerator.between(0, 1))
			.name("elementalcraft_inject")
			.build();
	private static final List<String> BLACKLIST = List.of("dispenser");

	private LootHandler() {}
	
	@SubscribeEvent
	public static void lootLoad(LootTableLoadEvent evt) {
		Identifier name = evt.getName();

		 if (name.toString().startsWith("minecraft:chests/") && BLACKLIST.stream().anyMatch(name.toString()::contains)) {
			evt.getTable().addPool(CHEST_INJECT);
		}
	}
}
