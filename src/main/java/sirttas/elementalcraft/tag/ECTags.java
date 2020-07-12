package sirttas.elementalcraft.tag;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
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

		public static final INamedTag<Item> PUREROCKS = createTag("purerocks");

		private static INamedTag<Item> createTag(String name) {
			return ItemTags.makeWrapperTag(ElementalCraft.MODID + ':' + name);
		}
	}

	public static class Blocks {
		public static final INamedTag<Block> LAVASHRINE_LIQUIFIABLES = createTag("lavashrine_liquifiables");
		public static final INamedTag<Block> PUREROCKS = createTag("purerocks");

		private static INamedTag<Block> createTag(String name) {
			return BlockTags.makeWrapperTag(ElementalCraft.MODID + ':' + name);
		}
	}
}
