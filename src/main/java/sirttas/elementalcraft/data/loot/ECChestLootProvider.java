package sirttas.elementalcraft.data.loot;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraft.world.storage.loot.IRandomRange;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraft.world.storage.loot.functions.SetCount;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.loot.function.RandomSpell;

/**
 * greatly inspired by Botania
 *
 * 
 */
public class ECChestLootProvider extends AbstractECLootProvider {

	public ECChestLootProvider(DataGenerator generator) {
		super(generator);
		LootFunctionManager.registerFunction(new RandomSpell.Serializer());
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
				ItemLootEntry.builder(ECItems.airCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10));
		LootPool.Builder pool = genBase(RandomValueRange.of(0, 2));

		entries.forEach(pool::addEntry);
		return pool;
	}

	private static LootPool.Builder genBase(IRandomRange range) {
		List<LootEntry.Builder<?>> entries = Lists.newArrayList(
				ItemLootEntry.builder(ECItems.drenchedIronIngot).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10),
				ItemLootEntry.builder(ECItems.drenchedIronNugget).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))).weight(15),
				ItemLootEntry.builder(ECItems.swiftAlloyIngot).acceptFunction(SetCount.builder(RandomValueRange.of(1, 2))).weight(5),
				ItemLootEntry.builder(ECItems.swiftAlloyNugget).acceptFunction(SetCount.builder(RandomValueRange.of(2, 4))).weight(7),
				ItemLootEntry.builder(ECItems.scroll).acceptFunction(RandomSpell.builder()).weight(15), 
				ItemLootEntry.builder(ECItems.emptyReceptacle).weight(2));
		LootPool.Builder pool = LootPool.builder().name("main").rolls(range);

		entries.forEach(pool::addEntry);
		return pool;
	}

	private static LootTable.Builder genFire() {
		List<LootEntry.Builder<?>> entries = Lists.newArrayList(
				ItemLootEntry.builder(ECItems.inertCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10),
				ItemLootEntry.builder(ECItems.fireCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(40));
		LootPool.Builder pool = genBase(RandomValueRange.of(2, 4));

		entries.forEach(pool::addEntry);
		return addVanilla(LootTable.builder().addLootPool(pool));
	}

	private static LootTable.Builder genWater() {
		List<LootEntry.Builder<?>> entries = Lists.newArrayList(
				ItemLootEntry.builder(ECItems.inertCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10),
				ItemLootEntry.builder(ECItems.waterCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(40));
		LootPool.Builder pool = genBase(RandomValueRange.of(2, 4));

		entries.forEach(pool::addEntry);
		return addVanilla(LootTable.builder().addLootPool(pool));
	}

	private static LootTable.Builder genAir() {
		List<LootEntry.Builder<?>> entries = Lists.newArrayList(
				ItemLootEntry.builder(ECItems.inertCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10),
				ItemLootEntry.builder(ECItems.airCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(40));
		LootPool.Builder pool = genBase(RandomValueRange.of(2, 4));

		entries.forEach(pool::addEntry);
		return addVanilla(LootTable.builder().addLootPool(pool));
	}

	private static LootTable.Builder genEarth() {
		List<LootEntry.Builder<?>> entries = Lists.newArrayList(
				ItemLootEntry.builder(ECItems.inertCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10),
				ItemLootEntry.builder(ECItems.earthCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 6))).weight(40));
		LootPool.Builder pool = genBase(RandomValueRange.of(2, 4));

		entries.forEach(pool::addEntry);
		return addVanilla(LootTable.builder().addLootPool(pool));
	}
	
	private static LootTable.Builder addVanilla(LootTable.Builder builder) {
		return builder.addLootPool(LootPool.builder().rolls(RandomValueRange.of(1.0F, 3.0F))
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
		.addLootPool(LootPool.builder().rolls(ConstantRange.of(2))
				.addEntry(ItemLootEntry.builder(Items.BONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 8.0F))))
				.addEntry(ItemLootEntry.builder(Items.GUNPOWDER).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 8.0F))))
				.addEntry(ItemLootEntry.builder(Items.ROTTEN_FLESH).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 8.0F))))
				.addEntry(ItemLootEntry.builder(Items.STRING).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 8.0F)))));
	}


	private void save(DirectoryCache cache, LootPool.Builder pool, ResourceLocation location) throws IOException {
		save(cache, LootTable.builder().addLootPool(pool), location);
	}

	private void save(DirectoryCache cache, LootTable.Builder builder, ResourceLocation location) throws IOException {
		save(cache, builder.setParameterSet(LootParameterSets.CHEST), getPath(generator.getOutputFolder(), location));
	}

	@Override
	public String getName() {
		return "ElementalCraft inject loot tables";
	}
}
