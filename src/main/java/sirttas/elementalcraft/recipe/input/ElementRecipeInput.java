package sirttas.elementalcraft.recipe.input;

import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;

public interface ElementRecipeInput extends ECRecipeInput, IElementTypeProvider {
    int getElementAmount(@NotNull ElementType elementType);
}
