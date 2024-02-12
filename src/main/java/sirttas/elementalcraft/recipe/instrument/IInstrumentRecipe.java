package sirttas.elementalcraft.recipe.instrument;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.recipe.IContainerBlockEntityRecipe;

import java.util.List;

public interface IInstrumentRecipe<T extends IInstrument> extends IContainerBlockEntityRecipe<T> {

    List<ElementType> getValidElementTypes();

    int getElementAmount();

    default int getElementAmount(T instrument) {
        return getElementAmount();
    }

}
