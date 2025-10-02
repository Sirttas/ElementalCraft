package sirttas.elementalcraft.tag;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.interaction.curios.CuriosConstants;

import java.util.function.Predicate;

public class ECTags {

	private ECTags() {
	}

	public static class Items {
		public static final TagKey<Item> SPELL_CAST_TOOLS = createTag("spell_cast_tools");

		public static final TagKey<Item> INFUSABLE_FOCUS = createTag("infusable/focus");
		public static final TagKey<Item> INFUSABLE_STAVES = createTag("infusable/staves");
		public static final TagKey<Item> INFUSABLE_SWORDS = createTag("infusable/swords");
		public static final TagKey<Item> INFUSABLE_PICKAXES = createTag("infusable/pickaxes");
		public static final TagKey<Item> INFUSABLE_AXES = createTag("infusable/axes");
		public static final TagKey<Item> INFUSABLE_SHOVELS = createTag("infusable/shovels");
		public static final TagKey<Item> INFUSABLE_HOES = createTag("infusable/hoes");
		public static final TagKey<Item> INFUSABLE_PAXELS = createTag("infusable/paxels");
		public static final TagKey<Item> INFUSABLE_SHILDS = createTag("infusable/shields");
		public static final TagKey<Item> INFUSABLE_BOWS = createTag("infusable/bows");
		public static final TagKey<Item> INFUSABLE_CROSSBOWS = createTag("infusable/crossbows");
		public static final TagKey<Item> INFUSABLE_FISHING_RODS = createTag("infusable/fishing_rods");
		public static final TagKey<Item> INFUSABLE_TRIDENTS = createTag("infusable/tridents");
		public static final TagKey<Item> INFUSABLE_HELMETS = createTag("infusable/helmets");
		public static final TagKey<Item> INFUSABLE_CHESTPLATES = createTag("infusable/chestplates");
		public static final TagKey<Item> INFUSABLE_LEGGINGS = createTag("infusable/leggings");
		public static final TagKey<Item> INFUSABLE_BOOTS = createTag("infusable/boots");

		public static final TagKey<Item> CHISELS = createTag("chisels");
		public static final TagKey<Item> SPELL_HOLDERS = createTag("spell_holders");
		public static final TagKey<Item> ELEMENTAL_CRYSTALS = createTag("crystals/elemental");
		public static final TagKey<Item> CRYSTALS = createTag("crystals");
		public static final TagKey<Item> LENSES = createTag("lenses");

		public static final TagKey<Item> RECEPTACLES = createTag("receptacles");
		public static final TagKey<Item> EMPTY_RECEPTACLES = createTag("receptacles/empty");
		public static final TagKey<Item> FULL_RECEPTACLES = createTag("receptacles/full");

		public static final TagKey<Item> RUNE_SLATES = createTag("rune_slates");
		public static final TagKey<Item> PUREROCKS = createTag("purerocks");
		public static final TagKey<Item> PIPES = createTag("pipes");
		public static final TagKey<Item> PIPES_UPGRADES = createTag("pipes_upgrades");
		public static final TagKey<Item> PIPE_COVER_HIDING = createTag("pipe_cover_hiding");
		public static final TagKey<Item> SHRINES = createTag("shrines");
		public static final TagKey<Item> SHRINE_UPGRADES = createTag("shrine_upgrades");

		public static final TagKey<Item> INSTRUMENTS = createTag("instruments");
		public static final TagKey<Item> ENCHANTMENT_HOLDER = createTag("enchantment_holder");

		public static final TagKey<Item> STAFF_CRAFT_SWORD = createTag("staff_craft_sword");

		public static final TagKey<Item> PURE_ORES_SOURCES = createTag("pure_ores/sources");
		public static final TagKey<Item> PURE_ORES_SOURCES_ORES = createTag("pure_ores/sources/ores");
		public static final TagKey<Item> PURE_ORES_SOURCES_RAW_MATERIALS = createTag("pure_ores/sources/raw_materials");
		public static final TagKey<Item> PURE_ORES_SOURCES_RAW_MATERIAL_BLOCKS = createTag("pure_ores/sources/raw_material_blocks");
		public static final TagKey<Item> PURE_ORES_SOURCES_CLUSTERS = createTag("pure_ores/sources/clusters");
		public static final TagKey<Item> PURE_ORES_SOURCES_GEORE_SHARDS = createTag("pure_ores/sources/geore_shards");
		public static final TagKey<Item> PURE_ORES_SOURCES_GEORE_BLOCKS = createTag("pure_ores/sources/geore_blocks");
		public static final TagKey<Item> PURE_ORES_SOURCES_RESONANT_ORE = createTag("pure_ores/sources/resonant_ore");
		public static final TagKey<Item> PURE_ORES_SOURCES_RAW_URANINITE = createTag("pure_ores/sources/raw_uraninite");
		public static final TagKey<Item> PURE_ORES_SOURCES_POOR_URANINITE = createTag("pure_ores/sources/poor_uraninite");
		public static final TagKey<Item> PURE_ORES_SOURCES_URANINITE = createTag("pure_ores/sources/uraninite");
		public static final TagKey<Item> PURE_ORES_SOURCES_DENSE_URANINITE = createTag("pure_ores/sources/dense_uraninite");
		public static final TagKey<Item> PURE_ORES_SPECIFICS = createTag("pure_ores/specifics");

