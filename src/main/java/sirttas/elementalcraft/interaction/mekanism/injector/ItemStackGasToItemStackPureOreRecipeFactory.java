package sirttas.elementalcraft.interaction.mekanism.injector;

import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class ItemStackGasToItemStackPureOreRecipeFactory<T extends ItemStackGasToItemStackRecipe> extends AbstractMekanismPureOreRecipeFactory<T> {

	private final Factory<T> factory;

	public ItemStackGasToItemStackPureOreRecipeFactory(@Nonnull RecipeManager recipeManager, @Nonnull RecipeTypeRegistryObject<T, InputRecipeCache.ItemChemical<Gas, GasStack, ItemStackGasToItemStackRecipe>> recipeType, @Nonnull Factory<T> factory) {
		super(recipeManager, recipeType);
		this.factory = factory;
	}

	@Override
	public T create(@NotNull RegistryAccess registry, @NotNull T recipe, @NotNull Ingredient ingredient) {
		return factory.create(getInput(ingredient, recipe.getItemInput()), tweakOutput(recipe.getChemicalInput()), getRecipeOutput(registry, recipe));
	}

	@Override
	public ItemStack getRecipeOutput(@NotNull RegistryAccess registry, @NotNull T recipe) {
		return tweakOutput(recipe.getOutput(ItemStack.EMPTY, GasStack.EMPTY));
	}

	@Override
	public boolean filter(RecipeHolder<T> recipe, ItemStack stack) {
		return recipe.value().getItemInput().test(stack) && super.filter(recipe, stack);
	}

	public interface Factory<T> {
		T create(ItemStackIngredient itemInput, ChemicalStackIngredient.GasStackIngredient gasInput, ItemStack output);
	}
}
