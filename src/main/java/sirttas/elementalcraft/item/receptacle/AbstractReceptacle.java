package sirttas.elementalcraft.item.receptacle;

import net.minecraft.item.ItemStack;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.property.ECProperties;

public class AbstractReceptacle extends ItemEC {

	public AbstractReceptacle() {
		super(ECProperties.Items.RECEPTACLE);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return Boolean.TRUE.equals(ECConfig.COMMON.receptacleEnchantable.get());
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return repair.getItem() == ECItems.swiftAlloyIngot;
	}

}