		public static final TagKey<Item> JEWELS = createTag("jewels");
		public static final TagKey<Item> JEWEL_SOCKETABLES = createTag("jewel_socketables");
		public static final TagKey<Item> SOURCE_SEEDS = createTag("source_seeds");

		public static final TagKey<Item> GROVE_SHRINE_FLOWERS = createTag("grove_shrine_flowers");
		public static final TagKey<Item> GROVE_SHRINE_BLACKLIST = createTag("grove_shrine_blacklist");
		public static final TagKey<Item> MYSTICAL_GROVE_FLOWERS = createTag("mystical_grove_flowers");
		public static final TagKey<Item> INGOTS_DRENCHED_IRON = createCommonTag("ingots/drenched_iron");
		public static final TagKey<Item> INGOTS_SWIFT_ALLOY = createCommonTag("ingots/swift_alloy");
		public static final TagKey<Item> INGOTS_FIREITE = createCommonTag("ingots/fireite");
		public static final TagKey<Item> NUGGETS_DRENCHED_IRON = createCommonTag("nuggets/drenched_iron");
		public static final TagKey<Item> NUGGETS_SWIFT_ALLOY = createCommonTag("nuggets/swift_alloy");
		public static final TagKey<Item> NUGGETS_FIREITE = createCommonTag("nuggets/fireite");
		public static final TagKey<Item> STORAGE_BLOCKS_DRENCHED_IRON = createCommonTag("storage_blocks/drenched_iron");
		public static final TagKey<Item> STORAGE_BLOCKS_SWIFT_ALLOY = createCommonTag("storage_blocks/swift_alloy");
		public static final TagKey<Item> STORAGE_BLOCKS_FIREITE = createCommonTag("storage_blocks/fireite");
		public static final TagKey<Item> STORAGE_BLOCKS_RAW_MATERIALS = createCommonTag("storage_blocks/raw_materials");
		public static final TagKey<Item> ORES_INERT_CRYSTAL = createCommonTag("ores/inert_crystal");
		public static final TagKey<Item> HARDENED_RODS = createCommonTag("rods/hardened");
		public static final TagKey<Item> TOOLS_PAXELS = createCommonTag("tools/paxels");
		public static final TagKey<Item> TOOLS_AIOTS = createCommonTag("tools/aiots");

		public static final TagKey<Item> CURIOS_ELEMENT_HOLDER = createTag("curios", CuriosConstants.ELEMENT_HOLDER_SLOT);

		public static final TagKey<Item> STRIPPED_OAK = createTag("stripped_oak");
		public static final TagKey<Item> STRIPPED_DARK_OAK = createTag("stripped_dark_oak");
		public static final TagKey<Item> STRIPPED_BIRCH = createTag("stripped_birch");
		public static final TagKey<Item> STRIPPED_ACACIA = createTag("stripped_acacia");
		public static final TagKey<Item> STRIPPED_JUNGLE = createTag("stripped_jungle");
		public static final TagKey<Item> STRIPPED_SPRUCE = createTag("stripped_spruce");
		public static final TagKey<Item> STRIPPED_MANGROVE = createTag("stripped_mangrove");
		public static final TagKey<Item> STRIPPED_CRIMSON = createTag("stripped_crimson");
		public static final TagKey<Item> STRIPPED_WARPED = createTag("stripped_warped");
		public static final TagKey<Item> STRIPPED_CHERRY = createTag("stripped_cherry");
		public static final TagKey<Item> STRIPPED_BAMBOO = createTag("stripped_bamboo");

