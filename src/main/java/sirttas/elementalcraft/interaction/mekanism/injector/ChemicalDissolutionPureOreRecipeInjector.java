package sirttas.elementalcraft.interaction.mekanism.injector;

import mekanism.api.chemical.gas.GasStack;
import mekanism.api.inventory.IgnoredIInventory;
import mekanism.api.recipes.ChemicalDissolutionRecipe;
import mekanism.api.recipes.inputs.ItemStackIngredient;
import mekanism.common.recipe.impl.ChemicalDissolutionIRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import sirttas.elementalcraft.ElementalCraft;

public class ChemicalDissolutionPureOreRecipeInjector extends AbstractMekanismPureOreRecipeInjector<IgnoredIInventory, ChemicalDissolutionRecipe> {

	public ChemicalDissolutionPureOreRecipeInjector(IRecipeType<ChemicalDissolutionRecipe> recipeType) {
		super(recipeType);
	}

	@Override
	public ChemicalDissolutionRecipe build(ChemicalDissolutionRecipe recipe, Ingredient ingredient) {
		return new ChemicalDissolutionIRecipe(ElementalCraft.createRL(buildRecipeId(recipe.getId())), ItemStackIngredient.from(ingredient), recipe.getGasInput(),
				tweakOutput(recipe.getOutput(ItemStack.EMPTY, GasStack.EMPTY).getChemicalStack()));
	}

	@Override
	public boolean filter(ChemicalDissolutionRecipe recipe, ItemStack stack) {
		return recipe.getItemInput().test(stack);
	}

}
