package sirttas.elementalcraft.interaction.mekanism.injector;

import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ChemicalDissolutionRecipe;
import mekanism.common.recipe.impl.ChemicalDissolutionIRecipe;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class ChemicalDissolutionPureOreRecipeInjector extends AbstractMekanismPureOreRecipeInjector<ChemicalDissolutionRecipe> {

	public ChemicalDissolutionPureOreRecipeInjector(RecipeTypeRegistryObject<ChemicalDissolutionRecipe, InputRecipeCache.ItemChemical<Gas, GasStack, ChemicalDissolutionRecipe>> recipeType) {
		super(recipeType);
	}

	@Override
	public ChemicalDissolutionRecipe build(ChemicalDissolutionRecipe recipe, Ingredient ingredient) {
		return new ChemicalDissolutionIRecipe(buildRecipeId(recipe.getId()), getInput(ingredient, recipe.getItemInput()), tweakOutput(recipe.getGasInput()),
				tweakOutput(recipe.getOutput(ItemStack.EMPTY, GasStack.EMPTY).getChemicalStack()));
	}

	@Override
	public boolean filter(ChemicalDissolutionRecipe recipe, ItemStack stack) {
		return recipe.getItemInput().test(stack) && super.filter(recipe, stack);
	}

}
