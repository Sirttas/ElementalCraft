package sirttas.elementalcraft.recipe.instrument;

import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.block.tile.ICraftingTile;
import sirttas.elementalcraft.recipe.IInventoryTileRecipe;

public interface IInstrumentRecipe<T extends ICraftingTile> extends IInventoryTileRecipe<T>, IElementTypeProvider {

}
