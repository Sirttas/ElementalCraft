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

		public static final TagKey<Item> SPELL_HOLDERS = createTag("spell_holders");
		public static final TagKey<Item> ELEMENTAL_CRYSTALS = createTag("crystals/elemental");
		public static final TagKey<Item> CRYSTALS = createTag("crystals");
		public static final TagKey<Item> LENSES = createTag("lenses");

		public static final TagKey<Item> SHARDS = createTag("shards");
		public static final TagKey<Item> DEFAULT_SHARDS = createTag("shards/default");
		public static final TagKey<Item> POWERFUL_SHARDS = createTag("shards/powerful");
		public static final TagKey<Item> FIRE_SHARDS = createTag("shards/fire");
		public static final TagKey<Item> WATER_SHARDS = createTag("shards/water");
		public static final TagKey<Item> EARTH_SHARDS = createTag("shards/earth");
		public static final TagKey<Item> AIR_SHARDS = createTag("shards/air");

		public static final TagKey<Item> CRUDE_FIRE_GEMS = createTag("gems/crude_fire");
		public static final TagKey<Item> CRUDE_WATER_GEMS = createTag("gems/crude_water");
		public static final TagKey<Item> CRUDE_EARTH_GEMS = createTag("gems/crude_earth");
		public static final TagKey<Item> CRUDE_AIR_GEMS = createTag("gems/crude_air");
		public static final TagKey<Item> FINE_FIRE_GEMS = createTag("gems/fine_fire");
		public static final TagKey<Item> FINE_WATER_GEMS = createTag("gems/fine_water");
		public static final TagKey<Item> FINE_EARTH_GEMS = createTag("gems/fine_earth");
		public static final TagKey<Item> FINE_AIR_GEMS = createTag("gems/fine_air");
		public static final TagKey<Item> PRISTINE_FIRE_GEMS = createTag("gems/pristine_fire");
		public static final TagKey<Item> PRISTINE_WATER_GEMS = createTag("gems/pristine_water");
		public static final TagKey<Item> PRISTINE_EARTH_GEMS = createTag("gems/pristine_earth");
		public static final TagKey<Item> PRISTINE_AIR_GEMS = createTag("gems/pristine_air");
		public static final TagKey<Item> INPUT_FIRE_GEMS = createTag("gems/input_fire");
		public static final TagKey<Item> INPUT_WATER_GEMS = createTag("gems/input_water");
		public static final TagKey<Item> INPUT_EARTH_GEMS = createTag("gems/input_earth");
		public static final TagKey<Item> INPUT_AIR_GEMS = createTag("gems/input_air");
		public static final TagKey<Item> INPUT_GEMS = createTag("gems/input");

		public static final TagKey<Item> RUNE_SLATES = createTag("rune_slates");
		public static final TagKey<Item> PUREROCKS = createTag("purerocks");
		public static final TagKey<Item> PIPES = createTag("pipes");
		public static final TagKey<Item> PIPES_UPGRADES = createTag("pipes_upgrades");
		public static final TagKey<Item> PIPE_COVER_HIDING = createTag("pipe_cover_hiding");
		public static final TagKey<Item> SHRINES = createTag("shrines");
		public static final TagKey<Item> SHRINE_UPGRADES = createTag("shrine_upgrades");

		public static final TagKey<Item> SMALL_CONTAINER_COMPATIBLES = createTag("small_container_compatibles");
		public static final TagKey<Item> INSTRUMENTS = createTag("instruments");
		public static final TagKey<Item> CONTAINER_TOOLS = createTag("container_tools");
		public static final TagKey<Item> ENCHANTMENT_HOLDER = createTag("enchantment_holder");

		public static final TagKey<Item> STAFF_CRAFT_SWORD = createTag("staff_craft_sword");

		public static final TagKey<Item> PURE_ORES_SOURCE_ORES = createTag("pure_ores/sources/ores");
		public static final TagKey<Item> PURE_ORES_SOURCE_RAW_MATERIALS = createTag("pure_ores/sources/raw_materials");
		public static final TagKey<Item> PURE_ORES_SOURCE_RAW_MATERIAL_BLOCKS = createTag("pure_ores/sources/raw_material_blocks");
		public static final TagKey<Item> PURE_ORES_SOURCE_GEORE_SHARDS = createTag("pure_ores/sources/geore_shards");
		public static final TagKey<Item> PURE_ORES_SOURCE_GEORE_BLOCKS = createTag("pure_ores/sources/geore_blocks");
		public static final TagKey<Item> PURE_ORES_SOURCE_RESONANT_ORE = createTag("pure_ores/sources/resonant_ore");
		public static final TagKey<Item> PURE_ORES_SOURCE_RAW_URANINITE = createTag("pure_ores/sources/raw_uraninite");
		public static final TagKey<Item> PURE_ORES_SOURCE_POOR_URANINITE = createTag("pure_ores/sources/poor_uraninite");
		public static final TagKey<Item> PURE_ORES_SOURCE_URANINITE = createTag("pure_ores/sources/uraninite");
		public static final TagKey<Item> PURE_ORES_SOURCE_DENSE_URANINITE = createTag("pure_ores/sources/dense_uraninite");
		public static final TagKey<Item> PURE_ORES_SPECIFICS = createTag("pure_ores/specifics");

		public static final TagKey<Item> JEWEL_SOCKETABLES = createTag("jewel_socketables");
		public static final TagKey<Item> SOURCE_SEEDS = createTag("source_seeds");
		public static final TagKey<Item> NATURAL_SOURCE_SEEDS = createTag("source_seeds/natural");
		public static final TagKey<Item> ARTIFICIAL_SOURCE_SEEDS = createTag("source_seeds/artificial");

		public static final TagKey<Item> GROVE_SHRINE_FLOWERS = createTag("grove_shrine_flowers");
		public static final TagKey<Item> GROVE_SHRINE_BLACKLIST = createTag("grove_shrine_blacklist");
		public static final TagKey<Item> MYSTICAL_GROVE_FLOWERS = createTag("mystical_grove_flowers");
		public static final TagKey<Item> INGOTS_DRENCHED_IRON = createForgeTag("ingots/drenched_iron");
		public static final TagKey<Item> INGOTS_SWIFT_ALLOY = createForgeTag("ingots/swift_alloy");
		public static final TagKey<Item> INGOTS_FIREITE = createForgeTag("ingots/fireite");
		public static final TagKey<Item> NUGGETS_DRENCHED_IRON = createForgeTag("nuggets/drenched_iron");
		public static final TagKey<Item> NUGGETS_SWIFT_ALLOY = createForgeTag("nuggets/swift_alloy");
		public static final TagKey<Item> NUGGETS_FIREITE = createForgeTag("nuggets/fireite");
		public static final TagKey<Item> STORAGE_BLOCKS_DRENCHED_IRON = createForgeTag("storage_blocks/drenched_iron");
		public static final TagKey<Item> STORAGE_BLOCKS_SWIFT_ALLOY = createForgeTag("storage_blocks/swift_alloy");
		public static final TagKey<Item> STORAGE_BLOCKS_FIREITE = createForgeTag("storage_blocks/fireite");
		public static final TagKey<Item> STORAGE_BLOCKS_RAW_MATERIALS = createForgeTag("storage_blocks/raw_materials");
		public static final TagKey<Item> ORES_INERT_CRYSTAL = createForgeTag("ores/inert_crystal");
		public static final TagKey<Item> HARDENED_RODS = createForgeTag("rods/hardened");
		public static final TagKey<Item> TOOLS_PAXELS = createForgeTag("tools/paxels");
		public static final TagKey<Item> TOOLS_AIOTS = createForgeTag("tools/aiots");

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

		public static final TagKey<Item> WHITE_FLOWERS = createForgeTag("flowers/white");
		public static final TagKey<Item> ORANGE_FLOWERS = createForgeTag("flowers/orange");
		public static final TagKey<Item> MAGENTA_FLOWERS = createForgeTag("flowers/magenta");
		public static final TagKey<Item> LIGHT_BLUE_FLOWERS = createForgeTag("flowers/light_blue");
		public static final TagKey<Item> YELLOW_FLOWERS = createForgeTag("flowers/yellow");
		public static final TagKey<Item> LIME_FLOWERS = createForgeTag("flowers/lime");
		public static final TagKey<Item> PINK_FLOWERS = createForgeTag("flowers/pink");
		public static final TagKey<Item> GRAY_FLOWERS = createForgeTag("flowers/gray");
		public static final TagKey<Item> LIGHT_GRAY_FLOWERS = createForgeTag("flowers/light_gray");
		public static final TagKey<Item> CYAN_FLOWERS = createForgeTag("flowers/cyan");
		public static final TagKey<Item> PURPLE_FLOWERS = createForgeTag("flowers/purple");
		public static final TagKey<Item> BLUE_FLOWERS = createForgeTag("flowers/blue");
		public static final TagKey<Item> BROWN_FLOWERS = createForgeTag("flowers/brown");
		public static final TagKey<Item> GREEN_FLOWERS = createForgeTag("flowers/green");
		public static final TagKey<Item> BLACK_FLOWERS = createForgeTag("flowers/black");
		public static final TagKey<Item> RED_FLOWERS = createForgeTag("flowers/red");


		private Items() {
		}

		private static TagKey<Item> createTag(String name) {
			return createTag(ElementalCraftApi.MODID, name);
		}

		private static TagKey<Item> createTag(String namespace, String name) {
			return ItemTags.create(new ResourceLocation(namespace, name));
		}

		private static TagKey<Item> createForgeTag(String name) {
			return createTag(ECNames.FORGE, name);
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
		public static final TagKey<Block> SMALL_CONTAINER_COMPATIBLES = createTag("small_container_compatibles");

		public static final TagKey<Block> PUREROCKS = createTag("purerocks");
		public static final TagKey<Block> PIPES = createTag("pipes");
		public static final TagKey<Block> SHRINES = createTag("shrines");
		public static final TagKey<Block> SHRINE_UPGRADES = createTag("shrine_upgrades");
		public static final TagKey<Block> INSTRUMENTS = createTag("instruments");
		public static final TagKey<Block> CONTAINER_TOOLS = createTag("container_tools");
		public static final TagKey<Block> PEDESTALS = createTag("pedestals");

		public static final TagKey<Block> RUNE_AFFECTED_SPEED = createTag("rune_affected/speed");
		public static final TagKey<Block> RUNE_AFFECTED_PRESERVATION = createTag("rune_affected/preservation");
		public static final TagKey<Block> RUNE_AFFECTED_OPTIMIZATION = createTag("rune_affected/optimization");
		public static final TagKey<Block> RUNE_AFFECTED_LUCK = createTag("rune_affected/luck");
		public static final TagKey<Block> RUNE_AFFECTED_TZEENTCH = createTag("rune_affected/tzeentch");

		public static final TagKey<Block> SHRINES_LAVA_LIQUIFIABLES = createTag("shrines/lava/liquifiables");
		public static final TagKey<Block> SHRINES_GROWTH_BLACKLIST = createTag("shrines/growth/blacklist");
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

		public static final TagKey<Block> STORAGE_BLOCKS_DRENCHED_IRON = createForgeTag("storage_blocks/drenched_iron");
		public static final TagKey<Block> STORAGE_BLOCKS_SWIFT_ALLOY = createForgeTag("storage_blocks/swift_alloy");
		public static final TagKey<Block> STORAGE_BLOCKS_FIREITE = createForgeTag("storage_blocks/fireite");

		public static final TagKey<Block> ORES_INERT_CRYSTAL = createForgeTag("ores/inert_crystal");
		public static final TagKey<Block> BUDDING = createForgeTag("budding");
		public static final TagKey<Block> BUDS = createForgeTag("buds");
		public static final TagKey<Block> CLUSTERS = createForgeTag("clusters");

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

		private Blocks() {
		}

		private static TagKey<Block> createTag(String name) {
			return createTag(ElementalCraftApi.MODID, name);
		}

		private static TagKey<Block> createForgeTag(String name) {
			return createTag(ECNames.FORGE, name);
		}

		private static TagKey<Block> createTag(String modId, String name) {
			return BlockTags.create(new ResourceLocation(modId, name));
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


		private Biomes() {
		}

		private static TagKey<Biome> createTag(String name) {
			return createTag(ElementalCraftApi.MODID, name);
		}

		private static TagKey<Biome> createForgeTag(String name) {
			return createTag(ECNames.FORGE, name);
		}

		private static TagKey<Biome> createTag(String modId, String name) {
			return TagKey.create(Registries.BIOME, new ResourceLocation(modId, name));
		}
	}

	public static class DamageTypes {
		public static final TagKey<DamageType> BYPASSES_JEWELS = createTag("bypasses_jewels");
		public static final TagKey<DamageType> BLOCKED_BY_TORTOISE_JEWEL = createTag("blocked_by_tortoise_jewel");


		private DamageTypes() {
		}

		private static TagKey<DamageType> createTag(String name) {
			return createTag(ElementalCraftApi.MODID, name);
		}

		private static TagKey<DamageType> createForgeTag(String name) {
			return createTag(ECNames.FORGE, name);
		}

		private static TagKey<DamageType> createTag(String modId, String name) {
			return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(modId, name));
		}
	}
}
