package sirttas.elementalcraft.item.receptacle;

import net.minecraft.item.ItemStack;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.property.ECProperties;

public class AbstractReceptacle extends ItemEC {

	public AbstractReceptacle() {
		super(ECProperties.Items.RECEPTACLE);
	}

	public AbstractReceptacle(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return Boolean.TRUE.equals(ECConfig.COMMON.receptacleEnchantable.get());
	}
}