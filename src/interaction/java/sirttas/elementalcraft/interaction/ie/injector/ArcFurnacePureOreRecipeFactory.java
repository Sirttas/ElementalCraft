package sirttas.elementalcraft.interaction.ie.injector;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.IERecipeTypes;
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;

import javax.annotation.Nonnull;

public class ArcFurnacePureOreRecipeFactory extends AbstractIEPureOreRecipeFactory<ArcFurnaceRecipe> {

    public ArcFurnacePureOreRecipeFactory(@Nonnull RecipeManager recipeManager) {
        super(recipeManager, IERecipeTypes.ARC_FURNACE.get(), ArcFurnaceRecipe.class);
    }

    @Override
    public ArcFurnaceRecipe buildIERecipe(ArcFurnaceRecipe recipe, Ingredient ingredient) {
        return new ArcFurnaceRecipe(
                recipe.output,
                recipe.slag,
                recipe.secondaryOutputs,
                recipe.getTotalProcessTime(),
                recipe.getTotalProcessEnergy(),
                new IngredientWithSize(ingredient, recipe.input.getCount()),
                recipe.additives
        );
    }

    @Override
    public boolean filterIERecipe(ArcFurnaceRecipe recipe, ItemStack stack) {
        return recipe.input.test(stack);
    }
}
