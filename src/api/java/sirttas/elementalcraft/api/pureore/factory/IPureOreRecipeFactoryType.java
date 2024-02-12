package sirttas.elementalcraft.api.pureore.factory;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;

import javax.annotation.Nonnull;

public interface IPureOreRecipeFactoryType<C extends Container, T extends Recipe<C>> {
    @Nonnull
    IPureOreRecipeFactory<C, T> create(@Nonnull RecipeManager recipeManager);

}
