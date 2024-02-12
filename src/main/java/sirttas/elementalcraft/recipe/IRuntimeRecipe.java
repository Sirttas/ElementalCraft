package sirttas.elementalcraft.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public interface IRuntimeRecipe<C extends Container> extends Recipe<C> {

    default RecipeType<?> getType() {
        return null;
    }
    default RecipeSerializer<?> getSerializer() {
        return null;
    }
}
