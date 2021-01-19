package sirttas.elementalcraft.block.instrument;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.block.tile.ICraftingTile;

public interface IInstrument extends ICraftingTile, IElementTypeProvider {

	IElementStorage getTank();

	ElementType getTankElementType();
}
