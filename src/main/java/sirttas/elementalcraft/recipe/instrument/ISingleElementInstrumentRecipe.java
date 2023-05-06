package sirttas.elementalcraft.recipe.instrument;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.block.instrument.IInstrument;

import java.util.List;

public interface ISingleElementInstrumentRecipe<T extends IInstrument> extends IInstrumentRecipe<T>, IElementTypeProvider {

    @Override
    default List<ElementType> getValidElementTypes() {
        return List.of(getElementType());
    }
}
