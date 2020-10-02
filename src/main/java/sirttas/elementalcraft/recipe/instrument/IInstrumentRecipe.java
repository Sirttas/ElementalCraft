package sirttas.elementalcraft.recipe.instrument;

import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.recipe.IInventoryTileRecipe;

public interface IInstrumentRecipe<T extends IInstrument> extends IInventoryTileRecipe<T> {

	ElementType getElementType();
}
