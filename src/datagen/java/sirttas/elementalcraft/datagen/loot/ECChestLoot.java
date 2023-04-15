package sirttas.elementalcraft.datagen.loot;

import net.minecraft.data.loot.ChestLoot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.elemental.ElementalItemHelper;
import sirttas.elementalcraft.loot.function.RandomSpell;
import sirttas.elementalcraft.nbt.NBTHelper;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

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
		return createWithType(UniformGenerator.between(2, 5), type);
	}

	private static LootTable.Builder createMediumAltar(ElementType type) {
		return createWithType(UniformGenerator.between(3, 7), type).withPool(createAdvancedPool(type));
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
				.add(LootItem.lootTableItem(ElementalItemHelper.getCrystalForType(type)).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(40))
				.add(LootItem.lootTableItem(ElementalItemHelper.getShardForType(type)).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 10))).setWeight(20))
				.add(LootItem.lootTableItem(ElementalItemHelper.getPowerfulShardForType(type)).setWeight(5))
				.add(randomSpell(type).setWeight(15))
				.add(rune(getSmallRuneName(type)).setWeight(10))
				.add(rune(getMediumRuneName(type)).setWeight(5))));
	}

	@Nonnull
	private static LootPool.Builder createAdvancedPool(ElementType type) {
		return LootPool.lootPool().name("advanced").setRolls(UniformGenerator.between(1, 3))
				.add(LootItem.lootTableItem(Items.GOLD_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))).setWeight(15))
				.add(LootItem.lootTableItem(ECItems.SWIFT_ALLOY_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(10))
				.add(LootItem.lootTableItem(ECItems.SWIFT_ALLOY_NUGGET.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5))).setWeight(15))
				.add(randomSpell(type).setWeight(15))
				.add(rune(getMediumRuneName(type)).setWeight(10))
				.add(LootItem.lootTableItem(ECItems.PURE_CRYSTAL.get()).setWeight(2));
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

	@Nonnull
	private static LootPoolSingletonContainer.Builder<?> randomSpell(ElementType type) {
		return LootItem.lootTableItem(ECItems.SCROLL.get()).apply(RandomSpell.builder(type));
	}

	private static LootPoolSingletonContainer.Builder<?> rune(String runeName) {
		var tag = new CompoundTag();

		NBTHelper.getOrCreate(tag, ECNames.EC_NBT).putString(ECNames.RUNE, ElementalCraft.createRL(runeName).toString());
		return LootItem.lootTableItem(ECItems.RUNE.get()).apply(SetNbtFunction.setTag(tag));
	}

	private static String getSmallRuneName(ElementType type) {
		return switch (type) {
			case AIR -> "wii";
			case EARTH -> "soaryn";
			case FIRE -> "manx";
			case WATER -> "claptrap";
			default -> throw new IllegalArgumentException("Unknown element type: " + type);
		};
	}

	private static String getMediumRuneName(ElementType type) {
		return switch (type) {
			case AIR -> "fus";
			case EARTH -> "kaworu";
			case FIRE -> "jita";
			case WATER -> "bombadil";
			default -> throw new IllegalArgumentException("Unknown element type: " + type);
		};
	}
}
