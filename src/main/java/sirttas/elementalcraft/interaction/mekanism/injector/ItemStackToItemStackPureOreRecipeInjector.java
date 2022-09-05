package sirttas.elementalcraft.interaction.mekanism.injector;

import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class ItemStackToItemStackPureOreRecipeInjector<T extends ItemStackToItemStackRecipe> extends AbstractMekanismPureOreRecipeInjector<T> {

	private final Factory<T> factory;

	public ItemStackToItemStackPureOreRecipeInjector(RecipeTypeRegistryObject<T, InputRecipeCache.SingleItem<ItemStackToItemStackRecipe>> recipeType, Factory<T> factory) {
		super(recipeType);
		this.factory = factory;
	}

	@Override
	public T build(T recipe, Ingredient ingredient) {
		return factory.create(buildRecipeId(recipe.getId()), getInput(ingredient, recipe.getInput()), getRecipeOutput(recipe));
	}

	@Override
	public ItemStack getRecipeOutput(T recipe) {
		return tweakOutput(recipe.getOutput(ItemStack.EMPTY));
	}

	@Override
	public boolean filter(T recipe, ItemStack stack) {
		return recipe.test(stack) && super.filter(recipe, stack);
	}

	public interface Factory<T> {
		T create(ResourceLocation id, ItemStackIngredient input, ItemStack output);
	}
}
