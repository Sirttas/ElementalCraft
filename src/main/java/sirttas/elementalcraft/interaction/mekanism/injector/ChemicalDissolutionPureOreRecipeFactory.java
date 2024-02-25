package sirttas.elementalcraft.interaction.mekanism.injector;

import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ChemicalDissolutionRecipe;
import mekanism.api.recipes.basic.BasicChemicalDissolutionRecipe;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class ChemicalDissolutionPureOreRecipeFactory extends AbstractMekanismPureOreRecipeFactory<ChemicalDissolutionRecipe> {

	public ChemicalDissolutionPureOreRecipeFactory(@Nonnull RecipeManager recipeManager, @Nonnull RecipeTypeRegistryObject<ChemicalDissolutionRecipe, InputRecipeCache.ItemChemical<Gas, GasStack, ChemicalDissolutionRecipe>> recipeType) {
		super(recipeManager, recipeType);
	}

	@Override
	public ChemicalDissolutionRecipe create(@NotNull RegistryAccess registry, @NotNull ChemicalDissolutionRecipe recipe, @NotNull Ingredient ingredient) {
		return new BasicChemicalDissolutionRecipe(getInput(ingredient, recipe.getItemInput()), tweakOutput(recipe.getGasInput()), tweakOutput(recipe.getOutput(ItemStack.EMPTY, GasStack.EMPTY).getChemicalStack()));
	}

	@Override
	public boolean filter(RecipeHolder<ChemicalDissolutionRecipe> recipe, ItemStack stack) {
		return recipe.value().getItemInput().test(stack) && super.filter(recipe, stack);
	}


}
