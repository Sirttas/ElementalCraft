package sirttas.elementalcraft.item;

import java.util.List;

import mezz.jei.color.ColorGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.property.ECProperties;

public class ItemEC extends Item implements IItemEC {

	public ItemEC() {
		this(ECProperties.Items.DEFAULT_ITEM_PROPERTIES);
	}

	public ItemEC(Properties properties) {
		super(properties);
	}

	private boolean glint = false;

	/**
	 * Returns true if this item has an enchantment glint. By default, this
	 * returns <code>stack.isItemEnchanted()</code>, but other items can
	 * override it (for instance, written books always return true).
	 * 
	 * Note that if you override this method, you generally want to also call
	 * the super version (on {@link Item}) to get the glint for enchanted items.
	 * Of course, that is unnecessary if the overwritten version always returns
	 * true.
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return super.hasEffect(stack) || glint;
	}

	public ItemEC setEffect(boolean glint) {
		this.glint = glint;
		return this;
	}

	@OnlyIn(Dist.CLIENT)
	public static final int lookupColor(ItemStack stack) {
		List<Integer> colors = ColorGetter.getColors(stack, 2);

		if (colors != null && !colors.isEmpty()) {
			return colors.get(0);
		}
		return -1;
	}
}
