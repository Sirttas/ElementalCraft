package sirttas.elementalcraft.tag;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.SerializationTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.Tag.Named;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune;

public class ECTags {
	
	private ECTags() {}
	
	public static class Items {
		public static final Named<Item> SPELL_CAST_TOOLS = createTag("spell_cast_tools");
		
		public static final Named<Item> INFUSABLE_FOCUS = createTag("infusable/focus");
		public static final Named<Item> INFUSABLE_STAVES = createTag("infusable/staves");
		public static final Named<Item> INFUSABLE_SWORDS = createTag("infusable/swords");
		public static final Named<Item> INFUSABLE_PICKAXES = createTag("infusable/pickaxes");
		public static final Named<Item> INFUSABLE_AXES = createTag("infusable/axes");
		public static final Named<Item> INFUSABLE_SHOVELS = createTag("infusable/shovels");
		public static final Named<Item> INFUSABLE_HOES = createTag("infusable/hoes");
		public static final Named<Item> INFUSABLE_SHILDS = createTag("infusable/shields");
		public static final Named<Item> INFUSABLE_BOWS = createTag("infusable/bows");
		public static final Named<Item> INFUSABLE_CROSSBOWS = createTag("infusable/crossbows");
		public static final Named<Item> INFUSABLE_FISHING_RODS = createTag("infusable/fishing_rods");
		public static final Named<Item> INFUSABLE_TRIDENTS = createTag("infusable/tridents");
		public static final Named<Item> INFUSABLE_HELMETS = createTag("infusable/helmets");
		public static final Named<Item> INFUSABLE_CHESTPLATES = createTag("infusable/chestplates");
		public static final Named<Item> INFUSABLE_LEGGINGS = createTag("infusable/leggings");
		public static final Named<Item> INFUSABLE_BOOTS = createTag("infusable/boots");

		public static final Named<Item> SPELL_HOLDERS = createTag("spell_holders");
		public static final Named<Item> ELEMENTAL_CRYSTALS = createTag("crystals/elemental");
		public static final Named<Item> CRYSTALS = createTag("crystals");
		public static final Named<Item> LENSES = createTag("lenses");

		public static final Named<Item> SHARDS = createTag("shards");
		public static final Named<Item> DEFAULT_SHARDS = createTag("shards/default");
		public static final Named<Item> POWERFUL_SHARDS = createTag("shards/powerful");
		public static final Named<Item> FIRE_SHARDS = createTag("shards/fire");
		public static final Named<Item> WATER_SHARDS = createTag("shards/water");
		public static final Named<Item> EARTH_SHARDS = createTag("shards/earth");
		public static final Named<Item> AIR_SHARDS = createTag("shards/air");

		public static final Named<Item> CRUDE_FIRE_GEMS = createTag("gems/crude_fire");
		public static final Named<Item> CRUDE_WATER_GEMS = createTag("gems/crude_water");
		public static final Named<Item> CRUDE_EARTH_GEMS = createTag("gems/crude_earth");
		public static final Named<Item> CRUDE_AIR_GEMS = createTag("gems/crude_air");
		public static final Named<Item> FINE_FIRE_GEMS = createTag("gems/fine_fire");
		public static final Named<Item> FINE_WATER_GEMS = createTag("gems/fine_water");
		public static final Named<Item> FINE_EARTH_GEMS = createTag("gems/fine_earth");
		public static final Named<Item> FINE_AIR_GEMS = createTag("gems/fine_air");
		public static final Named<Item> PRISTINE_FIRE_GEMS = createTag("gems/pristine_fire");
		public static final Named<Item> PRISTINE_WATER_GEMS = createTag("gems/pristine_water");
		public static final Named<Item> PRISTINE_EARTH_GEMS = createTag("gems/pristine_earth");
		public static final Named<Item> PRISTINE_AIR_GEMS = createTag("gems/pristine_air");
		public static final Named<Item> INPUT_FIRE_GEMS = createTag("gems/input_fire");
		public static final Named<Item> INPUT_WATER_GEMS = createTag("gems/input_water");
		public static final Named<Item> INPUT_EARTH_GEMS = createTag("gems/input_earth");
		public static final Named<Item> INPUT_AIR_GEMS = createTag("gems/input_air");
		public static final Named<Item> INPUT_GEMS = createTag("gems/input");