		public static final TagKey<Item> WHITE_FLOWERS = createCommonTag("flowers/white");
		public static final TagKey<Item> ORANGE_FLOWERS = createCommonTag("flowers/orange");
		public static final TagKey<Item> MAGENTA_FLOWERS = createCommonTag("flowers/magenta");
		public static final TagKey<Item> LIGHT_BLUE_FLOWERS = createCommonTag("flowers/light_blue");
		public static final TagKey<Item> YELLOW_FLOWERS = createCommonTag("flowers/yellow");
		public static final TagKey<Item> LIME_FLOWERS = createCommonTag("flowers/lime");
		public static final TagKey<Item> PINK_FLOWERS = createCommonTag("flowers/pink");
		public static final TagKey<Item> GRAY_FLOWERS = createCommonTag("flowers/gray");
		public static final TagKey<Item> LIGHT_GRAY_FLOWERS = createCommonTag("flowers/light_gray");
		public static final TagKey<Item> CYAN_FLOWERS = createCommonTag("flowers/cyan");
		public static final TagKey<Item> PURPLE_FLOWERS = createCommonTag("flowers/purple");
		public static final TagKey<Item> BLUE_FLOWERS = createCommonTag("flowers/blue");
		public static final TagKey<Item> BROWN_FLOWERS = createCommonTag("flowers/brown");
		public static final TagKey<Item> GREEN_FLOWERS = createCommonTag("flowers/green");
		public static final TagKey<Item> BLACK_FLOWERS = createCommonTag("flowers/black");
		public static final TagKey<Item> RED_FLOWERS = createCommonTag("flowers/red");

		private Items() { }

		private static TagKey<Item> createTag(String name) {
			return createTag(ElementalCraftApi.MODID, name);
		}

		private static TagKey<Item> createTag(String namespace, String name) {
			return ItemTags.create(ResourceLocation.fromNamespaceAndPath(namespace, name));
		}

		private static TagKey<Item> createCommonTag(String name) {
			return createTag(ECNames.COMMON_TAGS_NAMESPACE, name);
		}

		public static HolderSet.Named<Item> getTag(ResourceLocation loc) {
			return getTag(t -> t.location().equals(loc));
		}

		public static HolderSet.Named<Item> getTag(TagKey<Item> key) {
			return getTag(t -> t.equals(key));
		}

		public static HolderSet.Named<Item> getTag(Predicate<TagKey<Item>> predicate) {
			return BuiltInRegistries.ITEM.getTags()
					.filter(p -> predicate.test(p.getFirst()))
					.map(Pair::getSecond)
					.findFirst()
					.orElse(null);
		}
	}

	public static class Blocks {

		public static final TagKey<Block> SOURCES = createTag("sources");

		public static final TagKey<Block> PUREROCKS = createTag("purerocks");
		public static final TagKey<Block> PIPES = createTag("pipes");
		public static final TagKey<Block> SHRINES = createTag("shrines");
		public static final TagKey<Block> SHRINE_UPGRADES = createTag("shrine_upgrades");
		public static final TagKey<Block> EXTRACTORS = createTag("extractors");
		public static final TagKey<Block> SYNTHESIZERS = createTag("synthesizers");
		public static final TagKey<Block> INSTRUMENTS = createTag("instruments");
		public static final TagKey<Block> CONTAINER_TOOLS = createTag("container_tools");
		public static final TagKey<Block> SMALL_CONTAINER_TOOLS = createTag("container_tools/small");
		public static final TagKey<Block> FIRE_CONTAINER_TOOLS = createTag("container_tools/fire");
		public static final TagKey<Block> WATER_CONTAINER_TOOLS = createTag("container_tools/water");
		public static final TagKey<Block> EARTH_CONTAINER_TOOLS = createTag("container_tools/earth");
		public static final TagKey<Block> AIR_CONTAINER_TOOLS = createTag("container_tools/air");
		public static final TagKey<Block> PEDESTALS = createTag("pedestals");

		public static final TagKey<Block> RUNE_AFFECTED_SPEED = createTag("rune_affected/speed");
		public static final TagKey<Block> RUNE_AFFECTED_PRESERVATION = createTag("rune_affected/preservation");
		public static final TagKey<Block> RUNE_AFFECTED_OPTIMIZATION = createTag("rune_affected/optimization");
		public static final TagKey<Block> RUNE_AFFECTED_LUCK = createTag("rune_affected/luck");
		public static final TagKey<Block> RUNE_AFFECTED_TZEENTCH = createTag("rune_affected/tzeentch");
		public static final TagKey<Block> RUNE_AFFECTED_RANGE = createTag("rune_affected/range");

		public static final TagKey<Block> CULTIVABLE_TALL_PLANTS = createTag("cultivable_tall_plants");
		public static final TagKey<Block> USES_SINGLE_SET_FROM_ORDERED_SORTER = createTag("uses_single_set_from_ordered_sorter");

