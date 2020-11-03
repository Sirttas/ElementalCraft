package sirttas.elementalcraft.block.instrument.purifier;

import net.minecraft.item.ItemStack;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.inventory.IOInventory;

public class PurifierInventory extends IOInventory {

	public PurifierInventory(Runnable syncCallback) {
		super(syncCallback);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return super.isItemValidForSlot(index, stack) && ElementalCraft.PURE_ORE_MANAGER.isValidOre(stack);
	}

}
