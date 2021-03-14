package sirttas.elementalcraft.recipe.instrument;

import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.recipe.IInventoryTileRecipe;

public interface IInstrumentRecipe<T extends IInstrument> extends IInventoryTileRecipe<T>, IElementTypeProvider {

	@Override
	default void process(T instrument) {
		instrument.getInventory().setInventorySlotContents(0, this.getCraftingResult(instrument));
	}
	
}
