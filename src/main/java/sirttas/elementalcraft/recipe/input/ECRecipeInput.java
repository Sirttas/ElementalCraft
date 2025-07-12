package sirttas.elementalcraft.recipe.input;

import net.minecraft.world.item.crafting.RecipeInput;

public interface ECRecipeInput extends RecipeInput {
    default int getItemLimit(int slot) {
        return 64;
    }
}