		public static final Named<Item> RUNE_SLATES = createTag("rune_slates");
		public static final Named<Item> PUREROCKS = createTag("purerocks");
		public static final Named<Item> PIPES = createTag("pipes");
		public static final Named<Item> SHRINES = createTag("shrines");
		
		public static final Named<Item> PIPE_COVER_HIDING = createTag("pipe_cover_hiding");
		
		public static final Named<Item> STAFF_CRAFT_SWORD = createTag("staff_craft_sword");
		
		public static final Named<Item> PURE_ORES_ORE_SOURCE = createTag("pure_ores/ore_source");
		public static final Named<Item> PURE_ORES_RAW_METAL_SOURCE = createTag("pure_ores/raw_metas_source");
		public static final Named<Item> PURE_ORES_MOD_PROCESSING_BLACKLIST = createTag("pure_ores/mod_processing_blacklist");

		public static final Named<Item> IMPROVED_RECEPTACLES = createTag("improved_receptacles");
		public static final Named<Item> GROVE_SHRINE_FLOWERS = createTag("grove_shrine_flowers");
		public static final Named<Item> GROVE_SHRINE_BLACKLIST = createTag("grove_shrine_blacklist");
		public static final Named<Item> MYSTICAL_GROVE_FLOWERS = createTag("mystical_grove_flowers");
		public static final Named<Item> BOTANIA_PETALS = createOptional("botania", "petals");
		public static final Named<Item> BOTANIA_LIVINGROCK = createOptional("botania", "livingrock");
		
		public static final Named<Item> FORGE_SWORDS = createForgeTag("swords");
		public static final Named<Item> FORGE_PICKAXES = createForgeTag("pickaxes");
		public static final Named<Item> FORGE_AXES = createForgeTag("axes");
		public static final Named<Item> FORGE_SHOVELS = createForgeTag("shovels");
		public static final Named<Item> FORGE_HOES = createForgeTag("hoes");
		public static final Named<Item> FORGE_SHILDS = createForgeTag("shields");
		public static final Named<Item> FORGE_BOWS = createForgeTag("bows");
		public static final Named<Item> FORGE_CROSSBOWS = createForgeTag("crossbows");
		public static final Named<Item> FORGE_HELMETS = createForgeTag("helmets");
		public static final Named<Item> FORGE_CHESTPLATES = createForgeTag("chestplates");
		public static final Named<Item> FORGE_LEGGINGS = createForgeTag("leggings");
		public static final Named<Item> FORGE_BOOTS = createForgeTag("boots");

		public static final Named<Item> INGOTS_MANASTEEL = createOptional("forge", "ingots/manasteel");
		public static final Named<Item> INGOTS_DRENCHED_IRON = createForgeTag("ingots/drenched_iron");
		public static final Named<Item> INGOTS_SWIFT_ALLOY = createForgeTag("ingots/swift_alloy");
		public static final Named<Item> INGOTS_FIREITE = createForgeTag("ingots/fireite");
		public static final Named<Item> NUGGETS_DRENCHED_IRON = createForgeTag("nuggets/drenched_iron");
		public static final Named<Item> NUGGETS_SWIFT_ALLOY = createForgeTag("nuggets/swift_alloy");
		public static final Named<Item> NUGGETS_FIREITE = createForgeTag("nuggets/fireite");
		public static final Named<Item> STORAGE_BLOCKS_DRENCHED_IRON = createForgeTag("storage_blocks/drenched_iron");
		public static final Named<Item> STORAGE_BLOCKS_SWIFT_ALLOY = createForgeTag("storage_blocks/swift_alloy");
		public static final Named<Item> STORAGE_BLOCKS_FIREITE = createForgeTag("storage_blocks/fireite");

		private Items() {}
		
		private static Named<Item> createTag(String name) {
			return ItemTags.bind(ElementalCraftApi.MODID + ':' + name);
		}

		private static Named<Item> createOptional(String namespace, String name) {
			return ItemTags.createOptional(new ResourceLocation(namespace, name));
		}
		
		private static Named<Item> createForgeTag(String name) {
			return ItemTags.bind("forge:" + name);
		}

