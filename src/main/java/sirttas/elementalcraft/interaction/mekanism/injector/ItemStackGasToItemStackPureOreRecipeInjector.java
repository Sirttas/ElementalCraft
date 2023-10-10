package sirttas.elementalcraft.interaction.mekanism.injector;

import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class ItemStackGasToItemStackPureOreRecipeInjector<T extends ItemStackGasToItemStackRecipe> extends AbstractMekanismPureOreRecipeInjector<T> {

	private final Factory<T> factory;

	public ItemStackGasToItemStackPureOreRecipeInjector(RecipeTypeRegistryObject<T, InputRecipeCache.ItemChemical<Gas, GasStack, ItemStackGasToItemStackRecipe>> recipeType, Factory<T> factory) {
		super(recipeType);
		this.factory = factory;
	}

	@Override
	public T build(@NotNull RegistryAccess registry, T recipe, @NotNull Ingredient ingredient) {
		return factory.create(buildRecipeId(recipe.getId()), getInput(ingredient, recipe.getItemInput()), tweakOutput(recipe.getChemicalInput()), getRecipeOutput(registry, recipe));
	}

	@Override
	public ItemStack getRecipeOutput(@NotNull RegistryAccess registry, T recipe) {
		return tweakOutput(recipe.getOutput(ItemStack.EMPTY, GasStack.EMPTY));
	}

	@Override
	public boolean filter(T recipe, ItemStack stack) {
		return recipe.getItemInput().test(stack) && super.filter(recipe, stack);
	}

	public interface Factory<T> {
		T create(ResourceLocation id, ItemStackIngredient itemInput, ChemicalStackIngredient.GasStackIngredient gasInput, ItemStack output);
	}
}
