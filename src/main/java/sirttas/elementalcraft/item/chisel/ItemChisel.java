package sirttas.elementalcraft.item.chisel;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import sirttas.elementalcraft.ElementalCraftTab;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.ItemEC;

public class ItemChisel extends ItemEC {

	public static final String NAME = "chisel";

	public ItemChisel() {
		super(new Item.Properties().group(ElementalCraftTab.TAB).maxStackSize(1).maxDamage(ECConfig.COMMON.chiselDurability.get()));
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return repair.getItem() == ECItems.SWIFT_ALLOY_INGOT;
	}
}
