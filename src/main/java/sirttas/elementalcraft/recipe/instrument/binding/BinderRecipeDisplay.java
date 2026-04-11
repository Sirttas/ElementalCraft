package sirttas.elementalcraft.recipe.instrument.binding;

import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;

import java.util.List;

public record BinderRecipeDisplay(
        ElementType elementType,
        int elementAmount,
        List<SlotDisplay> ingredients,
        SlotDisplay result,
        SlotDisplay craftingStation
) implements RecipeDisplay {

    @Override
    @NotNull
    public Type<? extends @NotNull RecipeDisplay> type() {
        return null;
    }
}
