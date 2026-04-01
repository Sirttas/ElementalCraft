package sirttas.elementalcraft.recipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

public interface IRuntimeRecipe<I extends RecipeInput> extends Recipe<@NotNull I> {

    @Override
    default boolean showNotification() {
        return false;
    }

    @Override
    default String group() {
        return "";
    }

    @Override
    default RecipeSerializer<? extends Recipe<I>> getSerializer() {
        return null;
    }

    @Override
    default RecipeType<? extends Recipe<I>> getType() {
        return null;
    }

    @Override
    default RecipeBookCategory recipeBookCategory() {
        return null;
    }

}
