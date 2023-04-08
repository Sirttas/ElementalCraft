package sirttas.elementalcraft.datagen.loot;

import net.minecraft.data.loot.ChestLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.loot.function.RandomSpell;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

/**
 * greatly inspired by Botania
 *
 * 
 */
public class ECChestLoot extends ChestLoot {

	@Override
	public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
		consumer.accept(createPath("inject"), createInject());
		consumer.accept(createPath("altar/small_fire"), createSmallAltar(ElementType.FIRE));
		consumer.accept(createPath("altar/medium_fire"), createMediumAltar(ElementType.FIRE));
		consumer.accept(createPath("altar/small_water"), createSmallAltar(ElementType.WATER));
		consumer.accept(createPath("altar/medium_water"), createMediumAltar(ElementType.WATER));
		consumer.accept(createPath("altar/small_air"), createSmallAltar(ElementType.AIR));
		consumer.accept(createPath("altar/medium_air"), createMediumAltar(ElementType.AIR));
		consumer.accept(createPath("altar/small_earth"), createSmallAltar(ElementType.EARTH));
		consumer.accept(createPath("altar/medium_earth"), createMediumAltar(ElementType.EARTH));
	}

	@Nonnull
	private static ResourceLocation createPath(String inject) {
		return ElementalCraft.createRL("chests/" + inject);
	}

	private static LootTable.Builder createInject() {
		return LootTable.lootTable().withPool(createBase(UniformGenerator.between(0, 2))
				.add(LootItem.lootTableItem(ECItems.INERT_CRYSTAL.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))).setWeight(20))
				.add(LootItem.lootTableItem(ECItems.FIRE_CRYSTAL.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(10))
				.add(LootItem.lootTableItem(ECItems.EARTH_CRYSTAL.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(10))
				.add(LootItem.lootTableItem(ECItems.WATER_CRYSTAL.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(10))
				.add(LootItem.lootTableItem(ECItems.AIR_CRYSTAL.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(10))
				.add(LootItem.lootTableItem(ECItems.FIRE_SHARD.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))).setWeight(5))
				.add(LootItem.lootTableItem(ECItems.WATER_SHARD.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))).setWeight(5))
				.add(LootItem.lootTableItem(ECItems.EARTH_SHARD.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))).setWeight(5))
				.add(LootItem.lootTableItem(ECItems.AIR_SHARD.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))).setWeight(5))
				.add(LootItem.lootTableItem(ECItems.POWERFUL_FIRE_SHARD.get()).setWeight(2))
				.add(LootItem.lootTableItem(ECItems.POWERFUL_WATER_SHARD.get()).setWeight(2))
				.add(LootItem.lootTableItem(ECItems.POWERFUL_EARTH_SHARD.get()).setWeight(2))
				.add(LootItem.lootTableItem(ECItems.POWERFUL_AIR_SHARD.get()).setWeight(2))
				.add(LootItem.lootTableItem(ECItems.SCROLL.get()).apply(RandomSpell.builder()).setWeight(15)));
	}

	private static LootTable.Builder createSmallAltar(ElementType type) {
		return createWithType(UniformGenerator.between(2, 4), type);
	}

	private static LootTable.Builder createMediumAltar(ElementType type) {
		return addAdvanced(createWithType(UniformGenerator.between(3, 6), type), UniformGenerator.between(1, 3), type);
	}

	private static LootPool.Builder createBase(NumberProvider range) {
		return LootPool.lootPool().name("main").setRolls(range)
				.add(LootItem.lootTableItem(ECItems.DRENCHED_IRON_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(10))
				.add(LootItem.lootTableItem(ECItems.DRENCHED_IRON_NUGGET.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))).setWeight(15))
				.add(LootItem.lootTableItem(ECItems.SWIFT_ALLOY_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(5))
				.add(LootItem.lootTableItem(ECItems.SWIFT_ALLOY_NUGGET.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))).setWeight(7))
				.add(LootItem.lootTableItem(ECItems.SCROLL_PAPER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))).setWeight(8));
	}
	
	private static LootTable.Builder createWithType(NumberProvider range, ElementType type) {
		return addVanilla(LootTable.lootTable().withPool(createBase(range)
				.add(LootItem.lootTableItem(ECItems.INERT_CRYSTAL.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(10))
				.add(LootItem.lootTableItem(ECLootTableProvider.getCrystalForType(type)).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(40))
				.add(LootItem.lootTableItem(ECLootTableProvider.getShardForType(type)).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 10))).setWeight(20))
				.add(LootItem.lootTableItem(ECLootTableProvider.getPowerfulShardForType(type)).setWeight(5))
				.add(LootItem.lootTableItem(ECItems.SCROLL.get()).apply(RandomSpell.builder(type)).setWeight(15))));
	}
	
	private static LootTable.Builder addAdvanced(LootTable.Builder builder, NumberProvider range, ElementType type) {
		return builder.withPool(LootPool.lootPool().name("advanced").setRolls(range)
				.add(LootItem.lootTableItem(Items.GOLD_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))).setWeight(15))
				.add(LootItem.lootTableItem(ECItems.SWIFT_ALLOY_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(10))
				.add(LootItem.lootTableItem(ECItems.SWIFT_ALLOY_NUGGET.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5))).setWeight(15))
				.add(LootItem.lootTableItem(ECItems.SCROLL.get()).apply(RandomSpell.builder(type)).setWeight(15))
				.add(LootItem.lootTableItem(ECItems.PURE_CRYSTAL.get()).setWeight(5)));
		
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
}
