package sirttas.elementalcraft.datagen.tag;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.data.tags.VanillaItemTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.jewel.JewelItem;
import sirttas.elementalcraft.item.pipe.PipeUpgradeItem;
import sirttas.elementalcraft.item.spell.AbstractSpellHolderItem;
import sirttas.elementalcraft.tag.ECTags;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public class ECItemTagsProvider extends ItemTagsProvider {

	public static final String POWAH = "powah";
	public static final String BOTANIA = "bortania";
	public static final String BLUE_SKIES = "blue_skies";

	public ECItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, ElementalCraftApi.MODID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		(new ECBlockItemTagsProvider() {
			@Override
			protected TagAppender<Block, Block> tag(TagKey<Block> blockTag, TagKey<Item> itemTag) {
				return new VanillaItemTagsProvider.BlockToItemConverter(ECItemTagsProvider.this.tag(itemTag));
			}
		}).run();

		tag(ECTags.Items.SPELL_CAST_TOOLS).add(ECItems.FOCUS.get(), ECItems.STAFF.get());

		tag(ECTags.Items.INFUSABLE_FOCUS).add(ECItems.FOCUS.get());
		tag(ECTags.Items.INFUSABLE_STAVES).add(ECItems.STAFF.get());
		tag(ECTags.Items.INFUSABLE_SWORDS).addTag(ItemTags.SWORDS);
		tag(ECTags.Items.INFUSABLE_PICKAXES).addTag(ItemTags.PICKAXES);
		tag(ECTags.Items.INFUSABLE_SHOVELS).addTag(ItemTags.SHOVELS);
		tag(ECTags.Items.INFUSABLE_HOES).addTag(ItemTags.HOES);
		tag(ECTags.Items.INFUSABLE_AXES).addTag(ItemTags.AXES);
		tag(ECTags.Items.TOOLS_PAXELS);
		tag(ECTags.Items.TOOLS_AIOTS);
		tag(ECTags.Items.INFUSABLE_PAXELS).addTags(ECTags.Items.TOOLS_PAXELS, ECTags.Items.TOOLS_AIOTS);
		tag(ECTags.Items.INFUSABLE_SHILDS).addTags(Tags.Items.TOOLS_SHIELD);
		tag(ECTags.Items.INFUSABLE_BOWS).addTag(Tags.Items.TOOLS_BOW);
		tag(ECTags.Items.INFUSABLE_CROSSBOWS).addTag(Tags.Items.TOOLS_CROSSBOW);
		tag(ECTags.Items.INFUSABLE_FISHING_RODS).add(Items.FISHING_ROD);
		tag(ECTags.Items.INFUSABLE_TRIDENTS).add(Items.TRIDENT);
		tag(ECTags.Items.INFUSABLE_MACES).addTag(Tags.Items.TOOLS_MACE);
		tag(ECTags.Items.INFUSABLE_SPEARS).addTag(ItemTags.SPEARS);

		tag(ECTags.Items.INFUSABLE_HELMETS).addTag(ItemTags.HEAD_ARMOR);
		tag(ECTags.Items.INFUSABLE_CHESTPLATES).addTag(ItemTags.CHEST_ARMOR);
		tag(ECTags.Items.INFUSABLE_LEGGINGS).addTag(ItemTags.LEG_ARMOR);
		tag(ECTags.Items.INFUSABLE_BOOTS).addTag(ItemTags.FOOT_ARMOR);

		tag(ECTags.Items.CHISELS).add(ECItems.DRENCHED_IRON_CHISEL.get(), ECItems.SWIFT_ALLOY_CHISEL.get(), ECItems.FIREITE_CHISEL.get());
		tag(ECTags.Items.SPELL_HOLDERS).add(getItems(AbstractSpellHolderItem.class));
		tag(ECTags.Items.ELEMENTAL_CRYSTALS).add(ECItems.FIRE_CRYSTAL.get(), ECItems.WATER_CRYSTAL.get(), ECItems.EARTH_CRYSTAL.get(), ECItems.AIR_CRYSTAL.get());
		tag(ECTags.Items.CRYSTALS).add(ECItems.INERT_CRYSTAL.get(), ECItems.CONTAINED_CRYSTAL.get(), ECItems.PURE_CRYSTAL.get()).addTag(ECTags.Items.ELEMENTAL_CRYSTALS);
		tag(ECTags.Items.LENSES).add(ECItems.FIRE_LENS.get());

		tag(ECTags.Items.EMPTY_RECEPTACLES).add(ECItems.EMPTY_RECEPTACLE.get());
		tag(ECTags.Items.RECEPTACLES).addTags(ECTags.Items.EMPTY_RECEPTACLES, ECTags.Items.FULL_RECEPTACLES);

		tag(ECTags.Items.INGOTS_DRENCHED_IRON).add(ECItems.DRENCHED_IRON_INGOT.get());
		tag(ECTags.Items.INGOTS_SWIFT_ALLOY).add(ECItems.SWIFT_ALLOY_INGOT.get());
		tag(ECTags.Items.INGOTS_FIREITE).add(ECItems.FIREITE_INGOT.get());
		tag(Tags.Items.INGOTS).addTags(ECTags.Items.INGOTS_DRENCHED_IRON, ECTags.Items.INGOTS_SWIFT_ALLOY, ECTags.Items.INGOTS_FIREITE);

		tag(ECTags.Items.NUGGETS_DRENCHED_IRON).add(ECItems.DRENCHED_IRON_NUGGET.get());
		tag(ECTags.Items.NUGGETS_SWIFT_ALLOY).add(ECItems.SWIFT_ALLOY_NUGGET.get());
		tag(ECTags.Items.NUGGETS_FIREITE).add(ECItems.FIREITE_NUGGET.get());
		tag(Tags.Items.NUGGETS).addTags(ECTags.Items.NUGGETS_DRENCHED_IRON, ECTags.Items.NUGGETS_SWIFT_ALLOY, ECTags.Items.NUGGETS_FIREITE);

		tag(Tags.Items.GEMS).add(
				ECItems.CRUDE_FIRE_GEM.get(), ECItems.FINE_FIRE_GEM.get(), ECItems.PRISTINE_FIRE_GEM.get(),
				ECItems.CRUDE_WATER_GEM.get(), ECItems.FINE_WATER_GEM.get(), ECItems.PRISTINE_WATER_GEM.get(),
				ECItems.CRUDE_EARTH_GEM.get(), ECItems.FINE_EARTH_GEM.get(), ECItems.PRISTINE_EARTH_GEM.get(),
				ECItems.CRUDE_AIR_GEM.get(), ECItems.FINE_AIR_GEM.get(), ECItems.PRISTINE_AIR_GEM.get()
		);

		tag(ECTags.Items.HARDENED_RODS).add(ECItems.HARDENED_HANDLE.get());
		tag(Tags.Items.RODS).addTag(ECTags.Items.HARDENED_RODS);

		tag(ECTags.Items.STORAGE_BLOCKS_RAW_MATERIALS)
				.addTags(Tags.Items.STORAGE_BLOCKS_RAW_COPPER, Tags.Items.STORAGE_BLOCKS_RAW_IRON, Tags.Items.STORAGE_BLOCKS_RAW_GOLD);

		getOrCreateRawBuilder(ECTags.Items.STORAGE_BLOCKS_RAW_MATERIALS)
				.addOptionalTag(common("storage_blocks/raw_silver"))
				.addOptionalTag(common("storage_blocks/raw_lead"))
				.addOptionalTag(common("storage_blocks/raw_tin"))
				.addOptionalTag(common("storage_blocks/raw_zinc"))
				.addOptionalTag(common("storage_blocks/raw_aluminum"))
				.addOptionalTag(common("storage_blocks/raw_nickel"))
				.addOptionalTag(common("storage_blocks/raw_uranium"))
				.addOptionalTag(common("storage_blocks/raw_osmium"))
				.addOptionalTag(common("storage_blocks/raw_desh"))
				.addOptionalTag(common("storage_blocks/raw_calorite"))
				.addOptionalTag(common("storage_blocks/raw_ostrum"))
				.addOptionalTag(common("storage_blocks/raw_platinum"))
				.addOptionalTag(common("storage_blocks/raw_iesnium"))
				.addOptionalTag(common("storage_blocks/raw_unobtainium"))
				.addOptionalTag(common("storage_blocks/raw_crimson_iron"))
				.addOptionalTag(common("storage_blocks/raw_allthemodium"))
				.addOptionalTag(common("storage_blocks/raw_vibranium"))
				.addOptionalTag(common("storage_blocks/raw_iridium"))
				.addOptionalTag(common("storage_blocks/raw_azure_silver"))
				.addOptionalTag(Identifier.fromNamespaceAndPath(BLUE_SKIES, "storage_blocks/raw_aquite"))
				.addOptionalTag(Identifier.fromNamespaceAndPath(BLUE_SKIES, "storage_blocks/raw_charoite"))
				.addOptionalTag(Identifier.fromNamespaceAndPath(BLUE_SKIES, "storage_blocks/raw_falsite"))
				.addOptionalTag(Identifier.fromNamespaceAndPath(BLUE_SKIES, "storage_blocks/raw_ventium"))
				.addOptionalTag(Identifier.fromNamespaceAndPath(BLUE_SKIES, "storage_blocks/raw_horizonite"));

		tag(ECTags.Items.RUNE_SLATES).add(ECItems.MINOR_RUNE_SLATE.get(), ECItems.RUNE_SLATE.get(), ECItems.MAJOR_RUNE_SLATE.get());

		addPipeTags();

		tag(ECTags.Items.ENCHANTMENT_HOLDER).add(Items.BOOK, Items.ENCHANTED_BOOK);
		tag(ECTags.Items.STAFF_CRAFT_SWORD).add(Items.DIAMOND_SWORD, Items.NETHERITE_SWORD);

		addPureOreTags();

		tag(ECTags.Items.GROVE_SHRINE_FLOWERS).addTag(ItemTags.FLOWERS);
		getOrCreateRawBuilder(ECTags.Items.MYSTICAL_GROVE_FLOWERS)
				.addOptionalTag(Identifier.fromNamespaceAndPath(BOTANIA, "double_mystical_flowers"))
				.addOptionalTag(Identifier.fromNamespaceAndPath(BOTANIA, "mystical_flowers"));
		tag(ECTags.Items.GROVE_SHRINE_BLACKLIST)
				.add(Items.CHORUS_FLOWER, Items.WITHER_ROSE, Items.SPORE_BLOSSOM)
				.addTag(ECTags.Items.MYSTICAL_GROVE_FLOWERS);
		getOrCreateRawBuilder(ECTags.Items.GROVE_SHRINE_BLACKLIST)
				.addOptionalTag(Identifier.fromNamespaceAndPath(BOTANIA, "special_flowers"))
				.addOptionalTag(Identifier.fromNamespaceAndPath(BOTANIA, "floating_flowers"));

		tag(ECTags.Items.WHITE_FLOWERS).add(Items.LILY_OF_THE_VALLEY);
		tag(ECTags.Items.ORANGE_FLOWERS).add(Items.ORANGE_TULIP, Items.TORCHFLOWER, Items.OPEN_EYEBLOSSOM);
		tag(ECTags.Items.MAGENTA_FLOWERS).add(Items.LILAC);
		tag(ECTags.Items.LIGHT_BLUE_FLOWERS).add(Items.BLUE_ORCHID);
		tag(ECTags.Items.YELLOW_FLOWERS).add(Items.DANDELION, Items.SUNFLOWER, Items.WILDFLOWERS, Items.GOLDEN_DANDELION);
		tag(ECTags.Items.LIME_FLOWERS);
		tag(ECTags.Items.PINK_FLOWERS).add(Items.PEONY, Items.PINK_TULIP, Items.PINK_PETALS, Items.CACTUS_FLOWER);
		tag(ECTags.Items.GRAY_FLOWERS).add(Items.CLOSED_EYEBLOSSOM);
		tag(ECTags.Items.LIGHT_GRAY_FLOWERS).add(Items.AZURE_BLUET, Items.OXEYE_DAISY, Items.WHITE_TULIP);
		tag(ECTags.Items.CYAN_FLOWERS).add(Items.PITCHER_PLANT);
		tag(ECTags.Items.PURPLE_FLOWERS);
		tag(ECTags.Items.BLUE_FLOWERS).add(Items.CORNFLOWER);
		tag(ECTags.Items.BROWN_FLOWERS);
		tag(ECTags.Items.GREEN_FLOWERS);
		tag(ECTags.Items.BLACK_FLOWERS).add(Items.WITHER_ROSE);
		tag(ECTags.Items.RED_FLOWERS).add(Items.POPPY, Items.ROSE_BUSH, Items.RED_TULIP);

		tag(ItemTags.BEACON_PAYMENT_ITEMS).add(ECItems.DRENCHED_IRON_INGOT.get(), ECItems.SWIFT_ALLOY_INGOT.get(), ECItems.FIREITE_INGOT.get());
		tag(ItemTags.BOOKSHELF_BOOKS).add(ECItems.SPELL_BOOK.get());

		tag(ECTags.Items.JEWELS).add(getItems(JewelItem.class));
		tag(ECTags.Items.JEWEL_SOCKETABLES).addTags(Tags.Items.TOOLS, Tags.Items.ARMORS, ECTags.Items.SPELL_CAST_TOOLS).add(Items.ELYTRA);

		tag(ECTags.Items.SOURCE_SEEDS).add(ECItems.FIRE_SOURCE_SEED.get(), ECItems.WATER_SOURCE_SEED.get(), ECItems.EARTH_SOURCE_SEED.get(), ECItems.AIR_SOURCE_SEED.get());

		tag(ECTags.Items.CURIOS_ELEMENT_HOLDER).add(ECItems.FIRE_HOLDER.get(), ECItems.WATER_HOLDER.get(), ECItems.EARTH_HOLDER.get(), ECItems.AIR_HOLDER.get(), ECItems.PURE_HOLDER.get());

		tag(ItemTags.TRIM_MATERIALS).add(ECItems.DRENCHED_IRON_INGOT.get(), ECItems.SWIFT_ALLOY_INGOT.get(), ECItems.FIREITE_INGOT.get(), ECItems.SPRINGALINE_SHARD.get());
	}

	private void addPipeTags() {
		tag(ECTags.Items.PIPES_UPGRADES).add(getItems(PipeUpgradeItem.class));
		tag(ECTags.Items.COVER_HIDING)
				.addTags(ECTags.Items.PIPES, ECTags.Items.PIPES_UPGRADES)
				.add(ECBlocks.RETRIEVER.get().asItem(), ECBlocks.ORDERED_SORTER.get().asItem())
				.add(ECItems.COVER_FRAME.get());
	}

	private void addPureOreTags() {
		tag(ECTags.Items.PURE_ORES_SOURCES_ORES).addTag(Tags.Items.ORES);
		tag(ECTags.Items.PURE_ORES_SOURCES_RAW_MATERIALS).addTag(Tags.Items.RAW_MATERIALS);
		tag(ECTags.Items.PURE_ORES_SOURCES_RAW_MATERIAL_BLOCKS).addTag(ECTags.Items.STORAGE_BLOCKS_RAW_MATERIALS);
		tag(ECTags.Items.PURE_ORES_SOURCES_RESIN_BLOCKS).addTag(Tags.Items.STORAGE_BLOCKS_RESIN);
		tag(ECTags.Items.PURE_ORES_SOURCES_CLUSTERS).addTag(Tags.Items.CLUSTERS);
		tag(ECTags.Items.PURE_ORES_SOURCES_CLUMPS).addTag(Tags.Items.CLUMPS);

		getOrCreateRawBuilder(ECTags.Items.PURE_ORES_SOURCES_GEORE_SHARDS).addOptionalTag(common("geore_shards"));
		getOrCreateRawBuilder(ECTags.Items.PURE_ORES_SOURCES_GEORE_BLOCKS).addOptionalTag(common("geore_blocks"));

		getOrCreateRawBuilder(ECTags.Items.PURE_ORES_SOURCES_RESONANT_ORE).addOptionalTag(Identifier.fromNamespaceAndPath("deepresonance", "resonant_ore"));

		getOrCreateRawBuilder(ECTags.Items.PURE_ORES_SOURCES_RAW_URANINITE).addOptionalElement(Identifier.fromNamespaceAndPath(POWAH, "uraninite_raw"));
		getOrCreateRawBuilder(ECTags.Items.PURE_ORES_SOURCES_POOR_URANINITE)
				.addOptionalElement(Identifier.fromNamespaceAndPath(POWAH, "uraninite_ore_poor"))
				.addOptionalElement(Identifier.fromNamespaceAndPath(POWAH, "deepslate_uraninite_ore_poor"));
		getOrCreateRawBuilder(ECTags.Items.PURE_ORES_SOURCES_URANINITE)
				.addOptionalElement(Identifier.fromNamespaceAndPath(POWAH, "uraninite_ore"))
				.addOptionalElement(Identifier.fromNamespaceAndPath(POWAH, "deepslate_uraninite_ore"));
		getOrCreateRawBuilder(ECTags.Items.PURE_ORES_SOURCES_DENSE_URANINITE)
				.addOptionalElement(Identifier.fromNamespaceAndPath(POWAH, "uraninite_ore_dense"))
				.addOptionalElement(Identifier.fromNamespaceAndPath(POWAH, "deepslate_uraninite_ore_dense"));

		tag(ECTags.Items.PURE_ORES_SPECIFICS).addTags(
				ECTags.Items.PURE_ORES_SOURCES_RESONANT_ORE,
				ECTags.Items.PURE_ORES_SOURCES_RAW_URANINITE,
				ECTags.Items.PURE_ORES_SOURCES_POOR_URANINITE,
				ECTags.Items.PURE_ORES_SOURCES_URANINITE,
				ECTags.Items.PURE_ORES_SOURCES_DENSE_URANINITE
		);
		getOrCreateRawBuilder(ECTags.Items.PURE_ORES_SPECIFICS).addOptionalTag(common("ores/pendorite"));

		tag(ECTags.Items.PURE_ORES_US_FOR_COOKING_RECIPES).addTags(
				ECTags.Items.PURE_ORES_SOURCES_ORES,
				ECTags.Items.PURE_ORES_SOURCES_CLUMPS,
				ECTags.Items.PURE_ORES_SPECIFICS
		);

		tag(ECTags.Items.PURE_ORES_SOURCES).addTags(
				ECTags.Items.PURE_ORES_SOURCES_ORES,
				ECTags.Items.PURE_ORES_SOURCES_RAW_MATERIALS,
				ECTags.Items.PURE_ORES_SOURCES_RAW_MATERIAL_BLOCKS,
				ECTags.Items.PURE_ORES_SOURCES_CLUSTERS,
				ECTags.Items.PURE_ORES_SOURCES_CLUMPS,
				ECTags.Items.PURE_ORES_SOURCES_RESIN_BLOCKS,
				ECTags.Items.PURE_ORES_SOURCES_GEORE_SHARDS,
				ECTags.Items.PURE_ORES_SOURCES_GEORE_BLOCKS,
				ECTags.Items.PURE_ORES_SOURCES_RESONANT_ORE,
				ECTags.Items.PURE_ORES_SOURCES_RAW_URANINITE,
				ECTags.Items.PURE_ORES_SOURCES_POOR_URANINITE,
				ECTags.Items.PURE_ORES_SOURCES_URANINITE,
				ECTags.Items.PURE_ORES_SOURCES_DENSE_URANINITE
		);
	}

	private Identifier common(String name) {
		return Identifier.fromNamespaceAndPath(ECNames.COMMON_TAGS_NAMESPACE, name);
	}

	protected <T> Item[] getItems(List<String> modIds, Class<T> clazz, Predicate<T> filter) {
		return BuiltInRegistries.ITEM.entrySet().stream()
				.filter(e -> modIds.contains(e.getKey().identifier().getNamespace()) && clazz.isInstance(e.getValue()))
				.sorted(Map.Entry.comparingByKey())
				.map(e -> clazz.cast(e.getValue()))
				.filter(filter)
				.map(Item.class::cast)
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