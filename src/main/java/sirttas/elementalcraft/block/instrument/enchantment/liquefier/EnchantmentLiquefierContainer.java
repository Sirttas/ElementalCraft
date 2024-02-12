package sirttas.elementalcraft.block.instrument.enchantment.liquefier;

import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.block.instrument.InstrumentContainer;

import javax.annotation.Nonnull;

public class EnchantmentLiquefierContainer extends InstrumentContainer {

	public EnchantmentLiquefierContainer(EnchantmentLiquefierBlockEntity enchantmentLiquefier) {
		super(enchantmentLiquefier::setChanged, 2);
	}

	@Override
	public boolean canPlaceItem(int slot, @Nonnull ItemStack stack) {
		if (slot == 0) {
			return EnchantmentLiquefierHelper.isValidInput(stack);
		} else if (slot == 1) {
			return EnchantmentLiquefierHelper.isValidOutput(stack);
		}
		return super.canPlaceItem(slot, stack);
	}

}
