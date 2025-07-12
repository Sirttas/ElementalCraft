package sirttas.elementalcraft.recipe.melting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.recipe.input.ECRecipeInput;

public record MeltingRecipeInput(
        BlockState state,
        int elementAmount,
        int elementConsumption
) implements ECRecipeInput {

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
