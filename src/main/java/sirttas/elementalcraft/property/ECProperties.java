package sirttas.elementalcraft.property;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.state.EnumProperty;
import net.minecraftforge.common.ToolType;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraftTab;

public class ECProperties {

	public static final Block.Properties DEFAULT_BLOCK_PROPERTIES = Block.Properties.create(Material.IRON).hardnessAndResistance(2).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1);
	public static final Block.Properties BLOCK_NOT_SOLID = Block.Properties.create(Material.IRON).hardnessAndResistance(2).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1).notSolid();

	public static final Item.Properties DEFAULT_ITEM_PROPERTIES = new Item.Properties().group(ElementalCraftTab.tabElementalCraft);
	public static final Item.Properties ITEM_UNSTACKABLE = new Item.Properties().group(ElementalCraftTab.tabElementalCraft).maxStackSize(1);

	public static final EnumProperty<ElementType> ELEMENT_TYPE = EnumProperty.create("element_type", ElementType.class);
}
