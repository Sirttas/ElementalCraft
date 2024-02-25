package sirttas.elementalcraft.interaction.mekanism.injector;

import mekanism.api.recipes.ItemStackToItemStackRecipe;
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

public class ItemStackToItemStackPureOreRecipeFactory<T extends ItemStackToItemStackRecipe> extends AbstractMekanismPureOreRecipeFactory<T> {

	private final Factory<T> factory;

	public ItemStackToItemStackPureOreRecipeFactory(@Nonnull RecipeManager recipeManager, @Nonnull RecipeTypeRegistryObject<T, InputRecipeCache.SingleItem<ItemStackToItemStackRecipe>> recipeType, @Nonnull Factory<T> factory) {
		super(recipeManager, recipeType);
		this.factory = factory;
	}

	@Override
	public T create(@NotNull RegistryAccess registry, @NotNull T recipe, @NotNull Ingredient ingredient) {
		return factory.create(getInput(ingredient, recipe.getInput()), getRecipeOutput(registry, recipe));
	}

	@Override
	public ItemStack getRecipeOutput(@NotNull RegistryAccess registry, @NotNull T recipe) {
		return tweakOutput(recipe.getOutput(ItemStack.EMPTY));
	}

	@Override
	public boolean filter(RecipeHolder<T> recipe, ItemStack stack) {
		return recipe.value().test(stack) && super.filter(recipe, stack);
	}

	public interface Factory<T> {
		T create(ItemStackIngredient input, ItemStack output);
	}
}
