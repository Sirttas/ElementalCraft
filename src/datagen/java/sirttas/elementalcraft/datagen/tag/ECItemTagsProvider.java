package sirttas.elementalcraft.datagen.tag;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.pipe.PipeUpgradeItem;
import sirttas.elementalcraft.item.spell.AbstractSpellHolderItem;
import sirttas.elementalcraft.tag.ECTags;
import vazkii.botania.api.BotaniaAPI;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public class ECItemTagsProvider extends ItemTagsProvider {

	public static final String POWAH = "powah";
	public static final String BLUE_SKIES = "blue_skies";

	public ECItemTagsProvider(DataGenerator generatorIn, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
		super(generatorIn, blockTagsProvider, ElementalCraftApi.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {

		copy(ECTags.Blocks.STRIPPED_OAK, ECTags.Items.STRIPPED_OAK);
		copy(ECTags.Blocks.STRIPPED_DARK_OAK, ECTags.Items.STRIPPED_DARK_OAK);
		copy(ECTags.Blocks.STRIPPED_BIRCH, ECTags.Items.STRIPPED_BIRCH);
		copy(ECTags.Blocks.STRIPPED_ACACIA, ECTags.Items.STRIPPED_ACACIA);
		copy(ECTags.Blocks.STRIPPED_JUNGLE, ECTags.Items.STRIPPED_JUNGLE);
		copy(ECTags.Blocks.STRIPPED_SPRUCE, ECTags.Items.STRIPPED_SPRUCE);
		copy(ECTags.Blocks.STRIPPED_MANGROVE, ECTags.Items.STRIPPED_MANGROVE);
		copy(ECTags.Blocks.STRIPPED_CRIMSON, ECTags.Items.STRIPPED_CRIMSON);
		copy(ECTags.Blocks.STRIPPED_WARPED, ECTags.Items.STRIPPED_WARPED);

		copy(BlockTags.SLABS, ItemTags.SLABS);
		copy(BlockTags.STAIRS, ItemTags.STAIRS);
		copy(BlockTags.WALLS, ItemTags.WALLS);
		copy(BlockTags.FENCES, ItemTags.FENCES);
		copy(Tags.Blocks.GLASS_PANES, Tags.Items.GLASS_PANES);
		copy(ECTags.Blocks.ORES_INERT_CRYSTAL, ECTags.Items.ORES_INERT_CRYSTAL);
		copy(Tags.Blocks.ORES, Tags.Items.ORES);

		copy(ECTags.Blocks.PUREROCKS, ECTags.Items.PUREROCKS);
		copy(ECTags.Blocks.SHRINES, ECTags.Items.SHRINES);
		copy(ECTags.Blocks.SHRINE_UPGRADES, ECTags.Items.SHRINE_UPGRADES);
		copy(ECTags.Blocks.SMALL_CONTAINER_COMPATIBLES, ECTags.Items.SMALL_CONTAINER_COMPATIBLES);
		copy(ECTags.Blocks.INSTRUMENTS, ECTags.Items.INSTRUMENTS);
		copy(ECTags.Blocks.CONTAINER_TOOLS, ECTags.Items.CONTAINER_TOOLS);

		copy(ECTags.Blocks.STORAGE_BLOCKS_DRENCHED_IRON, ECTags.Items.STORAGE_BLOCKS_DRENCHED_IRON);
		copy(ECTags.Blocks.STORAGE_BLOCKS_SWIFT_ALLOY, ECTags.Items.STORAGE_BLOCKS_SWIFT_ALLOY);
		copy(ECTags.Blocks.STORAGE_BLOCKS_FIREITE, ECTags.Items.STORAGE_BLOCKS_FIREITE);
		copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);

		tag(ECTags.Items.SPELL_CAST_TOOLS).add(ECItems.FOCUS.get(), ECItems.STAFF.get());
		
		tag(ECTags.Items.INFUSABLE_FOCUS).add(ECItems.FOCUS.get());
		tag(ECTags.Items.INFUSABLE_STAVES).add(ECItems.STAFF.get());
		tag(ECTags.Items.INFUSABLE_SWORDS).addTag(Tags.Items.TOOLS_SWORDS);
		tag(ECTags.Items.INFUSABLE_PICKAXES).addTag(Tags.Items.TOOLS_PICKAXES);
		tag(ECTags.Items.INFUSABLE_SHOVELS).addTag(Tags.Items.TOOLS_SHOVELS);
		tag(ECTags.Items.INFUSABLE_HOES).addTag(Tags.Items.TOOLS_HOES);
		tag(ECTags.Items.INFUSABLE_AXES).addTag(Tags.Items.TOOLS_AXES);
		tag(ECTags.Items.INFUSABLE_SHILDS).addTags(Tags.Items.TOOLS_SHIELDS);
		tag(ECTags.Items.INFUSABLE_BOWS).addTag(Tags.Items.TOOLS_BOWS);
		tag(ECTags.Items.INFUSABLE_CROSSBOWS).addTag(Tags.Items.TOOLS_CROSSBOWS);
		tag(ECTags.Items.INFUSABLE_FISHING_RODS).add(Items.FISHING_ROD);
		tag(ECTags.Items.INFUSABLE_TRIDENTS).add(Items.TRIDENT);

		tag(ECTags.Items.INFUSABLE_HELMETS).addTag(Tags.Items.ARMORS_HELMETS);
		tag(ECTags.Items.INFUSABLE_CHESTPLATES).addTag(Tags.Items.ARMORS_CHESTPLATES);
		tag(ECTags.Items.INFUSABLE_LEGGINGS).addTag(Tags.Items.ARMORS_LEGGINGS);
		tag(ECTags.Items.INFUSABLE_BOOTS).addTag(Tags.Items.ARMORS_BOOTS);

		tag(ECTags.Items.SPELL_HOLDERS).add(getItems(AbstractSpellHolderItem.class));
		tag(ECTags.Items.ELEMENTAL_CRYSTALS).add(ECItems.FIRE_CRYSTAL.get(), ECItems.WATER_CRYSTAL.get(), ECItems.EARTH_CRYSTAL.get(), ECItems.AIR_CRYSTAL.get());
		tag(ECTags.Items.CRYSTALS).add(ECItems.INERT_CRYSTAL.get(), ECItems.CONTAINED_CRYSTAL.get(), ECItems.PURE_CRYSTAL.get()).addTag(ECTags.Items.ELEMENTAL_CRYSTALS);
		tag(ECTags.Items.LENSES).add(ECItems.FIRE_LENS.get(), ECItems.WATER_LENS.get(), ECItems.EARTH_LENS.get(), ECItems.AIR_LENS.get());
		
		tag(ECTags.Items.DEFAULT_SHARDS).add(ECItems.FIRE_SHARD.get(), ECItems.WATER_SHARD.get(), ECItems.EARTH_SHARD.get(), ECItems.AIR_SHARD.get());
		tag(ECTags.Items.POWERFUL_SHARDS).add(ECItems.POWERFUL_FIRE_SHARD.get(), ECItems.POWERFUL_WATER_SHARD.get(), ECItems.POWERFUL_EARTH_SHARD.get(), ECItems.POWERFUL_AIR_SHARD.get());
		tag(ECTags.Items.FIRE_SHARDS).add(ECItems.FIRE_SHARD.get(), ECItems.POWERFUL_FIRE_SHARD.get());
		tag(ECTags.Items.WATER_SHARDS).add(ECItems.WATER_SHARD.get(), ECItems.POWERFUL_WATER_SHARD.get());
		tag(ECTags.Items.EARTH_SHARDS).add(ECItems.EARTH_SHARD.get(), ECItems.POWERFUL_EARTH_SHARD.get());
		tag(ECTags.Items.AIR_SHARDS).add(ECItems.AIR_SHARD.get(), ECItems.POWERFUL_AIR_SHARD.get());
		tag(ECTags.Items.SHARDS).addTag(ECTags.Items.DEFAULT_SHARDS).addTag(ECTags.Items.POWERFUL_SHARDS);

		tag(ECTags.Items.INGOTS_DRENCHED_IRON).add(ECItems.DRENCHED_IRON_INGOT.get());
		tag(ECTags.Items.INGOTS_SWIFT_ALLOY).add(ECItems.SWIFT_ALLOY_INGOT.get());
		tag(ECTags.Items.INGOTS_FIREITE).add(ECItems.FIREITE_INGOT.get());
		tag(Tags.Items.INGOTS).addTags(ECTags.Items.INGOTS_DRENCHED_IRON, ECTags.Items.INGOTS_SWIFT_ALLOY, ECTags.Items.INGOTS_FIREITE);

		tag(ECTags.Items.NUGGETS_DRENCHED_IRON).add(ECItems.DRENCHED_IRON_NUGGET.get());
		tag(ECTags.Items.NUGGETS_SWIFT_ALLOY).add(ECItems.SWIFT_ALLOY_NUGGET.get());
		tag(ECTags.Items.NUGGETS_FIREITE).add(ECItems.FIREITE_NUGGET.get());
		tag(Tags.Items.NUGGETS).addTags(ECTags.Items.NUGGETS_DRENCHED_IRON, ECTags.Items.NUGGETS_SWIFT_ALLOY, ECTags.Items.NUGGETS_FIREITE);

		tag(ECTags.Items.PRISTINE_FIRE_GEMS).add(ECItems.PRISTINE_FIRE_GEM.get());
		tag(ECTags.Items.FINE_FIRE_GEMS).add(ECItems.FINE_FIRE_GEM.get(), ECItems.PRISTINE_FIRE_GEM.get());
		tag(ECTags.Items.CRUDE_FIRE_GEMS).add(ECItems.CRUDE_FIRE_GEM.get(), ECItems.FINE_FIRE_GEM.get(), ECItems.PRISTINE_FIRE_GEM.get());
		tag(ECTags.Items.INPUT_FIRE_GEMS).add(ECItems.CRUDE_FIRE_GEM.get(), ECItems.FINE_FIRE_GEM.get(), ECItems.PRISTINE_FIRE_GEM.get()).addTag(Tags.Items.GEMS_DIAMOND);
		tag(ECTags.Items.PRISTINE_WATER_GEMS).add(ECItems.PRISTINE_WATER_GEM.get());
		tag(ECTags.Items.FINE_WATER_GEMS).add(ECItems.FINE_WATER_GEM.get(), ECItems.PRISTINE_WATER_GEM.get());
		tag(ECTags.Items.CRUDE_WATER_GEMS).add(ECItems.CRUDE_WATER_GEM.get(), ECItems.FINE_WATER_GEM.get(), ECItems.PRISTINE_WATER_GEM.get());
		tag(ECTags.Items.INPUT_WATER_GEMS).add(ECItems.CRUDE_WATER_GEM.get(), ECItems.FINE_WATER_GEM.get(), ECItems.PRISTINE_WATER_GEM.get()).addTag(Tags.Items.GEMS_DIAMOND);
		tag(ECTags.Items.PRISTINE_EARTH_GEMS).add(ECItems.PRISTINE_EARTH_GEM.get());
		tag(ECTags.Items.FINE_EARTH_GEMS).add(ECItems.FINE_EARTH_GEM.get(), ECItems.PRISTINE_EARTH_GEM.get());
		tag(ECTags.Items.CRUDE_EARTH_GEMS).add(ECItems.CRUDE_EARTH_GEM.get(), ECItems.FINE_EARTH_GEM.get(), ECItems.PRISTINE_EARTH_GEM.get());
		tag(ECTags.Items.INPUT_EARTH_GEMS).add(ECItems.CRUDE_EARTH_GEM.get(), ECItems.FINE_EARTH_GEM.get(), ECItems.PRISTINE_EARTH_GEM.get()).addTag(Tags.Items.GEMS_DIAMOND);
		tag(ECTags.Items.PRISTINE_AIR_GEMS).add(ECItems.PRISTINE_AIR_GEM.get());
		tag(ECTags.Items.FINE_AIR_GEMS).add(ECItems.FINE_AIR_GEM.get(), ECItems.PRISTINE_AIR_GEM.get());
		tag(ECTags.Items.CRUDE_AIR_GEMS).add(ECItems.CRUDE_AIR_GEM.get(), ECItems.FINE_AIR_GEM.get(), ECItems.PRISTINE_AIR_GEM.get());
		tag(ECTags.Items.INPUT_AIR_GEMS).add(ECItems.CRUDE_AIR_GEM.get(), ECItems.FINE_AIR_GEM.get(), ECItems.PRISTINE_AIR_GEM.get()).addTag(Tags.Items.GEMS_DIAMOND);
		tag(ECTags.Items.INPUT_GEMS).addTags(ECTags.Items.INPUT_FIRE_GEMS, ECTags.Items.INPUT_WATER_GEMS, ECTags.Items.INPUT_EARTH_GEMS, ECTags.Items.INPUT_AIR_GEMS);
		tag(Tags.Items.GEMS).addTags(ECTags.Items.INPUT_GEMS);

		tag(ECTags.Items.HARDENED_RODS).add(ECItems.HARDENED_HANDLE.get());
		tag(Tags.Items.RODS).addTag(ECTags.Items.HARDENED_RODS);

		tag(ECTags.Items.STORAGE_BLOCKS_RAW_MATERIALS)
				.addTags(Tags.Items.STORAGE_BLOCKS_RAW_COPPER, Tags.Items.STORAGE_BLOCKS_RAW_IRON, Tags.Items.STORAGE_BLOCKS_RAW_GOLD)
				.addOptionalTag(forge("storage_blocks/raw_silver"))
				.addOptionalTag(forge("storage_blocks/raw_lead"))
				.addOptionalTag(forge("storage_blocks/raw_tin"))
				.addOptionalTag(forge("storage_blocks/raw_zinc"))
				.addOptionalTag(forge("storage_blocks/raw_aluminum"))
				.addOptionalTag(forge("storage_blocks/raw_nickel"))
				.addOptionalTag(forge("storage_blocks/raw_uranium"))
				.addOptionalTag(forge("storage_blocks/raw_osmium"))
				.addOptionalTag(forge("storage_blocks/raw_desh"))
				.addOptionalTag(forge("storage_blocks/raw_calorite"))
				.addOptionalTag(forge("storage_blocks/raw_ostrum"))
				.addOptionalTag(forge("storage_blocks/raw_platinum"))
				.addOptionalTag(forge("storage_blocks/raw_iesnium"))
				.addOptionalTag(forge("storage_blocks/raw_unobtainium"))
				.addOptionalTag(forge("storage_blocks/raw_crimson_iron"))
				.addOptionalTag(forge("storage_blocks/raw_allthemodium"))
				.addOptionalTag(forge("storage_blocks/raw_vibranium"))
				.addOptionalTag(forge("storage_blocks/raw_iridium"))
				.addOptionalTag(forge("storage_blocks/raw_azure_silver"));

		tag(ECTags.Items.RUNE_SLATES).add(ECItems.MINOR_RUNE_SLATE.get(), ECItems.RUNE_SLATE.get(), ECItems.MAJOR_RUNE_SLATE.get());

		addPipeTags();

		tag(ECTags.Items.STAFF_CRAFT_SWORD).add(Items.DIAMOND_SWORD, Items.NETHERITE_SWORD);

		addPureOreTags();

		tag(ECTags.Items.GROVE_SHRINE_FLOWERS).addTag(ItemTags.FLOWERS);
		tag(ECTags.Items.GROVE_SHRINE_BLACKLIST).addOptionalTag(new ResourceLocation(BotaniaAPI.MODID, "double_mystical_flowers")).addOptionalTag(new ResourceLocation(BotaniaAPI.MODID, "mystical_flowers")).addOptionalTag(new ResourceLocation(BotaniaAPI.MODID, "special_flowers")).addOptionalTag(new ResourceLocation(BotaniaAPI.MODID, "floating_flowers"));
		tag(ECTags.Items.MYSTICAL_GROVE_FLOWERS).addOptionalTag(new ResourceLocation(BotaniaAPI.MODID, "double_mystical_flowers")).addOptionalTag(new ResourceLocation(BotaniaAPI.MODID, "mystical_flowers"));

		tag(ECTags.Items.WHITE_FLOWERS).add(Items.LILY_OF_THE_VALLEY);
		tag(ECTags.Items.ORANGE_FLOWERS).add(Items.ORANGE_TULIP);
		tag(ECTags.Items.MAGENTA_FLOWERS).add(Items.LILAC);
		tag(ECTags.Items.LIGHT_BLUE_FLOWERS).add(Items.BLUE_ORCHID);
		tag(ECTags.Items.YELLOW_FLOWERS).add(Items.DANDELION, Items.SUNFLOWER);
		tag(ECTags.Items.LIME_FLOWERS);
		tag(ECTags.Items.PINK_FLOWERS).add(Items.PEONY, Items.PINK_TULIP);
		tag(ECTags.Items.GRAY_FLOWERS);
		tag(ECTags.Items.LIGHT_GRAY_FLOWERS).add(Items.AZURE_BLUET, Items.OXEYE_DAISY, Items.WHITE_TULIP);
		tag(ECTags.Items.CYAN_FLOWERS);
		tag(ECTags.Items.PURPLE_FLOWERS);
		tag(ECTags.Items.BLUE_FLOWERS).add(Items.CORNFLOWER);
		tag(ECTags.Items.BROWN_FLOWERS);
		tag(ECTags.Items.GREEN_FLOWERS);
		tag(ECTags.Items.BLACK_FLOWERS).add(Items.WITHER_ROSE);
		tag(ECTags.Items.RED_FLOWERS).add(Items.POPPY, Items.ROSE_BUSH, Items.RED_TULIP);

		tag(ItemTags.BEACON_PAYMENT_ITEMS).add(ECItems.DRENCHED_IRON_INGOT.get(), ECItems.SWIFT_ALLOY_INGOT.get(), ECItems.FIREITE_INGOT.get());

		tag(ECTags.Items.JEWEL_SOCKETABLES).addTags(Tags.Items.TOOLS, Tags.Items.ARMORS, ECTags.Items.SPELL_CAST_TOOLS).add(Items.ELYTRA);

		tag(ECTags.Items.CURIOS_ELEMENT_HOLDER).add(ECItems.FIRE_HOLDER.get(), ECItems.WATER_HOLDER.get(), ECItems.EARTH_HOLDER.get(), ECItems.AIR_HOLDER.get(), ECItems.PURE_HOLDER.get());
	}

	private void addPipeTags() {
		copy(ECTags.Blocks.PIPES, ECTags.Items.PIPES);
		tag(ECTags.Items.PIPES_UPGRADES).add(getItems(PipeUpgradeItem.class));
		tag(ECTags.Items.PIPE_COVER_HIDING)
				.addTags(ECTags.Items.PIPES, ECTags.Items.PIPES_UPGRADES)
				.add(ECItems.COVER_FRAME.get());
	}

	private void addPureOreTags() {
		tag(ECTags.Items.PURE_SOURCE_ORES_ORES).addTag(Tags.Items.ORES);
		tag(ECTags.Items.PURE_ORES_SOURCE_RAW_MATERIALS).addTag(Tags.Items.RAW_MATERIALS);
		tag(ECTags.Items.PURE_ORES_SOURCE_RAW_MATERIAL_BLOCKS).addTag(ECTags.Items.STORAGE_BLOCKS_RAW_MATERIALS);
		tag(ECTags.Items.PURE_ORES_SOURCE_GEORE_SHARDS).addOptionalTag(forge("geore_shards"));
		tag(ECTags.Items.PURE_ORES_SOURCE_GEORE_BLOCKS).addOptionalTag(forge("geore_blocks"));

		tag(ECTags.Items.PURE_ORES_SOURCE_RESONANT_ORE).addOptionalTag(new ResourceLocation("deepresonance", "resonant_ore"));

		tag(ECTags.Items.PURE_ORES_SOURCE_RAW_URANINITE).addOptional(new ResourceLocation(POWAH, "uraninite_raw"));
		tag(ECTags.Items.PURE_ORES_SOURCE_POOR_URANINITE)
				.addOptional(new ResourceLocation(POWAH, "uraninite_ore_poor"))
				.addOptional(new ResourceLocation(POWAH, "deepslate_uraninite_ore_poor"));
		tag(ECTags.Items.PURE_ORES_SOURCE_URANINITE)
				.addOptional(new ResourceLocation(POWAH, "uraninite_ore"))
				.addOptional(new ResourceLocation(POWAH, "deepslate_uraninite_ore"));
		tag(ECTags.Items.PURE_ORES_SOURCE_DENSE_URANINITE)
				.addOptional(new ResourceLocation(POWAH, "uraninite_ore_dense"))
				.addOptional(new ResourceLocation(POWAH, "deepslate_uraninite_ore_dense"));

		tag(ECTags.Items.PURE_ORES_SPECIFICS).addTags(
				ECTags.Items.PURE_ORES_SOURCE_RESONANT_ORE,
				ECTags.Items.PURE_ORES_SOURCE_RAW_URANINITE,
				ECTags.Items.PURE_ORES_SOURCE_POOR_URANINITE,
				ECTags.Items.PURE_ORES_SOURCE_URANINITE,
				ECTags.Items.PURE_ORES_SOURCE_DENSE_URANINITE
		);
		tag(ECTags.Items.PURE_ORES_MOD_PROCESSING_BLACKLIST);
	}

	private ResourceLocation forge(String name) {
		return new ResourceLocation(ECNames.FORGE, name);
	}

	protected <T> Item[] getItems(List<String> modIds, Class<T> clazz, Predicate<T> filter) {
		return registry.stream()
				.filter(i -> modIds.contains(ForgeRegistries.ITEMS.getKey(i).getNamespace()) && clazz.isInstance(i))
				.map(clazz::cast)
				.filter(filter)
				.map(Item.class::cast)
				.sorted(Comparator.comparing(ForgeRegistries.ITEMS::getKey))
				.toArray(Item[]::new);
	}

	protected <T> Item[] getItems(String modId, Class<T> clazz, Predicate<T> filter) {
		return getItems(Lists.newArrayList(modId), clazz, filter);
	}

	protected <T> Item[] getItems(String modId, Class<T> clazz) {
		return getItems(modId, clazz, Predicates.alwaysTrue());
	}

	protected <T> Item[] getItems(Class<T> clazz) {
		return getItems(ElementalCraftApi.MODID, clazz);
	}
}
