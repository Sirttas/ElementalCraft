package sirttas.elementalcraft.recipe.input;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;

public record SingleItemSingleElementRecipeInput(
        ItemStack item,
        int limit,
        ElementType elementType,
        int elementAmount
) implements ElementRecipeInput {

    public SingleItemSingleElementRecipeInput(ItemStack item, ElementType elementType, int elementAmount) {
        this(item, 64, elementType, elementAmount);
    }

    @Override
    public int getElementAmount(@NotNull ElementType elementType) {
        if (elementType != this.elementType) {
            return 0;
        }
        return elementAmount;
    }

    @NotNull
    @Override
    public ElementType getElementType() {
        return elementType;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        if (slot != 0) {
            throw new IllegalArgumentException("No item for index " + slot);
        } else {
            return this.item;
        }
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public int getItemLimit(int slot) {
        return limit;
    }
}
