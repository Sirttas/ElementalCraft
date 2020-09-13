package sirttas.elementalcraft.data.loot;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
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
		save(cache, genInject(), new ResourceLocation(ElementalCraft.MODID, "inject"));
		save(cache, genFire(), new ResourceLocation(ElementalCraft.MODID, "altar/small_fire"));
		save(cache, genWater(), new ResourceLocation(ElementalCraft.MODID, "altar/small_water"));
		save(cache, genAir(), new ResourceLocation(ElementalCraft.MODID, "altar/small_air"));
		save(cache, genEarth(), new ResourceLocation(ElementalCraft.MODID, "altar/small_earth"));
	}

	private static Path getPath(Path root, ResourceLocation id) {
		return root.resolve("data/" + id.getNamespace() + "/loot_tables/chests/" + id.getPath() + ".json");
	}

	private static LootPool.Builder genInject() {
		List<LootEntry.Builder<?>> entries = Lists.newArrayList(
				ItemLootEntry.builder(ECItems.inertCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))).weight(20),
				ItemLootEntry.builder(ECItems.fireCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10),
				ItemLootEntry.builder(ECItems.earthCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10),
				ItemLootEntry.builder(ECItems.waterCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10),
				ItemLootEntry.builder(ECItems.airCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10),
				ItemLootEntry.builder(ECItems.drenchedIronIngot).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10),
				ItemLootEntry.builder(ECItems.swiftAlloyIngot).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))).weight(5),
				ItemLootEntry.builder(ECItems.scroll).acceptFunction(RandomSpell.builder()).weight(15), 
				ItemLootEntry.builder(ECItems.emptyReceptacle).weight(2));
		LootPool.Builder pool = LootPool.builder().name("main").rolls(RandomValueRange.of(1, 2));

		entries.forEach(pool::addEntry);
		return pool;
	}

	private static LootPool.Builder genBase() {
		List<LootEntry.Builder<?>> entries = Lists.newArrayList(
				ItemLootEntry.builder(ECItems.drenchedIronIngot).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10),
				ItemLootEntry.builder(ECItems.swiftAlloyIngot).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))).weight(5),
				ItemLootEntry.builder(ECItems.scroll).acceptFunction(RandomSpell.builder()).weight(15), 
				ItemLootEntry.builder(ECItems.emptyReceptacle).weight(2));
		LootPool.Builder pool = LootPool.builder().name("main").rolls(RandomValueRange.of(2, 4));

		entries.forEach(pool::addEntry);
		return pool;
	}

	private static LootPool.Builder genFire() {
		List<LootEntry.Builder<?>> entries = Lists.newArrayList(
				ItemLootEntry.builder(ECItems.inertCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10),
				ItemLootEntry.builder(ECItems.fireCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(40));
		LootPool.Builder pool = genBase();

		entries.forEach(pool::addEntry);
		return pool;
	}

	private static LootPool.Builder genWater() {
		List<LootEntry.Builder<?>> entries = Lists.newArrayList(
				ItemLootEntry.builder(ECItems.inertCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10),
				ItemLootEntry.builder(ECItems.waterCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(40));
		LootPool.Builder pool = genBase();

		entries.forEach(pool::addEntry);
		return pool;
	}

	private static LootPool.Builder genAir() {
		List<LootEntry.Builder<?>> entries = Lists.newArrayList(
				ItemLootEntry.builder(ECItems.inertCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10),
				ItemLootEntry.builder(ECItems.airCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(40));
		LootPool.Builder pool = genBase();

		entries.forEach(pool::addEntry);
		return pool;
	}

	private static LootPool.Builder genEarth() {
		List<LootEntry.Builder<?>> entries = Lists.newArrayList(
				ItemLootEntry.builder(ECItems.inertCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10),
				ItemLootEntry.builder(ECItems.earthCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(40));
		LootPool.Builder pool = genBase();

		entries.forEach(pool::addEntry);
		return pool;
	}

	private void save(DirectoryCache cache, LootPool.Builder pool, ResourceLocation location) throws IOException {
		save(cache, LootTable.builder().addLootPool(pool).setParameterSet(LootParameterSets.BLOCK), getPath(generator.getOutputFolder(), location));
	}

	@Override
	public String getName() {
		return "ElementalCraft inject loot tables";
	}
}
