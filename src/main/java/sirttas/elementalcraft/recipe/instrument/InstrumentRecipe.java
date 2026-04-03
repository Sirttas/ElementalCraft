package sirttas.elementalcraft.recipe.instrument;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;

import java.util.List;

public interface InstrumentRecipe<I extends RecipeInput> extends Recipe<@NotNull I> {

    List<ElementType> getValidElementTypes();

    int getElementAmount();

    default int getElementAmount(I input) {
        return getElementAmount();
    }

}
