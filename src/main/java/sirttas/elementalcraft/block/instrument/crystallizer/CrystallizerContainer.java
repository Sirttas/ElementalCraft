package sirttas.elementalcraft.block.instrument.crystallizer;

import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.block.instrument.InstrumentContainer;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.tag.ECTags;

public class CrystallizerContainer extends InstrumentContainer {

	public CrystallizerContainer(Runnable syncCallback) {
		super(syncCallback, 12);
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		if (slot == 0) {
			return stack.is(ECTags.Items.INPUT_GEMS);
		} else if (slot == 1) {
			return stack.is(ECTags.Items.ELEMENTAL_CRYSTALS) || stack.is(ECItems.PURE_CRYSTAL.get());
		}
		return stack.is(ECTags.Items.SHARDS);
	}

}
