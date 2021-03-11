package sirttas.elementalcraft.tag;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import sirttas.dpanvil.api.tag.DataTagRegistry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.rune.Rune;

public class ECTags {
	
	private ECTags() {}
	
	public static class Items {
		public static final INamedTag<Item> INFUSABLE_SWORDS = createTag("infusable/swords");
		public static final INamedTag<Item> INFUSABLE_PICKAXES = createTag("infusable/pickaxes");
		public static final INamedTag<Item> INFUSABLE_AXES = createTag("infusable/axes");
		public static final INamedTag<Item> INFUSABLE_SHOVELS = createTag("infusable/shovels");
		public static final INamedTag<Item> INFUSABLE_HOES = createTag("infusable/hoes");
		public static final INamedTag<Item> INFUSABLE_SHILDS = createTag("infusable/shields");
		public static final INamedTag<Item> INFUSABLE_BOWS = createTag("infusable/bows");
		public static final INamedTag<Item> INFUSABLE_CROSSBOWS = createTag("infusable/crossbows");
		public static final INamedTag<Item> INFUSABLE_HELMETS = createTag("infusable/helmets");
		public static final INamedTag<Item> INFUSABLE_CHESTPLATES = createTag("infusable/chestplates");
		public static final INamedTag<Item> INFUSABLE_LEGGINGS = createTag("infusable/leggings");
		public static final INamedTag<Item> INFUSABLE_BOOTS = createTag("infusable/boots");

		public static final INamedTag<Item> SPELL_HOLDERS = createTag("spell_holders");
		public static final INamedTag<Item> ELEMENTAL_CRYSTALS = createTag("crystals/elemental");
		public static final INamedTag<Item> CRYSTALS = createTag("crystals");
		public static final INamedTag<Item> LENSES = createTag("lenses");

		public static final INamedTag<Item> SHARDS = createTag("shards");
		public static final INamedTag<Item> DEFAULT_SHARDS = createTag("shards/default");
		public static final INamedTag<Item> POWERFUL_SHARDS = createTag("shards/powerful");
		public static final INamedTag<Item> FIRE_SHARDS = createTag("shards/fire");
		public static final INamedTag<Item> WATER_SHARDS = createTag("shards/water");
		public static final INamedTag<Item> EARTH_SHARDS = createTag("shards/earth");
		public static final INamedTag<Item> AIR_SHARDS = createTag("shards/air");

		public static final INamedTag<Item> CRUDE_FIRE_GEMS = createTag("gems/crude_fire");
		public static final INamedTag<Item> CRUDE_WATER_GEMS = createTag("gems/crude_water");
		public static final INamedTag<Item> CRUDE_EARTH_GEMS = createTag("gems/crude_earth");
		public static final INamedTag<Item> CRUDE_AIR_GEMS = createTag("gems/crude_air");
		public static final INamedTag<Item> FINE_FIRE_GEMS = createTag("gems/fine_fire");
		public static final INamedTag<Item> FINE_WATER_GEMS = createTag("gems/fine_water");
		public static final INamedTag<Item> FINE_EARTH_GEMS = createTag("gems/fine_earth");
		public static final INamedTag<Item> FINE_AIR_GEMS = createTag("gems/fine_air");
		public static final INamedTag<Item> PRISTINE_FIRE_GEMS = createTag("gems/pristine_fire");
		public static final INamedTag<Item> PRISTINE_WATER_GEMS = createTag("gems/pristine_water");
		public static final INamedTag<Item> PRISTINE_EARTH_GEMS = createTag("gems/pristine_earth");
		public static final INamedTag<Item> PRISTINE_AIR_GEMS = createTag("gems/pristine_air");
		public static final INamedTag<Item> INPUT_FIRE_GEMS = createTag("gems/input_fire");
		public static final INamedTag<Item> INPUT_WATER_GEMS = createTag("gems/input_water");
		public static final INamedTag<Item> INPUT_EARTH_GEMS = createTag("gems/input_earth");
		public static final INamedTag<Item> INPUT_AIR_GEMS = createTag("gems/input_air");
		public static final INamedTag<Item> INPUT_GEMS = createTag("gems/input");

