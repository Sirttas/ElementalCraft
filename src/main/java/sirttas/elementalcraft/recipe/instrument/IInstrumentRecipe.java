package sirttas.elementalcraft.recipe.instrument;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import sirttas.elementalcraft.api.element.ElementType;

import java.util.List;

public interface IInstrumentRecipe<I extends RecipeInput> extends Recipe<I> {

    List<ElementType> getValidElementTypes();

    int getElementAmount();

    default int getElementAmount(I input) {
        return getElementAmount();
    }

}
