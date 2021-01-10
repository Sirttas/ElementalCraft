package sirttas.elementalcraft.block.instrument.crystallizer;

import net.minecraft.item.ItemStack;
import sirttas.elementalcraft.block.instrument.InstrumentInventory;
import sirttas.elementalcraft.tag.ECTags;

public class CrystallizerInventory extends InstrumentInventory {

	public CrystallizerInventory(Runnable syncCallback) {
		super(syncCallback, 12);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) { // TODO use tags built from recipes
		if (slot == 0) {
			return ECTags.Items.INPUT_GEMS.contains(stack.getItem());
		} else if (slot == 1) {
			return ECTags.Items.ELEMENTAL_CRYSTALS.contains(stack.getItem());
		}
		return ECTags.Items.SHARDS.contains(stack.getItem());
	}

}
