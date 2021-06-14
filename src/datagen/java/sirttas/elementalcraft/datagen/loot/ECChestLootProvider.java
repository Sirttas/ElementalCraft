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
	public void run(DirectoryCache cache) throws IOException {
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
		return genBase(RandomValueRange.between(0, 2))
				.add(ItemLootEntry.lootTableItem(ECItems.INERT_CRYSTAL).apply(SetCount.setCount(RandomValueRange.between(1, 5))).setWeight(20))
				.add(ItemLootEntry.lootTableItem(ECItems.FIRE_CRYSTAL).apply(SetCount.setCount(RandomValueRange.between(1, 3))).setWeight(10))
				.add(ItemLootEntry.lootTableItem(ECItems.EARTH_CRYSTAL).apply(SetCount.setCount(RandomValueRange.between(1, 3))).setWeight(10))
				.add(ItemLootEntry.lootTableItem(ECItems.WATER_CRYSTAL).apply(SetCount.setCount(RandomValueRange.between(1, 3))).setWeight(10))
				.add(ItemLootEntry.lootTableItem(ECItems.AIR_CRYSTAL).apply(SetCount.setCount(RandomValueRange.between(1, 3))).setWeight(10))
				.add(ItemLootEntry.lootTableItem(ECItems.FIRE_SHARD).apply(SetCount.setCount(RandomValueRange.between(3, 8))).setWeight(5))
				.add(ItemLootEntry.lootTableItem(ECItems.WATER_SHARD).apply(SetCount.setCount(RandomValueRange.between(3, 8))).setWeight(5))
				.add(ItemLootEntry.lootTableItem(ECItems.EARTH_SHARD).apply(SetCount.setCount(RandomValueRange.between(3, 8))).setWeight(5))
				.add(ItemLootEntry.lootTableItem(ECItems.AIR_SHARD).apply(SetCount.setCount(RandomValueRange.between(3, 8))).setWeight(5))
				.add(ItemLootEntry.lootTableItem(ECItems.POWERFUL_FIRE_SHARD).setWeight(2))
				.add(ItemLootEntry.lootTableItem(ECItems.POWERFUL_WATER_SHARD).setWeight(2))
				.add(ItemLootEntry.lootTableItem(ECItems.POWERFUL_EARTH_SHARD).setWeight(2))
				.add(ItemLootEntry.lootTableItem(ECItems.POWERFUL_AIR_SHARD).setWeight(2))
				.add(ItemLootEntry.lootTableItem(ECItems.SCROLL).apply(RandomSpell.builder()).setWeight(15));
	}

	private static LootTable.Builder genSmallAltar(ElementType type) {
		return genWithType(RandomValueRange.between(2, 4), type);
	}

	private static LootTable.Builder genMediumAltar(ElementType type) {
		return addAdvanced(genWithType(RandomValueRange.between(3, 6), type), RandomValueRange.between(1, 3), type);
	}

	private static LootPool.Builder genBase(IRandomRange range) {
		return LootPool.lootPool().name("main").setRolls(range)
				.add(ItemLootEntry.lootTableItem(ECItems.DRENCHED_IRON_INGOT).apply(SetCount.setCount(RandomValueRange.between(1, 3))).setWeight(10))
				.add(ItemLootEntry.lootTableItem(ECItems.DRENCHED_IRON_NUGGET).apply(SetCount.setCount(RandomValueRange.between(2, 5))).setWeight(15))
				.add(ItemLootEntry.lootTableItem(ECItems.SWIFT_ALLOY_INGOT).apply(SetCount.setCount(RandomValueRange.between(1, 2))).setWeight(5))
				.add(ItemLootEntry.lootTableItem(ECItems.SWIFT_ALLOY_NUGGET).apply(SetCount.setCount(RandomValueRange.between(2, 4))).setWeight(7))
				.add(ItemLootEntry.lootTableItem(ECItems.SCROLL_PAPER).apply(SetCount.setCount(RandomValueRange.between(2, 4))).setWeight(8));
	}
	
	private static LootTable.Builder genWithType(IRandomRange range, ElementType type) {
		return addVanilla(LootTable.lootTable().withPool(genBase(range)
				.add(ItemLootEntry.lootTableItem(ECItems.INERT_CRYSTAL).apply(SetCount.setCount(RandomValueRange.between(1, 3))).setWeight(10))
				.add(ItemLootEntry.lootTableItem(getCrystalForType(type)).apply(SetCount.setCount(RandomValueRange.between(1, 6))).setWeight(40))
				.add(ItemLootEntry.lootTableItem(getShardForType(type)).apply(SetCount.setCount(RandomValueRange.between(4, 10))).setWeight(20))
				.add(ItemLootEntry.lootTableItem(getPowerfulShardForType(type)).setWeight(5))
				.add(ItemLootEntry.lootTableItem(ECItems.SCROLL).apply(RandomSpell.builder(type)).setWeight(15))));
	}
	
	private static LootTable.Builder addAdvanced(LootTable.Builder builder, IRandomRange range, ElementType type) {
		return builder.withPool(LootPool.lootPool().name("advanced").setRolls(range)
				.add(ItemLootEntry.lootTableItem(Items.GOLD_INGOT).apply(SetCount.setCount(RandomValueRange.between(1.0F, 4.0F))).setWeight(15))
				.add(ItemLootEntry.lootTableItem(ECItems.SWIFT_ALLOY_INGOT).apply(SetCount.setCount(RandomValueRange.between(1, 3))).setWeight(10))
				.add(ItemLootEntry.lootTableItem(ECItems.SWIFT_ALLOY_NUGGET).apply(SetCount.setCount(RandomValueRange.between(3, 5))).setWeight(15))
				.add(ItemLootEntry.lootTableItem(ECItems.SCROLL).apply(RandomSpell.builder(type)).setWeight(15))
				.add(ItemLootEntry.lootTableItem(ECItems.PURE_CRYSTAL).setWeight(5))
				.add(ItemLootEntry.lootTableItem(ECItems.EMPTY_RECEPTACLE).setWeight(2)));
		
	}
	
	private static LootTable.Builder addVanilla(LootTable.Builder builder) {
		return builder.withPool(LootPool.lootPool().setRolls(RandomValueRange.between(1.0F, 3.0F)).name("vanilla_1")
				.add(ItemLootEntry.lootTableItem(Items.IRON_INGOT).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(1.0F, 4.0F))))
				.add(ItemLootEntry.lootTableItem(Items.GOLD_INGOT).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(1.0F, 4.0F))))
				.add(ItemLootEntry.lootTableItem(Items.BREAD).setWeight(20))
				.add(ItemLootEntry.lootTableItem(Items.WHEAT).setWeight(20).apply(SetCount.setCount(RandomValueRange.between(1.0F, 4.0F))))
				.add(ItemLootEntry.lootTableItem(Items.BUCKET).setWeight(10))
				.add(ItemLootEntry.lootTableItem(Items.REDSTONE).setWeight(15).apply(SetCount.setCount(RandomValueRange.between(1.0F, 4.0F))))
				.add(ItemLootEntry.lootTableItem(Items.COAL).setWeight(15).apply(SetCount.setCount(RandomValueRange.between(1.0F, 4.0F))))
				.add(ItemLootEntry.lootTableItem(Items.MELON_SEEDS).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(2.0F, 4.0F))))
				.add(ItemLootEntry.lootTableItem(Items.PUMPKIN_SEEDS).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(2.0F, 4.0F))))
				.add(ItemLootEntry.lootTableItem(Items.BEETROOT_SEEDS).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(2.0F, 4.0F)))))
		.withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(2)).name("vanilla_2")
				.add(ItemLootEntry.lootTableItem(Items.BONE).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(1.0F, 8.0F))))
				.add(ItemLootEntry.lootTableItem(Items.GUNPOWDER).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(1.0F, 8.0F))))
				.add(ItemLootEntry.lootTableItem(Items.ROTTEN_FLESH).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(1.0F, 8.0F))))
				.add(ItemLootEntry.lootTableItem(Items.STRING).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(1.0F, 8.0F))))
				.add(ItemLootEntry.lootTableItem(Items.ENDER_PEARL).setWeight(2)));
	}


	private void save(DirectoryCache cache, LootPool.Builder pool, ResourceLocation location) throws IOException {
		save(cache, LootTable.lootTable().withPool(pool), location);
	}

	private void save(DirectoryCache cache, LootTable.Builder builder, ResourceLocation location) throws IOException {
		save(cache, builder.setParamSet(LootParameterSets.CHEST), getPath(location));
	}

	private Path getPath(ResourceLocation id) {
		return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/loot_tables/chests/" + id.getPath() + ".json");
	}

	@Override
	public String getName() {
		return "ElementalCraft inject loot tables";
	}
}
