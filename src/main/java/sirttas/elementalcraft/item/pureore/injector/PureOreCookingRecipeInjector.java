package sirttas.elementalcraft.item.pureore.injector;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;

public class PureOreCookingRecipeInjector<T extends AbstractCookingRecipe> extends AbstractPureOreRecipeInjector<Container, T> {

	private final Factory<T> factory;

	public PureOreCookingRecipeInjector(RecipeType<T> recipeType, Factory<T> factory) {
		super(recipeType, false);
		this.factory = factory;
	}

	@Override
	public T build(T recipe, Ingredient ingredient) {
		return factory.create(ElementalCraft.createRL(buildRecipeId(recipe.getId())), recipe.getGroup(), ingredient, getRecipeOutput(recipe), recipe.getExperience(), recipe.getCookingTime());
	}

	public interface Factory<T extends AbstractCookingRecipe> {
		T create(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience, int cookTime);
	}

}
