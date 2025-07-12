package sirttas.elementalcraft.api.pureore.factory;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;

import javax.annotation.Nonnull;

public interface IPureOreRecipeFactoryType<C extends RecipeInput, T extends Recipe<C>> {
    @Nonnull
    IPureOreRecipeFactory<C, T> create(@Nonnull RecipeManager recipeManager);

}
