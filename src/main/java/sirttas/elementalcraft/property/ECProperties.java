package sirttas.elementalcraft.property;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.state.EnumProperty;
import net.minecraftforge.common.ToolType;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraftTab;
import sirttas.elementalcraft.config.ECConfig;

public class ECProperties {

	public static final EnumProperty<ElementType> ELEMENT_TYPE = EnumProperty.create("element_type", ElementType.class);

	public static class Blocks {
		public static final AbstractBlock.Properties DEFAULT_BLOCK_PROPERTIES = AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(2).sound(SoundType.METAL)
				.harvestTool(ToolType.PICKAXE).harvestLevel(1);
		public static final AbstractBlock.Properties BLOCK_NOT_SOLID = AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(2).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE)
				.harvestLevel(1).notSolid();
		public static final AbstractBlock.Properties WHITEROCK = AbstractBlock.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(1.5F, 6.0F);
		public static final AbstractBlock.Properties PUREROCK = AbstractBlock.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(75.0F, 2400.0F);
	}

	public static class Items {
		public static final Item.Properties DEFAULT_ITEM_PROPERTIES = new Item.Properties().group(ElementalCraftTab.tabElementalCraft);
		public static final Item.Properties ITEM_UNSTACKABLE = new Item.Properties().group(ElementalCraftTab.tabElementalCraft).maxStackSize(1);
		public static final Item.Properties RECEPTACLE = new Item.Properties().group(ElementalCraftTab.tabElementalCraft).maxStackSize(1).maxDamage(ECConfig.CONFIG.receptacleDurability.get());
	}
}
