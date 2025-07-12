package sirttas.elementalcraft.recipe.source.breeding;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.recipe.input.ECRecipeInput;
import sirttas.elementalcraft.recipe.input.SingleItemSingleElementRecipeInput;

import java.util.List;

public record SourceBreedingRecipeInput(
        ItemStack seed,
        ElementType elementType,
        List<SingleItemSingleElementRecipeInput> pedestalInputs
) implements ECRecipeInput, IElementTypeProvider {
    @Override
    public @NotNull ItemStack getItem(int slot) {
        if (slot != 0) {
            throw new IllegalArgumentException("No item for index " + slot);
        } else {
            return this.seed;
        }
    }

    @Override
    public int size() {
        return 1;
    }

    @NotNull
    @Override
    public ElementType getElementType() {
        return elementType;
    }
}
