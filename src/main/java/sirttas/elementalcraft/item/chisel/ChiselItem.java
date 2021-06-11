package sirttas.elementalcraft.item.chisel;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import sirttas.elementalcraft.ElementalCraftTab;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.ECItem;

public class ChiselItem extends ECItem {

	public static final String NAME = "chisel";

	public ChiselItem() {
		super(new Item.Properties().tab(ElementalCraftTab.TAB).stacksTo(1).durability(ECConfig.COMMON.chiselDurability.get()));
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		return repair.getItem() == ECItems.SWIFT_ALLOY_INGOT;
	}
}
