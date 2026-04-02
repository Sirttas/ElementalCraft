package sirttas.elementalcraft.recipe.instrument;

import net.minecraft.world.item.crafting.RecipeInput;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;

import java.util.List;

public interface SingleElementInstrumentRecipe<I extends RecipeInput> extends IInstrumentRecipe<I>, IElementTypeProvider {

    @Override
    default List<ElementType> getValidElementTypes() {
        return List.of(getElementType());
    }
}
