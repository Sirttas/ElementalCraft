package sirttas.elementalcraft.datagen.loot;

import java.io.IOException;
import java.nio.file.Path;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.loot.function.ECLootFunctions;
import sirttas.elementalcraft.loot.function.RandomSpell;

import javax.annotation.Nonnull;

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
	public void run(@Nonnull HashCache cache) throws IOException {
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
		return genBase(UniformGenerator.between(0, 2))
				.add(LootItem.lootTableItem(ECItems.INERT_CRYSTAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))).setWeight(20))
				.add(LootItem.lootTableItem(ECItems.FIRE_CRYSTAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(10))
				.add(LootItem.lootTableItem(ECItems.EARTH_CRYSTAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(10))
				.add(LootItem.lootTableItem(ECItems.WATER_CRYSTAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(10))
				.add(LootItem.lootTableItem(ECItems.AIR_CRYSTAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(10))
				.add(LootItem.lootTableItem(ECItems.FIRE_SHARD).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))).setWeight(5))
				.add(LootItem.lootTableItem(ECItems.WATER_SHARD).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))).setWeight(5))
				.add(LootItem.lootTableItem(ECItems.EARTH_SHARD).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))).setWeight(5))
				.add(LootItem.lootTableItem(ECItems.AIR_SHARD).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))).setWeight(5))
				.add(LootItem.lootTableItem(ECItems.POWERFUL_FIRE_SHARD).setWeight(2))
				.add(LootItem.lootTableItem(ECItems.POWERFUL_WATER_SHARD).setWeight(2))
				.add(LootItem.lootTableItem(ECItems.POWERFUL_EARTH_SHARD).setWeight(2))
				.add(LootItem.lootTableItem(ECItems.POWERFUL_AIR_SHARD).setWeight(2))
				.add(LootItem.lootTableItem(ECItems.SCROLL).apply(RandomSpell.builder()).setWeight(15));
	}

	private static LootTable.Builder genSmallAltar(ElementType type) {
		return genWithType(UniformGenerator.between(2, 4), type);
	}

	private static LootTable.Builder genMediumAltar(ElementType type) {
		return addAdvanced(genWithType(UniformGenerator.between(3, 6), type), UniformGenerator.between(1, 3), type);
	}

	private static LootPool.Builder genBase(NumberProvider range) {
		return LootPool.lootPool().name("main").setRolls(range)
				.add(LootItem.lootTableItem(ECItems.DRENCHED_IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(10))
				.add(LootItem.lootTableItem(ECItems.DRENCHED_IRON_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))).setWeight(15))
				.add(LootItem.lootTableItem(ECItems.SWIFT_ALLOY_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(5))
				.add(LootItem.lootTableItem(ECItems.SWIFT_ALLOY_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))).setWeight(7))
				.add(LootItem.lootTableItem(ECItems.SCROLL_PAPER).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))).setWeight(8));
	}
	
	private static LootTable.Builder genWithType(NumberProvider range, ElementType type) {
		return addVanilla(LootTable.lootTable().withPool(genBase(range)
				.add(LootItem.lootTableItem(ECItems.INERT_CRYSTAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(10))
				.add(LootItem.lootTableItem(getCrystalForType(type)).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(40))
				.add(LootItem.lootTableItem(getShardForType(type)).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 10))).setWeight(20))
				.add(LootItem.lootTableItem(getPowerfulShardForType(type)).setWeight(5))
				.add(LootItem.lootTableItem(ECItems.SCROLL).apply(RandomSpell.builder(type)).setWeight(15))));
	}
	
	private static LootTable.Builder addAdvanced(LootTable.Builder builder, NumberProvider range, ElementType type) {
		return builder.withPool(LootPool.lootPool().name("advanced").setRolls(range)
				.add(LootItem.lootTableItem(Items.GOLD_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))).setWeight(15))
				.add(LootItem.lootTableItem(ECItems.SWIFT_ALLOY_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(10))
				.add(LootItem.lootTableItem(ECItems.SWIFT_ALLOY_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5))).setWeight(15))
				.add(LootItem.lootTableItem(ECItems.SCROLL).apply(RandomSpell.builder(type)).setWeight(15))
				.add(LootItem.lootTableItem(ECItems.PURE_CRYSTAL).setWeight(5))
				.add(LootItem.lootTableItem(ECItems.EMPTY_RECEPTACLE).setWeight(2)));
		
	}
	
	private static LootTable.Builder addVanilla(LootTable.Builder builder) {
		return builder.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1.0F, 3.0F)).name("vanilla_1")
				.add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.BREAD).setWeight(20))
				.add(LootItem.lootTableItem(Items.WHEAT).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.BUCKET).setWeight(10))
				.add(LootItem.lootTableItem(Items.REDSTONE).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.COAL).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.MELON_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.BEETROOT_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))))
		.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(2)).name("vanilla_2")
				.add(LootItem.lootTableItem(Items.BONE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
				.add(LootItem.lootTableItem(Items.GUNPOWDER).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
				.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
				.add(LootItem.lootTableItem(Items.STRING).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
				.add(LootItem.lootTableItem(Items.ENDER_PEARL).setWeight(2)));
	}


	private void save(HashCache cache, LootPool.Builder pool, ResourceLocation location) throws IOException {
		save(cache, LootTable.lootTable().withPool(pool), location);
	}

	private void save(HashCache cache, LootTable.Builder builder, ResourceLocation location) throws IOException {
		save(cache, builder.setParamSet(LootContextParamSets.CHEST), getPath(location));
	}

	private Path getPath(ResourceLocation id) {
		return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/loot_tables/chests/" + id.getPath() + ".json");
	}

	@Nonnull
    @Override
	public String getName() {
		return "ElementalCraft inject loot tables";
	}
}
