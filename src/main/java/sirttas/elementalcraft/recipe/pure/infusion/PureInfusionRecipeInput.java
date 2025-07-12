package sirttas.elementalcraft.recipe.pure.infusion;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.recipe.input.ECRecipeInput;
import sirttas.elementalcraft.recipe.input.SingleItemSingleElementRecipeInput;

import java.util.List;
import java.util.Map;

public record PureInfusionRecipeInput(
        Map<ElementType, SingleItemSingleElementRecipeInput> pedestalInputs,
        ItemStack pureInfuserInput
) implements ECRecipeInput {

    @Override
    public @NotNull ItemStack getItem(int slot) {
        if (slot != 0) {
            throw new IllegalArgumentException("No item for index " + slot);
        } else {
            return this.pureInfuserInput;
        }
    }

    public @NotNull ItemStack getStackInPedestal(ElementType elementType) {
        var pedestalInput = pedestalInputs.get(elementType);

        return pedestalInput == null ? ItemStack.EMPTY : pedestalInput.item();
    }

    public List<ItemStack> getStacksInPedestals() {
        return pedestalInputs.values().stream()
                .map(SingleItemSingleElementRecipeInput::item)
                .toList();
    }

    @Override
    public int size() {
        return 1;
    }
}
