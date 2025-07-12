package sirttas.elementalcraft.recipe.input;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;

import java.util.List;

public record MultipleItemsSingleElementRecipeInput(
        List<ItemStack> stacks,
        ElementType elementType,
        int elementAmount
) implements ElementRecipeInput {

    public MultipleItemsSingleElementRecipeInput(List<ItemStack> stacks, ElementType elementType, int elementAmount) {
        this.stacks = stacks.stream()
                .filter(s -> !s.isEmpty())
                .toList();
        this.elementType = elementType;
        this.elementAmount = elementAmount;
    }

    @Override
    public int getElementAmount(@NotNull ElementType elementType) {
        if (elementType != this.elementType) {
            return 0;
        }
        return elementAmount;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return stacks.get(slot);
    }

    @Override
    public int size() {
        return stacks.size();
    }

    @NotNull
    @Override
    public ElementType getElementType() {
        return elementType;
    }

    @Override
    public int getItemLimit(int slot) {
        return 1;
    }

    public SingleItemSingleElementRecipeInput singleItem() {
        if (size() != 1) {
            throw new IllegalStateException("Cannot convert to single item recipe input, it doesn't have exactly one item!");
        }
        return new SingleItemSingleElementRecipeInput(stacks.getFirst(), 1, elementType, elementAmount);
    }
}
