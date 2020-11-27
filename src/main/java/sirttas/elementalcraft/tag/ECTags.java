package sirttas.elementalcraft.tag;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;

public class ECTags {
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

		public static final INamedTag<Item> SHARDS = createTag("shards");
		public static final INamedTag<Item> FIRE_SHARDS = createTag("shards/fire");
		public static final INamedTag<Item> WATER_SHARDS = createTag("shards/water");
		public static final INamedTag<Item> EARTH_SHARDS = createTag("shards/earth");
		public static final INamedTag<Item> AIR_SHARDS = createTag("shards/air");

		public static final INamedTag<Item> PUREROCKS = createTag("purerocks");
		public static final INamedTag<Item> PIPES = createTag("pipes");
		public static final INamedTag<Item> SHRINES = createTag("shrines");

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

		public static final INamedTag<Item> INGOTS_DRENCHED_IRON = createForgeTag("ingots/drenched_iron");
		public static final INamedTag<Item> INGOTS_SWIFT_ALLOY = createForgeTag("ingots/swift_alloy");
		public static final INamedTag<Item> NUGGETS_DRENCHED_IRON = createForgeTag("nuggets/drenched_iron");
		public static final INamedTag<Item> NUGGETS_SWIFT_ALLOY = createForgeTag("nuggets/swift_alloy");

		private static INamedTag<Item> createTag(String name) {
			return ItemTags.makeWrapperTag(ElementalCraft.MODID + ':' + name);
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
	}

	public static class Blocks {
		public static final INamedTag<Block> LAVASHRINE_LIQUIFIABLES = createTag("lavashrine_liquifiables");
		public static final INamedTag<Block> SMALL_TANK_COMPATIBLES = createTag("small_tank_compatibles");

		public static final INamedTag<Block> PUREROCKS = createTag("purerocks");
		public static final INamedTag<Block> PIPES = createTag("pipes");
		public static final INamedTag<Block> SHRINES = createTag("shrines");

		public static final INamedTag<Block> SHRINES_UPGRADABLES_ACCELERATION = createTag("shrines/upgradables/acceleration");
		public static final INamedTag<Block> SHRINES_UPGRADABLES_RANGE = createTag("shrines/upgradables/range");

		private static INamedTag<Block> createTag(String name) {
			return BlockTags.makeWrapperTag(ElementalCraft.MODID + ':' + name);
		}

		public static ITag<Block> getTag(ResourceLocation loc) {
			ITag<Block> tag = BlockTags.getCollection().get(loc);

			if (tag == null) {
				tag = TagCollectionManager.getManager().getBlockTags().get(loc);
			}
			return tag;
		}
	}
}
