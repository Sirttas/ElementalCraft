package sirttas.elementalcraft.recipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("DataFlowIssue")
public interface RuntimeRecipe<I extends RecipeInput> extends Recipe<@NotNull I> {

    @Override
    default boolean showNotification() {
        return false;
    }

    @Override
    default @NotNull String group() {
        return "";
    }

    @Override
    default @NotNull RecipeSerializer<? extends @NotNull Recipe<@NotNull I>> getSerializer() {
        return null;
    }

    @Override
    default @NotNull RecipeType<? extends @NotNull Recipe<@NotNull I>> getType() {
        return null;
    }

    @Override
    default @NotNull RecipeBookCategory recipeBookCategory() {
        return null;
    }

}
