package sirttas.elementalcraft.block.instrument.io.purifier;

import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.container.IOContainer;

import javax.annotation.Nonnull;

public class PurifierContainer extends IOContainer {

	public PurifierContainer(Runnable syncCallback) {
		super(syncCallback);
	}

	@Override
	public boolean canPlaceItem(int index, @Nonnull ItemStack stack) {
		return super.canPlaceItem(index, stack) && ElementalCraft.PURE_ORE_MANAGER.isValidOre(stack);
	}

}
