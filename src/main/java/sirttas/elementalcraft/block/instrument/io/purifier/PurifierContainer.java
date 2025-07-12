package sirttas.elementalcraft.block.instrument.io.purifier;

import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.container.IOContainer;
import sirttas.elementalcraft.pureore.PureOreManager;

import javax.annotation.Nonnull;

public class PurifierContainer extends IOContainer {

	public PurifierContainer(Runnable syncCallback) {
		super(syncCallback);
	}

	@Override
	public boolean canPlaceItem(int index, @Nonnull ItemStack stack) {
		return super.canPlaceItem(index, stack) && PureOreManager.getInstance().isValidOre(stack);
	}

}