		public static final TagKey<Block> SHRINES_MELTING_LIQUIFIABLES_LAVA = createTag("shrines/melting/liquifiables/lava");
		public static final TagKey<Block> SHRINES_MELTING_LIQUIFIABLES_WATER = createTag("shrines/melting/liquifiables/water");
		public static final TagKey<Block> SHRINES_GROWTH_BLACKLIST = createTag("shrines/growth/blacklist");
		public static final TagKey<Block> SHRINES_GROWTH_BONELESS = createTag("shrines/growth/boneless");
		public static final TagKey<Block> SHRINES_ORE_HARVESTABLE_CRYSTALS = createTag("shrines/ore/harvestable_crystals");
		public static final TagKey<Block> SHRINES_HARVEST_HARVESTABLE_TALL_PLANTS = createTag("shrines/harvest/harvestable_tall_plants");

		public static final TagKey<Block> SHRINES_UPGRADABLES_ACCELERATION = createTag("shrines/upgradables/acceleration");
		public static final TagKey<Block> SHRINES_UPGRADABLES_RANGE = createTag("shrines/upgradables/range");
		public static final TagKey<Block> SHRINES_UPGRADABLES_STRENGTH = createTag("shrines/upgradables/strength");
		public static final TagKey<Block> SHRINES_UPGRADABLES_PROTECTION = createTag("shrines/upgradables/protection");
		public static final TagKey<Block> SHRINES_UPGRADABLES_PLANTING = createTag("shrines/upgradables/planting");
		public static final TagKey<Block> SHRINES_UPGRADABLES_FORTUNE = createTag("shrines/upgradables/fortune");
		public static final TagKey<Block> SHRINES_UPGRADABLES_SILK_TOUCH = createTag("shrines/upgradables/silk_touch");
		public static final TagKey<Block> SHRINES_UPGRADABLES_CRYSTAL_HARVEST = createTag("shrines/upgradables/crystal_harvest");
		public static final TagKey<Block> SHRINES_UPGRADABLES_SILK_TOUCH_ATTACHED = createTag("shrines/upgradables/silk_touch/attached");

		public static final TagKey<Block> TREE_PARTS = createTag("tree_parts");

		public static final TagKey<Block> STORAGE_BLOCKS_DRENCHED_IRON = createCommonTag("storage_blocks/drenched_iron");
		public static final TagKey<Block> STORAGE_BLOCKS_SWIFT_ALLOY = createCommonTag("storage_blocks/swift_alloy");
		public static final TagKey<Block> STORAGE_BLOCKS_FIREITE = createCommonTag("storage_blocks/fireite");

		public static final TagKey<Block> ORES_INERT_CRYSTAL = createCommonTag("ores/inert_crystal");

		public static final TagKey<Block> BAG_OF_YURTING_BLACKLIST = createTag("bagofyurting", "blacklist");

		public static final TagKey<Block> STRIPPED_OAK = createTag("stripped_oak");
		public static final TagKey<Block> STRIPPED_DARK_OAK = createTag("stripped_dark_oak");
		public static final TagKey<Block> STRIPPED_BIRCH = createTag("stripped_birch");
		public static final TagKey<Block> STRIPPED_ACACIA = createTag("stripped_acacia");
		public static final TagKey<Block> STRIPPED_JUNGLE = createTag("stripped_jungle");
		public static final TagKey<Block> STRIPPED_SPRUCE = createTag("stripped_spruce");
		public static final TagKey<Block> STRIPPED_MANGROVE = createTag("stripped_mangrove");
		public static final TagKey<Block> STRIPPED_CRIMSON = createTag("stripped_crimson");
		public static final TagKey<Block> STRIPPED_WARPED = createTag("stripped_warped");
		public static final TagKey<Block> STRIPPED_CHERRY = createTag("stripped_cherry");
		public static final TagKey<Block> STRIPPED_BAMBOO = createTag("stripped_bamboo");

        private Blocks() { }

		private static TagKey<Block> createTag(String name) {
			return createTag(ElementalCraftApi.MODID, name);
		}

		private static TagKey<Block> createCommonTag(String name) {
			return createTag(ECNames.COMMON_TAGS_NAMESPACE, name);
		}

		private static TagKey<Block> createTag(String modId, String name) {
			return BlockTags.create(ResourceLocation.fromNamespaceAndPath(modId, name));
		}

		public static HolderSet.Named<Block> getTag(ResourceLocation loc) {
			return getTag(t -> t.location().equals(loc));
		}

		public static HolderSet.Named<Block> getTag(TagKey<Block> key) {
			return getTag(t -> t.equals(key));
		}

