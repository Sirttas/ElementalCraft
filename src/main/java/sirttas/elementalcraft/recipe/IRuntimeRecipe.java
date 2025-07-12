package sirttas.elementalcraft.recipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public interface IRuntimeRecipe<I extends RecipeInput> extends Recipe<I> {

    default RecipeType<?> getType() {
        return null;
    }
    default RecipeSerializer<?> getSerializer() {
        return null;
    }
}