		public static final INamedTag<Item> RUNE_SLATES = createTag("rune_slates");
		public static final INamedTag<Item> PUREROCKS = createTag("purerocks");
		public static final INamedTag<Item> PIPES = createTag("pipes");
		public static final INamedTag<Item> SHRINES = createTag("shrines");
		
		public static final INamedTag<Item> PURE_ORES = createTag("pure_ores");
		public static final INamedTag<Item> PURE_ORES_MOD_PROCESSING_BLACKLIST = createTag("pure_ores/mod_processing_blacklist");

		public static final INamedTag<Item> IMPROVED_RECEPTACLES = createTag("improved_receptacles");
		public static final INamedTag<Item> MYSTICAL_GROVE_FLOWERS = createTag("mystical_grove_flowers");
		public static final INamedTag<Item> BOTANIA_PETALS = createOptional("botania", "petals");
		public static final INamedTag<Item> BOTANIA_LIVINGROCK = createOptional("botania", "livingrock");
		
		public static final INamedTag<Item> FORGE_SWORDS = createForgeTag("swords");
		public static final INamedTag<Item> FORGE_PICKAXES = createForgeTag("pickaxes");
		public static final INamedTag<Item> FORGE_AXES = createForgeTag("axes");
		public static final INamedTag<Item> FORGE_SHOVELS = createForgeTag("shovels");
		public static final INamedTag<Item> FORGE_HOES = createForgeTag("hoes");
		public static final INamedTag<Item> FORGE_SHILDS = createForgeTag("shields");
		public static final INamedTag<Item> FORGE_BOWS = createForgeTag("bows");
		public static final INamedTag<Item> FORGE_CROSSBOWS = createForgeTag("crossbows");
		public static final INamedTag<Item> FORGE_HELMETS = createForgeTag("helmets");
		public static final INamedTag<Item> FORGE_CHESTPLATES = createForgeTag("chestplates");
		public static final INamedTag<Item> FORGE_LEGGINGS = createForgeTag("leggings");
		public static final INamedTag<Item> FORGE_BOOTS = createForgeTag("boots");

		public static final INamedTag<Item> INGOTS_MANASTEEL = createOptional("forge", "ingots/manasteel");
		public static final INamedTag<Item> INGOTS_DRENCHED_IRON = createForgeTag("ingots/drenched_iron");
		public static final INamedTag<Item> INGOTS_SWIFT_ALLOY = createForgeTag("ingots/swift_alloy");
		public static final INamedTag<Item> INGOTS_FIREITE = createForgeTag("ingots/fireite");
		public static final INamedTag<Item> NUGGETS_DRENCHED_IRON = createForgeTag("nuggets/drenched_iron");
		public static final INamedTag<Item> NUGGETS_SWIFT_ALLOY = createForgeTag("nuggets/swift_alloy");
		public static final INamedTag<Item> NUGGETS_FIREITE = createForgeTag("nuggets/fireite");
		public static final INamedTag<Item> STORAGE_BLOCKS_DRENCHED_IRON = createForgeTag("storage_blocks/drenched_iron");
		public static final INamedTag<Item> STORAGE_BLOCKS_SWIFT_ALLOY = createForgeTag("storage_blocks/swift_alloy");
		public static final INamedTag<Item> STORAGE_BLOCKS_FIREITE = createForgeTag("storage_blocks/fireite");

		private Items() {}
		
		private static INamedTag<Item> createTag(String name) {
			return ItemTags.makeWrapperTag(ElementalCraft.MODID + ':' + name);
		}

		private static INamedTag<Item> createOptional(String namespace, String name) {
			return ItemTags.createOptional(new ResourceLocation(namespace, name));
		}
		
		private static INamedTag<Item> createForgeTag(String name) {
			return ItemTags.makeWrapperTag("forge:" + name);
		}

		public static ITag<Item> getTag(ResourceLocation loc) {
			ITag<Item> tag = ItemTags.getCollection().get(loc);

			if (tag == null) {
				tag = TagCollectionManager.getManager().getItemTags().get(loc);
			}
			return tag;
		}

