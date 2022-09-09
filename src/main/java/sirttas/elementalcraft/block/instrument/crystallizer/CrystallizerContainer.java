package sirttas.elementalcraft.block.instrument.crystallizer;

import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.block.instrument.InstrumentContainer;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;

public class CrystallizerContainer extends InstrumentContainer {

	private final CrystallizerBlockEntity crystallizer;

	public CrystallizerContainer(CrystallizerBlockEntity crystallizer) {
		super(crystallizer::setChanged, 12);
		this.crystallizer = crystallizer;
	}

	@Override
	public boolean canPlaceItem(int slot, @Nonnull ItemStack stack) {
		if (slot == 0) {
			return stack.is(ECTags.Items.INPUT_GEMS);
		} else if (slot == 1) {
			return stack.is(ECTags.Items.ELEMENTAL_CRYSTALS) || stack.is(ECItems.PURE_CRYSTAL.get());
		}
		return crystallizer.isValidShard(stack);
	}

}
