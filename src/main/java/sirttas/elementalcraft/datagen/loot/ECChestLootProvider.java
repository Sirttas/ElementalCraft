package sirttas.elementalcraft.datagen.loot;

import java.io.IOException;
import java.nio.file.Path;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.item.Items;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.IRandomRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.loot.function.ECLootFunctions;
import sirttas.elementalcraft.loot.function.RandomSpell;

/**
 * greatly inspired by Botania
 *
 * 
 */
public class ECChestLootProvider extends AbstractECLootProvider {

	public ECChestLootProvider(DataGenerator generator) {
		super(generator);
		ECLootFunctions.setup();
	}

	@Override
	public void act(DirectoryCache cache) throws IOException {
		save(cache, genInject(), ElementalCraft.createRL("inject"));
		save(cache, genSmallAltar(ElementType.FIRE), ElementalCraft.createRL("altar/small_fire"));
		save(cache, genMediumAltar(ElementType.FIRE), ElementalCraft.createRL("altar/medium_fire"));
		save(cache, genSmallAltar(ElementType.WATER), ElementalCraft.createRL("altar/small_water"));
		save(cache, genMediumAltar(ElementType.WATER), ElementalCraft.createRL("altar/medium_water"));
		save(cache, genSmallAltar(ElementType.AIR), ElementalCraft.createRL("altar/small_air"));
		save(cache, genMediumAltar(ElementType.AIR), ElementalCraft.createRL("altar/medium_air"));
		save(cache, genSmallAltar(ElementType.EARTH), ElementalCraft.createRL("altar/small_earth"));
		save(cache, genMediumAltar(ElementType.EARTH), ElementalCraft.createRL("altar/medium_earth"));
	}