		public static ResourceLocation getTagName(ITag<Item> tag) {
			if (tag instanceof INamedTag) {
				return ((INamedTag<Item>) tag).getName();
			}
			ResourceLocation loc = ItemTags.getCollection().getDirectIdFromTag(tag);

			if (loc == null) {
				loc = TagCollectionManager.getManager().getItemTags().getDirectIdFromTag(tag);
			}
			return loc;
		}
	}

	public static class Blocks {
		public static final INamedTag<Block> LAVASHRINE_LIQUIFIABLES = createTag("lavashrine_liquifiables");
		public static final INamedTag<Block> SMALL_TANK_COMPATIBLES = createTag("small_tank_compatibles");

		public static final INamedTag<Block> PUREROCKS = createTag("purerocks");
		public static final INamedTag<Block> PIPES = createTag("pipes");
		public static final INamedTag<Block> SHRINES = createTag("shrines");
		public static final INamedTag<Block> INSTRUMENTS = createTag("instrumentrs");
		public static final INamedTag<Block> PEDESTALS = createTag("pedestals");

		public static final INamedTag<Block> RUNE_AFFECTED = createTag("rune_affected");
		public static final INamedTag<Block> RUNE_AFFECTED_SPEED = createTag("rune_affected/speed");
		public static final INamedTag<Block> RUNE_AFFECTED_PRESERVATION = createTag("rune_affected/preservation");
		public static final INamedTag<Block> RUNE_AFFECTED_LUCK = createTag("rune_affected/luck");

		public static final INamedTag<Block> SHRINES_UPGRADABLES_ACCELERATION = createTag("shrines/upgradables/acceleration");
		public static final INamedTag<Block> SHRINES_UPGRADABLES_RANGE = createTag("shrines/upgradables/range");
		public static final INamedTag<Block> SHRINES_UPGRADABLES_STRENGTH = createTag("shrines/upgradables/strength");

		public static final INamedTag<Block> STORAGE_BLOCKS_DRENCHED_IRON = createForgeTag("storage_blocks/drenched_iron");
		public static final INamedTag<Block> STORAGE_BLOCKS_SWIFT_ALLOY = createForgeTag("storage_blocks/swift_alloy");
		public static final INamedTag<Block> STORAGE_BLOCKS_FIREITE = createForgeTag("storage_blocks/fireite");

		private Blocks() {}
		
		private static INamedTag<Block> createTag(String name) {
			return BlockTags.makeWrapperTag(ElementalCraft.MODID + ':' + name);
		}

		private static INamedTag<Block> createForgeTag(String name) {
			return BlockTags.makeWrapperTag("forge:" + name);
		}

		public static ITag<Block> getTag(ResourceLocation loc) {
			ITag<Block> tag = BlockTags.getCollection().get(loc);

			if (tag == null) {
				tag = TagCollectionManager.getManager().getBlockTags().get(loc);
			}
			return tag;
		}

		public static ResourceLocation getTagName(ITag<Block> tag) {
			if (tag instanceof INamedTag) {
				return ((INamedTag<Block>) tag).getName();
			}
			ResourceLocation loc = BlockTags.getCollection().getDirectIdFromTag(tag);

			if (loc == null) {
				loc = TagCollectionManager.getManager().getBlockTags().getDirectIdFromTag(tag);
			}
			return loc;
		}
	}
	
	public static class Runes {
		
		public static final DataTagRegistry<Rune> RUNE_TAGS = new DataTagRegistry<>();
		
		public static final INamedTag<Rune> SPEED_RUNES = createTag("speed_runes");
		public static final INamedTag<Rune> ELEMENT_PRESERVATION_RUNES = createTag("element_preservation_runes");
		public static final INamedTag<Rune> LUCK_RUNES = createTag("luck_runes");
		
		private Runes() {}
		
		private static INamedTag<Rune> createTag(String name) {
			return RUNE_TAGS.makeWrapperTag(ElementalCraft.createRL(name));
		}
	}
}
