package sirttas.elementalcraft.block.instrument.inscriber;

import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.block.instrument.InstrumentContainer;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;

public class InscriberContainer extends InstrumentContainer {

	public InscriberContainer(Runnable syncCallback) {
		super(syncCallback, 4);
	}

	@Override
	public boolean canPlaceItem(int slot, @Nonnull ItemStack stack) {
		return slot != 0 || ECTags.Items.RUNE_SLATES.contains(stack.getItem());
	}

}
