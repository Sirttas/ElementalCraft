package sirttas.elementalcraft.interaction.ie.injector;

import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.crafting.IERecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;

import javax.annotation.Nonnull;

public class CrusherPureOreRecipeFactory extends AbstractIEPureOreRecipeFactory<CrusherRecipe> {

    public CrusherPureOreRecipeFactory(@Nonnull RecipeManager recipeManager) {
        super(recipeManager, IERecipeTypes.CRUSHER.get(), CrusherRecipe.class);
    }

    @Override
    public CrusherRecipe buildIERecipe(CrusherRecipe recipe, Ingredient ingredient) {
        return new CrusherRecipe(recipe.output, ingredient, recipe.getTotalProcessEnergy(), recipe.secondaryOutputs);
    }

    @Override
    public boolean filterIERecipe(CrusherRecipe recipe, ItemStack stack) {
        return recipe.input.test(stack);
    }
}
