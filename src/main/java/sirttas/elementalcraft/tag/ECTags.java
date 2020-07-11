package sirttas.elementalcraft.tag;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import sirttas.elementalcraft.ElementalCraft;

public class ECTags {
	public static class Items {
		public static final INamedTag<Item> INFUSABLE_SWORDS = createTag("infusable_swords");
		public static final INamedTag<Item> INFUSABLE_PICKAXES = createTag("infusable_pickaxes");
		public static final INamedTag<Item> INFUSABLE_AXES = createTag("infusable_axes");
		public static final INamedTag<Item> INFUSABLE_SHOVELS = createTag("infusable_shovels");
		public static final INamedTag<Item> INFUSABLE_HOES = createTag("infusable_hoes");
		public static final INamedTag<Item> INFUSABLE_SHILDS = createTag("infusable_shields");
		public static final INamedTag<Item> INFUSABLE_BOWS = createTag("infusable_bows");
		public static final INamedTag<Item> INFUSABLE_CROSSBOWS = createTag("infusable_crossbows");
		public static final INamedTag<Item> INFUSABLE_HELMETS = createTag("infusable_helmets");
		public static final INamedTag<Item> INFUSABLE_CHESTPLATES = createTag("infusable_chestplates");
		public static final INamedTag<Item> INFUSABLE_LEGGINGS = createTag("infusable_leggings");
		public static final INamedTag<Item> INFUSABLE_BOOTS = createTag("infusable_boots");

		public static final INamedTag<Item> LAVASHRINE_LIQUIFIABLES = createTag("lavashrine_liquifiables");

		private static INamedTag<Item> createTag(String name) {
			return ItemTags.makeWrapperTag(ElementalCraft.MODID + ':' + name);
		}
	}

	public static class Blocks {
		public static final INamedTag<Block> LAVASHRINE_LIQUIFIABLES = createTag("lavashrine_liquifiables");

		private static INamedTag<Block> createTag(String name) {
			return BlockTags.makeWrapperTag(ElementalCraft.MODID + ':' + name);
		}
	}
}
