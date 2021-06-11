package sirttas.elementalcraft.block.instrument.inscriber;

import net.minecraft.item.ItemStack;
import sirttas.elementalcraft.block.instrument.InstrumentInventory;
import sirttas.elementalcraft.tag.ECTags;

public class InscriberInventory extends InstrumentInventory {

	public InscriberInventory(Runnable syncCallback) {
		super(syncCallback, 4);
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return slot != 0 || ECTags.Items.RUNE_SLATES.contains(stack.getItem());
	}

}
