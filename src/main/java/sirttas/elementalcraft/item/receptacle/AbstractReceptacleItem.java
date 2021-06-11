package sirttas.elementalcraft.item.receptacle;

import net.minecraft.item.ItemStack;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.property.ECProperties;

public abstract class AbstractReceptacleItem extends ECItem {

	protected AbstractReceptacleItem() {
		super(ECProperties.Items.RECEPTACLE);
	}

	protected AbstractReceptacleItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return Boolean.TRUE.equals(ECConfig.COMMON.receptacleEnchantable.get());
	}
}