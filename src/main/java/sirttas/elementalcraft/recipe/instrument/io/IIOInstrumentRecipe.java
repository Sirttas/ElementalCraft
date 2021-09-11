package sirttas.elementalcraft.recipe.instrument.io;

import java.util.Random;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public interface IIOInstrumentRecipe<T extends IInstrument> extends IInstrumentRecipe<T> {

	default Random getRand(T instrument) {
		return new Random();
	}
	
	default int getLuck(T instrument) {
		return 0;
	}
	
	default boolean matches(ItemStack input) {
		return true;
	}
	
	@Override
	default boolean matches(T instrument) {
		ItemStack craftingResult = assemble(instrument);

		return instrument.getItemHandler().map(inv -> {
			ItemStack output = inv.getStackInSlot(1);

			return instrument.getContainerElementType() == this.getElementType() && matches(inv.getStackInSlot(0))
					&& (output.isEmpty() || (ItemHandlerHelper.canItemStacksStack(output, craftingResult) && output.getCount() + craftingResult.getCount() <= inv.getSlotLimit(1)));
		}).orElse(false);
	}	
}