		public static HolderSet.Named<Block> getTag(Predicate<TagKey<Block>> predicate) {
			return BuiltInRegistries.BLOCK.getTags()
					.filter(p -> predicate.test(p.getFirst()))
					.map(Pair::getSecond)
					.findFirst()
					.orElse(null);
		}
	}

	public static class Biomes {
		public static final TagKey<Biome> HAS_SOURCE_ALTAR = createTag("has_structure/source_altar");
		public static final TagKey<Biome> HAS_INERT_CRYSTAL = createTag("has_inert_crystal");
		public static final TagKey<Biome> HAS_SOURCE_ALL = createTag("has_sources/all");
		public static final TagKey<Biome> HAS_SOURCE_ICY = createTag("has_sources/icy");
		public static final TagKey<Biome> HAS_SOURCE_JUNGLE = createTag("has_sources/jungle");
		public static final TagKey<Biome> HAS_SOURCE_MUSHROOM = createTag("has_sources/mushroom");
		public static final TagKey<Biome> HAS_SOURCE_NETHER = createTag("has_sources/nether");
		public static final TagKey<Biome> HAS_SOURCE_NETHER_ALL = createTag("has_sources/nether/all");
		public static final TagKey<Biome> HAS_SOURCE_NETHER_FOREST = createTag("has_sources/nether/forest");
		public static final TagKey<Biome> HAS_SOURCE_OCEAN = createTag("has_sources/ocean");
		public static final TagKey<Biome> HAS_SOURCE_PLAIN = createTag("has_sources/plain");
		public static final TagKey<Biome> HAS_SOURCE_WET = createTag("has_sources/wet");
		public static final TagKey<Biome> HAS_SOURCE_DRY = createTag("has_sources/dry");
		public static final TagKey<Biome> HAS_SOURCE_MOUNTAIN = createTag("has_sources/mountain");
		public static final TagKey<Biome> HAS_SOURCE_HILL = createTag("has_sources/hill");
		public static final TagKey<Biome> HAS_SOURCE_FOREST = createTag("has_sources/forest");
		public static final TagKey<Biome> HAS_SOURCE_END = createTag("has_sources/end");
		public static final TagKey<Biome> HAS_SOURCE_LUSH_CAVE = createTag("has_sources/lush_cave");
		public static final TagKey<Biome> HAS_SOURCE_DRIPSTONE_CAVE = createTag("has_sources/dripstone_cave");
		public static final TagKey<Biome> HAS_SOURCE_DEEP_DARK = createTag("has_sources/deep_dark");
		public static final TagKey<Biome> HAS_SOURCE_UNDERGROUND = createTag("has_sources/underground");
		public static final TagKey<Biome> HAS_SOURCE_SKY = createTag("has_sources/sky");

		private Biomes() { }

		private static TagKey<Biome> createTag(String name) {
			return createTag(ElementalCraftApi.MODID, name);
		}

		private static TagKey<Biome> createCommonTag(String name) {
			return createTag(ECNames.COMMON_TAGS_NAMESPACE, name);
		}

		private static TagKey<Biome> createTag(String modId, String name) {
			return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(modId, name));
		}
	}

	public static class DamageTypes {
		public static final TagKey<DamageType> BYPASSES_DEFENSE_JEWELS = createTag("bypasses_jewels/defense");
		public static final TagKey<DamageType> BYPASSES_ATTACK_JEWELS = createTag("bypasses_jewels/attack");
		public static final TagKey<DamageType> BLOCKED_BY_TORTOISE_JEWEL = createTag("blocked_by_tortoise_jewel");


		private DamageTypes() { }

		private static TagKey<DamageType> createTag(String name) {
			return createTag(ElementalCraftApi.MODID, name);
		}

		private static TagKey<DamageType> createCommonTag(String name) {
			return createTag(ECNames.COMMON_TAGS_NAMESPACE, name);
		}

		private static TagKey<DamageType> createTag(String modId, String name) {
			return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name));
		}
	}

	public static class GameEvents {

		public static final TagKey<GameEvent> SYNTHESIZABLE_TO_AIR = createTag("synthesizable_to_air");

		private GameEvents() { }

		private static TagKey<GameEvent> createTag(String name) {
			return createTag(ElementalCraftApi.MODID, name);
		}

		private static TagKey<GameEvent> createCommonTag(String name) {
			return createTag(ECNames.COMMON_TAGS_NAMESPACE, name);
		}

		private static TagKey<GameEvent> createTag(String modId, String name) {
			return TagKey.create(Registries.GAME_EVENT, ResourceLocation.fromNamespaceAndPath(modId, name));
		}
	}

}
