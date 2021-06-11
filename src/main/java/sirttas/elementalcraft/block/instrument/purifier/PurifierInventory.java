package sirttas.elementalcraft.block.instrument.purifier;

import net.minecraft.item.ItemStack;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.inventory.IOInventory;

public class PurifierInventory extends IOInventory {

	public PurifierInventory(Runnable syncCallback) {
		super(syncCallback);
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack stack) {
		return super.canPlaceItem(index, stack) && ElementalCraft.PURE_ORE_MANAGER.isValidOre(stack);
	}

}
