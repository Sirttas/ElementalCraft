package sirttas.elementalcraft.property;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import sirttas.elementalcraft.ElementalCraftTab;
import sirttas.elementalcraft.config.ECConfig;

public class ECProperties {

	private ECProperties() {}
	
	public static class Blocks {
		public static final AbstractBlock.Properties DEFAULT_BLOCK_PROPERTIES = AbstractBlock.Properties.of(Material.METAL).strength(2).sound(SoundType.METAL)
				.harvestTool(ToolType.PICKAXE).harvestLevel(1);
		public static final AbstractBlock.Properties BLOCK_NOT_SOLID = AbstractBlock.Properties.of(Material.METAL).strength(2).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE)
				.harvestLevel(1).noOcclusion();
		public static final AbstractBlock.Properties WHITEROCK = AbstractBlock.Properties.of(Material.STONE, MaterialColor.QUARTZ).strength(1.5F, 6.0F);
		public static final AbstractBlock.Properties PUREROCK = AbstractBlock.Properties.of(Material.STONE, MaterialColor.QUARTZ).strength(75.0F, 2400.0F);
		
		private Blocks() {}
	}

	public static class Items {
		public static final Item.Properties DEFAULT_ITEM_PROPERTIES = new Item.Properties().tab(ElementalCraftTab.TAB);
		public static final Item.Properties ITEM_UNSTACKABLE = new Item.Properties().tab(ElementalCraftTab.TAB).stacksTo(1);
		public static final Item.Properties RECEPTACLE = new Item.Properties().tab(ElementalCraftTab.TAB).durability(ECConfig.COMMON.receptacleDurability.get());
		public static final Item.Properties RECEPTACLE_IMPROVED = new Item.Properties().tab(ElementalCraftTab.TAB).durability(ECConfig.COMMON.improvedReceptacleDurability.get());
		public static final Item.Properties FIREITE = new Item.Properties().tab(ElementalCraftTab.TAB).fireResistant();
		public static final Item.Properties LENSE = new Item.Properties().tab(ElementalCraftTab.TAB).durability(1000);
		
		private Items() {}
	}
}
