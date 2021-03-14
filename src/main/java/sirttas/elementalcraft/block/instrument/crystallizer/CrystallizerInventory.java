package sirttas.elementalcraft.block.instrument.crystallizer;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import sirttas.elementalcraft.block.instrument.InstrumentInventory;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.tag.ECTags;

public class CrystallizerInventory extends InstrumentInventory {

	public CrystallizerInventory(Runnable syncCallback) {
		super(syncCallback, 12);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) { // TODO use tags built from recipes
		Item item = stack.getItem();

		if (slot == 0) {
			return ECTags.Items.INPUT_GEMS.contains(item);
		} else if (slot == 1) {
			return ECTags.Items.ELEMENTAL_CRYSTALS.contains(item) || ECItems.PURE_CRYSTAL == item;
		}
		return ECTags.Items.SHARDS.contains(item);
	}

}