		public static Tag<Item> getTag(ResourceLocation loc) {
			Tag<Item> tag = ItemTags.getAllTags().getTag(loc);

			if (tag != null) {
				return tag;
			}
			return SerializationTags.getInstance().getTagOrThrow(Registry.ITEM_REGISTRY, loc, id -> new IllegalStateException("Unknown item tag '" + id + "'"));
		}

		public static ResourceLocation getTagName(Tag<Item> tag) {
			if (tag instanceof Named) {
				return ((Named<Item>) tag).getName();
			}
			return ItemTags.getAllTags().getId(tag);
		}
	}

	public static class Blocks {
		public static final Named<Block> LAVASHRINE_LIQUIFIABLES = createTag("lavashrine_liquifiables");
		public static final Named<Block> SMALL_CONTAINER_COMPATIBLES = createTag("small_container_compatibles");

		public static final Named<Block> PUREROCKS = createTag("purerocks");
		public static final Named<Block> PIPES = createTag("pipes");
		public static final Named<Block> SHRINES = createTag("shrines");
		public static final Named<Block> INSTRUMENTS = createTag("instruments");
		public static final Named<Block> CONTAINER_TOOLS = createTag("container_tools");
		public static final Named<Block> PEDESTALS = createTag("pedestals");

		public static final Named<Block> RUNE_AFFECTED = createTag("rune_affected");
		public static final Named<Block> RUNE_AFFECTED_SPEED = createTag("rune_affected/speed");
		public static final Named<Block> RUNE_AFFECTED_PRESERVATION = createTag("rune_affected/preservation");
		public static final Named<Block> RUNE_AFFECTED_LUCK = createTag("rune_affected/luck");

		public static final Named<Block> SHRINES_UPGRADABLES_ACCELERATION = createTag("shrines/upgradables/acceleration");
		public static final Named<Block> SHRINES_UPGRADABLES_RANGE = createTag("shrines/upgradables/range");
		public static final Named<Block> SHRINES_UPGRADABLES_STRENGTH = createTag("shrines/upgradables/strength");
		public static final Named<Block> SHRINES_UPGRADABLES_PROTECTION = createTag("shrines/upgradables/protection");

		public static final Named<Block> STORAGE_BLOCKS_DRENCHED_IRON = createForgeTag("storage_blocks/drenched_iron");
		public static final Named<Block> STORAGE_BLOCKS_SWIFT_ALLOY = createForgeTag("storage_blocks/swift_alloy");
		public static final Named<Block> STORAGE_BLOCKS_FIREITE = createForgeTag("storage_blocks/fireite");
		
		public static final Named<Block> BAG_OF_YURTING_BLACKLIST = createTag("bagofyurting", "blacklist");

		private Blocks() {}
		
		private static Named<Block> createTag(String name) {
			return createTag(ElementalCraftApi.MODID, name);
		}

		private static Named<Block> createForgeTag(String name) {
			return createTag("forge", name);
		}

		private static Named<Block> createTag(String modId, String name) {
			return BlockTags.bind(modId + ':' + name);
		}
		
		public static Tag<Block> getTag(ResourceLocation loc) {
			var tag = BlockTags.getAllTags().getTag(loc);
			
			if (tag != null) {
				return tag;
			}
			return SerializationTags.getInstance().getTagOrThrow(Registry.BLOCK_REGISTRY, loc, id -> new IllegalStateException("Unknown bock tag '" + id + "'"));
		}

		public static ResourceLocation getTagName(Tag<Block> tag) {
			if (tag instanceof Named) {
				return ((Named<Block>) tag).getName();
			}
			return BlockTags.getAllTags().getId(tag);
		}
	}
	
	public static class Runes {
		
		public static final Named<Rune> SPEED_RUNES = createTag("speed_runes");
		public static final Named<Rune> ELEMENT_PRESERVATION_RUNES = createTag("element_preservation_runes");
		public static final Named<Rune> LUCK_RUNES = createTag("luck_runes");
		
		private Runes() {}
		
		private static Named<Rune> createTag(String name) {
			return ElementalCraftApi.RUNE_TAGS.makeWrapperTag(ElementalCraft.createRL(name));
		}
	}
}
