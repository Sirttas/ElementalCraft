package sirttas.elementalcraft.recipe.instrument;

import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.recipe.IInventoryBlockEntityRecipe;

public interface IInstrumentRecipe<T extends IInstrument> extends IInventoryBlockEntityRecipe<T>, IElementTypeProvider {
	
}
