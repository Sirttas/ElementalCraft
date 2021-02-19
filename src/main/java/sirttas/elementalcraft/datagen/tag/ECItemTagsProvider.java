package sirttas.elementalcraft.datagen.tag;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

import mekanism.tools.common.MekanismTools;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.data.TagsProvider;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.spell.AbstractItemSpellHolder;
import sirttas.elementalcraft.tag.ECTags;

public class ECItemTagsProvider extends ItemTagsProvider {

	public ECItemTagsProvider(DataGenerator generatorIn, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
		super(generatorIn, blockTagsProvider, ElementalCraft.MODID, existingFileHelper);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void registerTags() {
		copy(BlockTags.SLABS, ItemTags.SLABS);
		copy(BlockTags.STAIRS, ItemTags.STAIRS);
		copy(BlockTags.WALLS, ItemTags.WALLS);
		copy(BlockTags.FENCES, ItemTags.FENCES);
		copy(Tags.Blocks.GLASS_PANES, Tags.Items.GLASS_PANES);
		copy(Tags.Blocks.ORES, Tags.Items.ORES);

		copy(ECTags.Blocks.PUREROCKS, ECTags.Items.PUREROCKS);
		copy(ECTags.Blocks.PIPES, ECTags.Items.PIPES);
		copy(ECTags.Blocks.SHRINES, ECTags.Items.SHRINES);

		copy(ECTags.Blocks.STORAGE_BLOCKS_DRENCHED_IRON, ECTags.Items.STORAGE_BLOCKS_DRENCHED_IRON);
		copy(ECTags.Blocks.STORAGE_BLOCKS_SWIFT_ALLOY, ECTags.Items.STORAGE_BLOCKS_SWIFT_ALLOY);
		copy(ECTags.Blocks.STORAGE_BLOCKS_FIREITE, ECTags.Items.STORAGE_BLOCKS_FIREITE);
		copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);

		getOrCreateBuilder(ECTags.Items.FORGE_SWORDS);
		getOrCreateBuilder(ECTags.Items.FORGE_PICKAXES);
		getOrCreateBuilder(ECTags.Items.FORGE_AXES);
		getOrCreateBuilder(ECTags.Items.FORGE_SHOVELS);
		getOrCreateBuilder(ECTags.Items.FORGE_HOES);
		getOrCreateBuilder(ECTags.Items.FORGE_SHILDS);
		getOrCreateBuilder(ECTags.Items.FORGE_BOWS);
		getOrCreateBuilder(ECTags.Items.FORGE_CROSSBOWS);
		getOrCreateBuilder(ECTags.Items.FORGE_HELMETS);
		getOrCreateBuilder(ECTags.Items.FORGE_CHESTPLATES);
		getOrCreateBuilder(ECTags.Items.FORGE_LEGGINGS);
		getOrCreateBuilder(ECTags.Items.FORGE_BOOTS);

		addOptionals(getOrCreateBuilder(ECTags.Items.INFUSABLE_SWORDS).add(Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD).addTag(ECTags.Items.FORGE_SWORDS),
				getItems(MekanismTools.MODID, SwordItem.class));
		addOptionals(getOrCreateBuilder(ECTags.Items.INFUSABLE_PICKAXES).add(Items.IRON_PICKAXE, Items.GOLDEN_PICKAXE, Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE)
				.addTag(ECTags.Items.FORGE_PICKAXES), getItems(MekanismTools.MODID, PickaxeItem.class));
		addOptionals(getOrCreateBuilder(ECTags.Items.INFUSABLE_AXES).add(Items.IRON_AXE, Items.GOLDEN_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE).addTag(ECTags.Items.FORGE_AXES),
				getItems(MekanismTools.MODID, AxeItem.class));
		getOrCreateBuilder(ECTags.Items.INFUSABLE_SHOVELS);
		getOrCreateBuilder(ECTags.Items.INFUSABLE_HOES);
		getOrCreateBuilder(ECTags.Items.INFUSABLE_SHILDS);
		addOptionals(getOrCreateBuilder(ECTags.Items.INFUSABLE_BOWS).add(Items.BOW).addTag(ECTags.Items.FORGE_BOWS), getItems(MekanismTools.MODID, BowItem.class));
		addOptionals(getOrCreateBuilder(ECTags.Items.INFUSABLE_CROSSBOWS).add(Items.CROSSBOW).addTag(ECTags.Items.FORGE_CROSSBOWS), getItems(MekanismTools.MODID, CrossbowItem.class));

		addOptionals(getOrCreateBuilder(ECTags.Items.INFUSABLE_HELMETS).add(Items.IRON_HELMET, Items.GOLDEN_HELMET, Items.DIAMOND_HELMET, Items.NETHERITE_HELMET).addTag(ECTags.Items.FORGE_HELMETS),
				getItems(MekanismTools.MODID, ArmorItem.class, item -> item.getEquipmentSlot() == EquipmentSlotType.HEAD));
		addOptionals(getOrCreateBuilder(ECTags.Items.INFUSABLE_CHESTPLATES).add(Items.IRON_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.NETHERITE_CHESTPLATE)
				.addTag(ECTags.Items.FORGE_CHESTPLATES), getItems(MekanismTools.MODID, ArmorItem.class, item -> item.getEquipmentSlot() == EquipmentSlotType.CHEST));
		addOptionals(getOrCreateBuilder(ECTags.Items.INFUSABLE_LEGGINGS).add(Items.IRON_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.DIAMOND_LEGGINGS, Items.NETHERITE_LEGGINGS)
				.addTag(ECTags.Items.FORGE_LEGGINGS), getItems(MekanismTools.MODID, ArmorItem.class, item -> item.getEquipmentSlot() == EquipmentSlotType.LEGS));
		addOptionals(getOrCreateBuilder(ECTags.Items.INFUSABLE_BOOTS).add(Items.IRON_BOOTS, Items.GOLDEN_BOOTS, Items.DIAMOND_BOOTS, Items.NETHERITE_BOOTS).addTag(ECTags.Items.FORGE_BOOTS),
				getItems(MekanismTools.MODID, ArmorItem.class, item -> item.getEquipmentSlot() == EquipmentSlotType.FEET));

		getOrCreateBuilder(ECTags.Items.SPELL_HOLDERS).add(getItems(AbstractItemSpellHolder.class));
		getOrCreateBuilder(ECTags.Items.ELEMENTAL_CRYSTALS).add(ECItems.fireCrystal, ECItems.waterCrystal, ECItems.earthCrystal, ECItems.airCrystal);
		getOrCreateBuilder(ECTags.Items.CRYSTALS).add(ECItems.inertCrystal, ECItems.containedCrystal, ECItems.pureCrystal).addTag(ECTags.Items.ELEMENTAL_CRYSTALS);
		
		getOrCreateBuilder(ECTags.Items.DEFAULT_SHARDS).add(ECItems.fireShard, ECItems.waterShard, ECItems.earthShard, ECItems.airShard);
		getOrCreateBuilder(ECTags.Items.POWERFUL_SHARDS).add(ECItems.powerfulFireShard, ECItems.powerfulWaterShard, ECItems.powerfulEarthShard, ECItems.powerfulAirShard);
		getOrCreateBuilder(ECTags.Items.FIRE_SHARDS).add(ECItems.fireShard, ECItems.powerfulFireShard);
		getOrCreateBuilder(ECTags.Items.WATER_SHARDS).add(ECItems.waterShard, ECItems.powerfulWaterShard);
		getOrCreateBuilder(ECTags.Items.EARTH_SHARDS).add(ECItems.earthShard, ECItems.powerfulEarthShard);
		getOrCreateBuilder(ECTags.Items.AIR_SHARDS).add(ECItems.airShard, ECItems.powerfulAirShard);
		getOrCreateBuilder(ECTags.Items.SHARDS).addTag(ECTags.Items.DEFAULT_SHARDS).addTag(ECTags.Items.POWERFUL_SHARDS);

		getOrCreateBuilder(ECTags.Items.INGOTS_DRENCHED_IRON).add(ECItems.drenchedIronIngot);
		getOrCreateBuilder(ECTags.Items.INGOTS_SWIFT_ALLOY).add(ECItems.swiftAlloyIngot);
		getOrCreateBuilder(ECTags.Items.INGOTS_FIREITE).add(ECItems.fireiteIngot);
		getOrCreateBuilder(Tags.Items.INGOTS).addTags(ECTags.Items.INGOTS_DRENCHED_IRON, ECTags.Items.INGOTS_SWIFT_ALLOY, ECTags.Items.INGOTS_FIREITE);

		getOrCreateBuilder(ECTags.Items.NUGGETS_DRENCHED_IRON).add(ECItems.drenchedIronNugget);
		getOrCreateBuilder(ECTags.Items.NUGGETS_SWIFT_ALLOY).add(ECItems.swiftAlloyNugget);
		getOrCreateBuilder(ECTags.Items.NUGGETS_FIREITE).add(ECItems.fireiteNugget);
		getOrCreateBuilder(Tags.Items.NUGGETS).addTags(ECTags.Items.NUGGETS_DRENCHED_IRON, ECTags.Items.NUGGETS_SWIFT_ALLOY, ECTags.Items.NUGGETS_FIREITE);

		getOrCreateBuilder(Tags.Items.GEMS_DIAMOND);
		getOrCreateBuilder(ECTags.Items.PRISTINE_FIRE_GEMS).add(ECItems.pristineFireGem);
		getOrCreateBuilder(ECTags.Items.FINE_FIRE_GEMS).add(ECItems.fineFireGem, ECItems.pristineFireGem);
		getOrCreateBuilder(ECTags.Items.CRUDE_FIRE_GEMS).add(ECItems.crudeFireGem, ECItems.fineFireGem, ECItems.pristineFireGem);
		getOrCreateBuilder(ECTags.Items.INPUT_FIRE_GEMS).add(ECItems.crudeFireGem, ECItems.fineFireGem, ECItems.pristineFireGem).addTag(Tags.Items.GEMS_DIAMOND);
		getOrCreateBuilder(ECTags.Items.PRISTINE_WATER_GEMS).add(ECItems.pristineWaterGem);
		getOrCreateBuilder(ECTags.Items.FINE_WATER_GEMS).add(ECItems.fineWaterGem, ECItems.pristineWaterGem);
		getOrCreateBuilder(ECTags.Items.CRUDE_WATER_GEMS).add(ECItems.crudeWaterGem, ECItems.fineWaterGem, ECItems.pristineWaterGem);
		getOrCreateBuilder(ECTags.Items.INPUT_WATER_GEMS).add(ECItems.crudeWaterGem, ECItems.fineWaterGem, ECItems.pristineWaterGem).addTag(Tags.Items.GEMS_DIAMOND);
		getOrCreateBuilder(ECTags.Items.PRISTINE_EARTH_GEMS).add(ECItems.pristineEarthGem);
		getOrCreateBuilder(ECTags.Items.FINE_EARTH_GEMS).add(ECItems.fineEarthGem, ECItems.pristineEarthGem);
		getOrCreateBuilder(ECTags.Items.CRUDE_EARTH_GEMS).add(ECItems.crudeEarthGem, ECItems.fineEarthGem, ECItems.pristineEarthGem);
		getOrCreateBuilder(ECTags.Items.INPUT_EARTH_GEMS).add(ECItems.crudeEarthGem, ECItems.fineEarthGem, ECItems.pristineEarthGem).addTag(Tags.Items.GEMS_DIAMOND);
		getOrCreateBuilder(ECTags.Items.PRISTINE_AIR_GEMS).add(ECItems.pristineAirGem);
		getOrCreateBuilder(ECTags.Items.FINE_AIR_GEMS).add(ECItems.fineAirGem, ECItems.pristineAirGem);
		getOrCreateBuilder(ECTags.Items.CRUDE_AIR_GEMS).add(ECItems.crudeAirGem, ECItems.fineAirGem, ECItems.pristineAirGem);
		getOrCreateBuilder(ECTags.Items.INPUT_AIR_GEMS).add(ECItems.crudeAirGem, ECItems.fineAirGem, ECItems.pristineAirGem).addTag(Tags.Items.GEMS_DIAMOND);
		getOrCreateBuilder(ECTags.Items.INPUT_GEMS).addTags(ECTags.Items.INPUT_FIRE_GEMS, ECTags.Items.INPUT_WATER_GEMS, ECTags.Items.INPUT_EARTH_GEMS, ECTags.Items.INPUT_AIR_GEMS);
		getOrCreateBuilder(Tags.Items.GEMS).addTags(ECTags.Items.INPUT_GEMS);

		getOrCreateBuilder(ECTags.Items.RUNE_SLATES).add(ECItems.minorRuneSlate, ECItems.runeSlate, ECItems.majorRuneSlate);
		
		getOrCreateBuilder(ECTags.Items.PURE_ORES).addTag(Tags.Items.ORES);
		getOrCreateBuilder(ECTags.Items.PURE_ORES_MOD_PROCESSING_BLACKLIST).addTags(Tags.Items.ORES_DIAMOND, Tags.Items.ORES_EMERALD);
	}

	public TagsProvider.Builder<Item> addOptionals(TagsProvider.Builder<Item> builder, Item... optionals) {
		Stream.of(optionals).map(Item::getRegistryName).forEach(builder::addOptional);
		return builder;
	}

	protected <T extends Item> Item[] getItems(List<String> modIds, Class<T> clazz, Predicate<T> filter) {
		return registry.stream().filter(i -> modIds.contains(i.getRegistryName().getNamespace()) && clazz.isInstance(i)).map(clazz::cast).filter(filter)
				.sorted(Comparator.comparing(Item::getRegistryName)).toArray(Item[]::new);
	}

	protected <T extends Item> Item[] getItems(String modId, Class<T> clazz, Predicate<T> filter) {
		return getItems(Lists.newArrayList(modId), clazz, filter);
	}

	protected Item[] getItems(List<String> modIds, Class<? extends Item> clazz) {
		return getItems(modIds, clazz, Predicates.alwaysTrue());
	}

	protected Item[] getItems(String modId, Class<? extends Item> clazz) {
		return getItems(modId, clazz, Predicates.alwaysTrue());
	}

	protected Item[] getItems(Class<? extends Item> clazz) {
		return getItems(ElementalCraft.MODID, clazz);
	}
}
