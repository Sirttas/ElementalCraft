package sirttas.elementalcraft.tag;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;

public class ECTags {
	public static class Items {
		public static final Tag<Item> INFUSABLE_SWORDS = createTag("infusable_swords");
		public static final Tag<Item> INFUSABLE_PICKAXES = createTag("infusable_pickaxes");
		public static final Tag<Item> INFUSABLE_AXES = createTag("infusable_axes");
		public static final Tag<Item> INFUSABLE_SHOVELS = createTag("infusable_shovels");
		public static final Tag<Item> INFUSABLE_HOES = createTag("infusable_hoes");
		public static final Tag<Item> INFUSABLE_SHILDS = createTag("infusable_shields");
		public static final Tag<Item> INFUSABLE_BOWS = createTag("infusable_bows");
		public static final Tag<Item> INFUSABLE_CROSSBOWS = createTag("infusable_crossbows");
		public static final Tag<Item> INFUSABLE_HELMETS = createTag("infusable_helmets");
		public static final Tag<Item> INFUSABLE_CHESTPLATES = createTag("infusable_chestplates");
		public static final Tag<Item> INFUSABLE_LEGGINGS = createTag("infusable_leggings");
		public static final Tag<Item> INFUSABLE_BOOTS = createTag("infusable_boots");

		public static final Tag<Item> LAVASHRINE_LIQUIFIABLES = createTag("lavashrine_liquifiables");

		private static Tag<Item> createTag(String name) {
			return new ItemTags.Wrapper(new ResourceLocation(ElementalCraft.MODID, name));
		}
	}

	public static class Blocks {
		public static final Tag<Block> LAVASHRINE_LIQUIFIABLES = createTag("lavashrine_liquifiables");

		private static Tag<Block> createTag(String name) {
			return new BlockTags.Wrapper(new ResourceLocation(ElementalCraft.MODID, name));
		}
	}
}
