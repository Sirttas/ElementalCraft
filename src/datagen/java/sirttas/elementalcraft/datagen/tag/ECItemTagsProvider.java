package sirttas.elementalcraft.datagen.tag;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.spell.AbstractSpellHolderItem;
import sirttas.elementalcraft.tag.ECTags;

public class ECItemTagsProvider extends ItemTagsProvider {

	private static final String BOTANIA = "botania";
	private static final String MEKANISM_TOOLS = "mekanismtools";
	
	public ECItemTagsProvider(DataGenerator generatorIn, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
		super(generatorIn, blockTagsProvider, ElementalCraftApi.MODID, existingFileHelper);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags() {
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

		tag(ECTags.Items.FORGE_SWORDS);
		tag(ECTags.Items.FORGE_PICKAXES);
		tag(ECTags.Items.FORGE_AXES);
		tag(ECTags.Items.FORGE_SHOVELS);
		tag(ECTags.Items.FORGE_HOES);
		tag(ECTags.Items.FORGE_SHILDS);
		tag(ECTags.Items.FORGE_BOWS);
		tag(ECTags.Items.FORGE_CROSSBOWS);
		tag(ECTags.Items.FORGE_HELMETS);
		tag(ECTags.Items.FORGE_CHESTPLATES);
		tag(ECTags.Items.FORGE_LEGGINGS);
		tag(ECTags.Items.FORGE_BOOTS);

		tag(ECTags.Items.IMPROVED_RECEPTACLES).add(ECItems.EMPTY_RECEPTACLE_IMPROVED, ECItems.RECEPTACLE_IMPROVED);

		tag(ECTags.Items.SPELL_CAST_TOOLS).add(ECItems.FOCUS, ECItems.STAFF);
		
		tag(ECTags.Items.INFUSABLE_FOCUS).add(ECItems.FOCUS);
		tag(ECTags.Items.INFUSABLE_STAVES).add(ECItems.STAFF);
		addOptionals(tag(ECTags.Items.INFUSABLE_SWORDS).add(Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD).addTag(ECTags.Items.FORGE_SWORDS),
				getItems(MEKANISM_TOOLS, SwordItem.class));
		addOptionals(tag(ECTags.Items.INFUSABLE_PICKAXES).add(Items.IRON_PICKAXE, Items.GOLDEN_PICKAXE, Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE)
				.addTag(ECTags.Items.FORGE_PICKAXES), getItems(MEKANISM_TOOLS, PickaxeItem.class));
		addOptionals(tag(ECTags.Items.INFUSABLE_SHOVELS).add(Items.IRON_SHOVEL, Items.GOLDEN_SHOVEL, Items.DIAMOND_SHOVEL, Items.NETHERITE_SHOVEL)
				.addTag(ECTags.Items.FORGE_SHOVELS), getItems(MEKANISM_TOOLS, ShovelItem.class));
		addOptionals(tag(ECTags.Items.INFUSABLE_HOES).add(Items.IRON_HOE, Items.GOLDEN_HOE, Items.DIAMOND_HOE, Items.NETHERITE_HOE)
				.addTag(ECTags.Items.FORGE_HOES), getItems(MEKANISM_TOOLS, HoeItem.class));
		addOptionals(tag(ECTags.Items.INFUSABLE_AXES).add(Items.IRON_AXE, Items.GOLDEN_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE).addTag(ECTags.Items.FORGE_AXES),
				getItems(MEKANISM_TOOLS, AxeItem.class));
		tag(ECTags.Items.INFUSABLE_SHILDS);
		addOptionals(tag(ECTags.Items.INFUSABLE_BOWS).add(Items.BOW).addTag(ECTags.Items.FORGE_BOWS), getItems(MEKANISM_TOOLS, BowItem.class));
		addOptionals(tag(ECTags.Items.INFUSABLE_CROSSBOWS).add(Items.CROSSBOW).addTag(ECTags.Items.FORGE_CROSSBOWS), getItems(MEKANISM_TOOLS, CrossbowItem.class));
		tag(ECTags.Items.INFUSABLE_FISHING_RODS).add(Items.FISHING_ROD);
		tag(ECTags.Items.INFUSABLE_TRIDENTS).add(Items.TRIDENT);

		addOptionals(tag(ECTags.Items.INFUSABLE_HELMETS).add(Items.IRON_HELMET, Items.GOLDEN_HELMET, Items.DIAMOND_HELMET, Items.NETHERITE_HELMET).addTag(ECTags.Items.FORGE_HELMETS),
				getItems(MEKANISM_TOOLS, ArmorItem.class, item -> item.getSlot() == EquipmentSlot.HEAD));
		addOptionals(tag(ECTags.Items.INFUSABLE_CHESTPLATES).add(Items.IRON_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.NETHERITE_CHESTPLATE)
				.addTag(ECTags.Items.FORGE_CHESTPLATES), getItems(MEKANISM_TOOLS, ArmorItem.class, item -> item.getSlot() == EquipmentSlot.CHEST));
		addOptionals(tag(ECTags.Items.INFUSABLE_LEGGINGS).add(Items.IRON_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.DIAMOND_LEGGINGS, Items.NETHERITE_LEGGINGS)
				.addTag(ECTags.Items.FORGE_LEGGINGS), getItems(MEKANISM_TOOLS, ArmorItem.class, item -> item.getSlot() == EquipmentSlot.LEGS));
		addOptionals(tag(ECTags.Items.INFUSABLE_BOOTS).add(Items.IRON_BOOTS, Items.GOLDEN_BOOTS, Items.DIAMOND_BOOTS, Items.NETHERITE_BOOTS).addTag(ECTags.Items.FORGE_BOOTS),
				getItems(MEKANISM_TOOLS, ArmorItem.class, item -> item.getSlot() == EquipmentSlot.FEET));

		tag(ECTags.Items.SPELL_HOLDERS).add(getItems(AbstractSpellHolderItem.class));
		tag(ECTags.Items.ELEMENTAL_CRYSTALS).add(ECItems.FIRE_CRYSTAL, ECItems.WATER_CRYSTAL, ECItems.EARTH_CRYSTAL, ECItems.AIR_CRYSTAL);
		tag(ECTags.Items.CRYSTALS).add(ECItems.INERT_CRYSTAL, ECItems.CONTAINED_CRYSTAL, ECItems.PURE_CRYSTAL).addTag(ECTags.Items.ELEMENTAL_CRYSTALS);
		tag(ECTags.Items.LENSES).add(ECItems.FIRE_LENSE, ECItems.WATER_LENSE, ECItems.EARTH_LENSE, ECItems.AIR_LENSE);
		
		tag(ECTags.Items.DEFAULT_SHARDS).add(ECItems.FIRE_SHARD, ECItems.WATER_SHARD, ECItems.EARTH_SHARD, ECItems.AIR_SHARD);
		tag(ECTags.Items.POWERFUL_SHARDS).add(ECItems.POWERFUL_FIRE_SHARD, ECItems.POWERFUL_WATER_SHARD, ECItems.POWERFUL_EARTH_SHARD, ECItems.POWERFUL_AIR_SHARD);
		tag(ECTags.Items.FIRE_SHARDS).add(ECItems.FIRE_SHARD, ECItems.POWERFUL_FIRE_SHARD);
		tag(ECTags.Items.WATER_SHARDS).add(ECItems.WATER_SHARD, ECItems.POWERFUL_WATER_SHARD);
		tag(ECTags.Items.EARTH_SHARDS).add(ECItems.EARTH_SHARD, ECItems.POWERFUL_EARTH_SHARD);
		tag(ECTags.Items.AIR_SHARDS).add(ECItems.AIR_SHARD, ECItems.POWERFUL_AIR_SHARD);
		tag(ECTags.Items.SHARDS).addTag(ECTags.Items.DEFAULT_SHARDS).addTag(ECTags.Items.POWERFUL_SHARDS);

		tag(ECTags.Items.INGOTS_DRENCHED_IRON).add(ECItems.DRENCHED_IRON_INGOT);
		tag(ECTags.Items.INGOTS_SWIFT_ALLOY).add(ECItems.SWIFT_ALLOY_INGOT);
		tag(ECTags.Items.INGOTS_FIREITE).add(ECItems.FIREITE_INGOT);
		tag(Tags.Items.INGOTS).addTags(ECTags.Items.INGOTS_DRENCHED_IRON, ECTags.Items.INGOTS_SWIFT_ALLOY, ECTags.Items.INGOTS_FIREITE);

		tag(ECTags.Items.NUGGETS_DRENCHED_IRON).add(ECItems.DRENCHED_IRON_NUGGET);
		tag(ECTags.Items.NUGGETS_SWIFT_ALLOY).add(ECItems.SWIFT_ALLOY_NUGGET);
		tag(ECTags.Items.NUGGETS_FIREITE).add(ECItems.FIREITE_NUGGET);
		tag(Tags.Items.NUGGETS).addTags(ECTags.Items.NUGGETS_DRENCHED_IRON, ECTags.Items.NUGGETS_SWIFT_ALLOY, ECTags.Items.NUGGETS_FIREITE);

		tag(ECTags.Items.PRISTINE_FIRE_GEMS).add(ECItems.PRISTINE_FIRE_GEM);
		tag(ECTags.Items.FINE_FIRE_GEMS).add(ECItems.FINE_FIRE_GEM, ECItems.PRISTINE_FIRE_GEM);
		tag(ECTags.Items.CRUDE_FIRE_GEMS).add(ECItems.CRUDE_FIRE_GEM, ECItems.FINE_FIRE_GEM, ECItems.PRISTINE_FIRE_GEM);
		tag(ECTags.Items.INPUT_FIRE_GEMS).add(ECItems.CRUDE_FIRE_GEM, ECItems.FINE_FIRE_GEM, ECItems.PRISTINE_FIRE_GEM).addTag(Tags.Items.GEMS_DIAMOND);
		tag(ECTags.Items.PRISTINE_WATER_GEMS).add(ECItems.PRISTINE_WATER_GEM);
		tag(ECTags.Items.FINE_WATER_GEMS).add(ECItems.FINE_WATER_GEM, ECItems.PRISTINE_WATER_GEM);
		tag(ECTags.Items.CRUDE_WATER_GEMS).add(ECItems.CRUDE_WATER_GEM, ECItems.FINE_WATER_GEM, ECItems.PRISTINE_WATER_GEM);
		tag(ECTags.Items.INPUT_WATER_GEMS).add(ECItems.CRUDE_WATER_GEM, ECItems.FINE_WATER_GEM, ECItems.PRISTINE_WATER_GEM).addTag(Tags.Items.GEMS_DIAMOND);
		tag(ECTags.Items.PRISTINE_EARTH_GEMS).add(ECItems.PRISTINE_EARTH_GEM);
		tag(ECTags.Items.FINE_EARTH_GEMS).add(ECItems.FINE_EARTH_GEM, ECItems.PRISTINE_EARTH_GEM);
		tag(ECTags.Items.CRUDE_EARTH_GEMS).add(ECItems.CRUDE_EARTH_GEM, ECItems.FINE_EARTH_GEM, ECItems.PRISTINE_EARTH_GEM);
		tag(ECTags.Items.INPUT_EARTH_GEMS).add(ECItems.CRUDE_EARTH_GEM, ECItems.FINE_EARTH_GEM, ECItems.PRISTINE_EARTH_GEM).addTag(Tags.Items.GEMS_DIAMOND);
		tag(ECTags.Items.PRISTINE_AIR_GEMS).add(ECItems.PRISTINE_AIR_GEM);
		tag(ECTags.Items.FINE_AIR_GEMS).add(ECItems.FINE_AIR_GEM, ECItems.PRISTINE_AIR_GEM);
		tag(ECTags.Items.CRUDE_AIR_GEMS).add(ECItems.CRUDE_AIR_GEM, ECItems.FINE_AIR_GEM, ECItems.PRISTINE_AIR_GEM);
		tag(ECTags.Items.INPUT_AIR_GEMS).add(ECItems.CRUDE_AIR_GEM, ECItems.FINE_AIR_GEM, ECItems.PRISTINE_AIR_GEM).addTag(Tags.Items.GEMS_DIAMOND);
		tag(ECTags.Items.INPUT_GEMS).addTags(ECTags.Items.INPUT_FIRE_GEMS, ECTags.Items.INPUT_WATER_GEMS, ECTags.Items.INPUT_EARTH_GEMS, ECTags.Items.INPUT_AIR_GEMS);
		tag(Tags.Items.GEMS).addTags(ECTags.Items.INPUT_GEMS);

		tag(ECTags.Items.RUNE_SLATES).add(ECItems.MINOR_RUNE_SLATE, ECItems.RUNE_SLATE, ECItems.MAJOR_RUNE_SLATE);
		
		tag(ECTags.Items.PIPE_COVER_HIDING).addTag(ECTags.Items.PIPES).add(ECItems.COVER_FRAM, ECItems.PIPE_PRIORITY);
		
		tag(ECTags.Items.STAFF_CRAFT_SWORD).add(Items.DIAMOND_SWORD, Items.NETHERITE_SWORD);
		
		tag(ECTags.Items.PURE_ORES).addTag(Tags.Items.ORES);
		tag(ECTags.Items.PURE_ORES_MOD_PROCESSING_BLACKLIST).addTags(Tags.Items.ORES_DIAMOND, Tags.Items.ORES_EMERALD);
		
		tag(ECTags.Items.GROVE_SHRINE_FLOWERS).addTag(ItemTags.FLOWERS);
		tag(ECTags.Items.GROVE_SHRINE_BLACKLIST).addOptionalTag(new ResourceLocation(BOTANIA, "double_mystical_flowers"))
				.addOptionalTag(new ResourceLocation(BOTANIA, "mystical_flowers")).addOptionalTag(new ResourceLocation(BOTANIA, "special_flowers"))
				.addOptionalTag(new ResourceLocation(BOTANIA, "floating_flowers"));
		tag(ECTags.Items.MYSTICAL_GROVE_FLOWERS).addOptionalTag(new ResourceLocation(BOTANIA, "double_mystical_flowers"))
				.addOptionalTag(new ResourceLocation(BOTANIA, "mystical_flowers"));
	}

	public TagsProvider.TagAppender<Item> addOptionals(TagsProvider.TagAppender<Item> builder, Item... optionals) {
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
		return getItems(ElementalCraftApi.MODID, clazz);
	}
}