	private static LootPool.Builder genInject() {
		return genBase(RandomValueRange.of(0, 2))
				.addEntry(ItemLootEntry.builder(ECItems.INERT_CRYSTAL).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))).weight(20))
				.addEntry(ItemLootEntry.builder(ECItems.FIRE_CRYSTAL).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10))
				.addEntry(ItemLootEntry.builder(ECItems.EARTH_CRYSTAL).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10))
				.addEntry(ItemLootEntry.builder(ECItems.WATER_CRYSTAL).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10))
				.addEntry(ItemLootEntry.builder(ECItems.AIR_CRYSTAL).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10))
				.addEntry(ItemLootEntry.builder(ECItems.FIRE_SHARD).acceptFunction(SetCount.builder(RandomValueRange.of(3, 8))).weight(5))
				.addEntry(ItemLootEntry.builder(ECItems.WATER_SHARD).acceptFunction(SetCount.builder(RandomValueRange.of(3, 8))).weight(5))
				.addEntry(ItemLootEntry.builder(ECItems.EARTH_SHARD).acceptFunction(SetCount.builder(RandomValueRange.of(3, 8))).weight(5))
				.addEntry(ItemLootEntry.builder(ECItems.AIR_SHARD).acceptFunction(SetCount.builder(RandomValueRange.of(3, 8))).weight(5))
				.addEntry(ItemLootEntry.builder(ECItems.POWERFUL_FIRE_SHARD).weight(2))
				.addEntry(ItemLootEntry.builder(ECItems.POWERFUL_WATER_SHARD).weight(2))
				.addEntry(ItemLootEntry.builder(ECItems.POWERFUL_EARTH_SHARD).weight(2))
				.addEntry(ItemLootEntry.builder(ECItems.POWERFUL_AIR_SHARD).weight(2))
				.addEntry(ItemLootEntry.builder(ECItems.SCROLL).acceptFunction(RandomSpell.builder()).weight(15));
	}

	private static LootTable.Builder genSmallAltar(ElementType type) {
		return genWithType(RandomValueRange.of(2, 4), type);
	}

	private static LootTable.Builder genMediumAltar(ElementType type) {
		return addAdvanced(genWithType(RandomValueRange.of(3, 6), type), RandomValueRange.of(1, 3), type);
	}

	private static LootPool.Builder genBase(IRandomRange range) {
		return LootPool.builder().name("main").rolls(range)
				.addEntry(ItemLootEntry.builder(ECItems.DRENCHED_IRON_INGOT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10))
				.addEntry(ItemLootEntry.builder(ECItems.DRENCHED_IRON_NUGGET).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))).weight(15))
				.addEntry(ItemLootEntry.builder(ECItems.SWIFT_ALLOY_INGOT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))).weight(5))
				.addEntry(ItemLootEntry.builder(ECItems.SWIFT_ALLOY_NUGGET).acceptFunction(SetCount.builder(RandomValueRange.of(2, 4))).weight(7))
				.addEntry(ItemLootEntry.builder(ECItems.SCROLL_PAPER).acceptFunction(SetCount.builder(RandomValueRange.of(2, 4))).weight(8));
	}
	
	private static LootTable.Builder genWithType(IRandomRange range, ElementType type) {
		return addVanilla(LootTable.builder().addLootPool(genBase(range)
				.addEntry(ItemLootEntry.builder(ECItems.INERT_CRYSTAL).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10))
				.addEntry(ItemLootEntry.builder(getCrystalForType(type)).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(40))
				.addEntry(ItemLootEntry.builder(getShardForType(type)).acceptFunction(SetCount.builder(RandomValueRange.of(4, 10))).weight(20))
				.addEntry(ItemLootEntry.builder(getPowerfulShardForType(type)).weight(5))
				.addEntry(ItemLootEntry.builder(ECItems.SCROLL).acceptFunction(RandomSpell.builder(type)).weight(15))));
	}
	
	private static LootTable.Builder addAdvanced(LootTable.Builder builder, IRandomRange range, ElementType type) {
		return builder.addLootPool(LootPool.builder().name("advanced").rolls(range)
				.addEntry(ItemLootEntry.builder(Items.GOLD_INGOT).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 4.0F))).weight(15))
				.addEntry(ItemLootEntry.builder(ECItems.SWIFT_ALLOY_INGOT).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10))
				.addEntry(ItemLootEntry.builder(ECItems.SWIFT_ALLOY_NUGGET).acceptFunction(SetCount.builder(RandomValueRange.of(3, 5))).weight(15))
				.addEntry(ItemLootEntry.builder(ECItems.SCROLL).acceptFunction(RandomSpell.builder(type)).weight(15))
				.addEntry(ItemLootEntry.builder(ECItems.PURE_CRYSTAL).weight(5))
				.addEntry(ItemLootEntry.builder(ECItems.EMPTY_RECEPTACLE).weight(2)));
		
	}
	
	private static LootTable.Builder addVanilla(LootTable.Builder builder) {
		return builder.addLootPool(LootPool.builder().rolls(RandomValueRange.of(1.0F, 3.0F)).name("vanilla_1")
				.addEntry(ItemLootEntry.builder(Items.IRON_INGOT).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 4.0F))))
				.addEntry(ItemLootEntry.builder(Items.GOLD_INGOT).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 4.0F))))
				.addEntry(ItemLootEntry.builder(Items.BREAD).weight(20))
				.addEntry(ItemLootEntry.builder(Items.WHEAT).weight(20).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 4.0F))))
				.addEntry(ItemLootEntry.builder(Items.BUCKET).weight(10))
				.addEntry(ItemLootEntry.builder(Items.REDSTONE).weight(15).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 4.0F))))
				.addEntry(ItemLootEntry.builder(Items.COAL).weight(15).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 4.0F))))
				.addEntry(ItemLootEntry.builder(Items.MELON_SEEDS).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(2.0F, 4.0F))))
				.addEntry(ItemLootEntry.builder(Items.PUMPKIN_SEEDS).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(2.0F, 4.0F))))
				.addEntry(ItemLootEntry.builder(Items.BEETROOT_SEEDS).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(2.0F, 4.0F)))))
		.addLootPool(LootPool.builder().rolls(ConstantRange.of(2)).name("vanilla_2")
				.addEntry(ItemLootEntry.builder(Items.BONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 8.0F))))
				.addEntry(ItemLootEntry.builder(Items.GUNPOWDER).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 8.0F))))
				.addEntry(ItemLootEntry.builder(Items.ROTTEN_FLESH).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 8.0F))))
				.addEntry(ItemLootEntry.builder(Items.STRING).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 8.0F))))
				.addEntry(ItemLootEntry.builder(Items.ENDER_PEARL).weight(2)));
	}


	private void save(DirectoryCache cache, LootPool.Builder pool, ResourceLocation location) throws IOException {
		save(cache, LootTable.builder().addLootPool(pool), location);
	}

	private void save(DirectoryCache cache, LootTable.Builder builder, ResourceLocation location) throws IOException {
		save(cache, builder.setParameterSet(LootParameterSets.CHEST), getPath(location));
	}

	private Path getPath(ResourceLocation id) {
		return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/loot_tables/chests/" + id.getPath() + ".json");
	}

	@Override
	public String getName() {
		return "ElementalCraft inject loot tables";
	}
}
