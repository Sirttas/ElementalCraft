package sirttas.elementalcraft.item.pureore.injector;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;

public class PureOreCookingRecipeInjector<T extends AbstractCookingRecipe> extends AbstractPureOreRecipeInjector<IInventory, T> {

	private final Factory<T> factory;

	public PureOreCookingRecipeInjector(IRecipeType<T> recipeType, Factory<T> factory) {
		super(recipeType, false);
		this.factory = factory;
	}

	@Override
	public T build(T recipe, Ingredient ingredient) {
		return factory.create(ElementalCraft.createRL(buildRecipeId(recipe.getId())), recipe.getGroup(), ingredient, getRecipeOutput(recipe), recipe.getExperience(), recipe.getCookTime());
	}

	public static interface Factory<T extends AbstractCookingRecipe> {
		T create(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience, int cookTime);
	}

}
