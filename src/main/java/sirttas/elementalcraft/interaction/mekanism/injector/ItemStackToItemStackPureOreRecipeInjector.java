package sirttas.elementalcraft.interaction.mekanism.injector;

import mekanism.api.inventory.IgnoredIInventory;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.inputs.ItemStackIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;

public class ItemStackToItemStackPureOreRecipeInjector<T extends ItemStackToItemStackRecipe> extends AbstractMekanismPureOreRecipeInjector<IgnoredIInventory, T> {

	private final Factory<T> factory;

	public ItemStackToItemStackPureOreRecipeInjector(IRecipeType<T> recipeType, Factory<T> factory) {
		super(recipeType);
		this.factory = factory;
	}

	@Override
	public T build(T recipe, Ingredient ingredient) {
		return factory.create(ElementalCraft.createRL(buildRecipeId(recipe.getId())), ItemStackIngredient.from(ingredient), getRecipeOutput(recipe));
	}

	@Override
	public ItemStack getRecipeOutput(T recipe) {
		return tweakOutput(recipe.getOutput(ItemStack.EMPTY));
	}

	@Override
	public boolean filter(T recipe, ItemStack stack) {
		return recipe.test(stack);
	}

	public static interface Factory<T> {
		T create(ResourceLocation id, ItemStackIngredient input, ItemStack output);
	}
}
