package sirttas.elementalcraft.interaction.ie.injector;

import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.crafting.IERecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class CrusherPureOreRecipeInjector extends AbstractIEPureOreRecipeInjector<CrusherRecipe> {

    public CrusherPureOreRecipeInjector() {
        super(IERecipeTypes.CRUSHER, CrusherRecipe.class);
    }

    @Override
    public CrusherRecipe buildIERecipe(CrusherRecipe recipe, Ingredient ingredient) {
        var newRecipe = new CrusherRecipe(buildRecipeId(recipe.getId()), recipe.output, ingredient, recipe.getTotalProcessEnergy());

        recipe.secondaryOutputs.forEach(newRecipe::addToSecondaryOutput);
        return newRecipe;
    }

    @Override
    public boolean filterIERecipe(CrusherRecipe recipe, ItemStack stack) {
        return recipe.input.test(stack);
    }
}
