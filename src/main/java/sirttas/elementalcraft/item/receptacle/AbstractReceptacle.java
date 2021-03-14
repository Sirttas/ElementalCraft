package sirttas.elementalcraft.item.receptacle;

import net.minecraft.item.ItemStack;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.property.ECProperties;

public abstract class AbstractReceptacle extends ItemEC {

	protected AbstractReceptacle() {
		super(ECProperties.Items.RECEPTACLE);
	}

	protected AbstractReceptacle(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return Boolean.TRUE.equals(ECConfig.COMMON.receptacleEnchantable.get());
	}
}