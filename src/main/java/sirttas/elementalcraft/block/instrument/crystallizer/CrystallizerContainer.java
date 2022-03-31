package sirttas.elementalcraft.block.instrument.crystallizer;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.block.instrument.InstrumentContainer;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.tag.ECTags;

public class CrystallizerContainer extends InstrumentContainer {

	public CrystallizerContainer(Runnable syncCallback) {
		super(syncCallback, 12);
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) { // TODO use tags built from recipes
		Item item = stack.getItem();

		if (slot == 0) {
			return stack.is(ECTags.Items.INPUT_GEMS);
		} else if (slot == 1) {
			return stack.is(ECTags.Items.ELEMENTAL_CRYSTALS) || ECItems.PURE_CRYSTAL == item;
		}
		return stack.is(ECTags.Items.SHARDS);
	}

}
