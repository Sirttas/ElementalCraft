package sirttas.elementalcraft.interaction.mekanism.injector;

import mekanism.api.chemical.gas.GasStack;
import mekanism.api.inventory.IgnoredIInventory;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.inputs.ItemStackIngredient;
import mekanism.api.recipes.inputs.chemical.GasStackIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;

public class ItemStackGasToItemStackPureOreRecipeInjector<T extends ItemStackGasToItemStackRecipe> extends AbstractMekanismPureOreRecipeInjector<IgnoredIInventory, T> {

	private final Factory<T> factory;

	public ItemStackGasToItemStackPureOreRecipeInjector(IRecipeType<T> recipeType, Factory<T> factory) {
		super(recipeType);
		this.factory = factory;
	}

	@Override
	public T build(T recipe, Ingredient ingredient) {
		return factory.create(ElementalCraft.createRL(buildRecipeId(recipe.getId())), ItemStackIngredient.from(ingredient), recipe.getChemicalInput(), getRecipeOutput(recipe));
	}

	@Override
	public ItemStack getRecipeOutput(T recipe) {
		return tweakOutput(recipe.getOutput(ItemStack.EMPTY, GasStack.EMPTY));
	}

	@Override
	public boolean filter(T recipe, ItemStack stack) {
		return recipe.getItemInput().test(stack);
	}

	public static interface Factory<T> {
		T create(ResourceLocation id, ItemStackIngredient itemInput, GasStackIngredient gasInput, ItemStack output);
	}
}
