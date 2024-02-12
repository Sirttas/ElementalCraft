package sirttas.elementalcraft.pureore.injector;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.pureore.factory.AbstractPureOreRecipeFactory;
import sirttas.elementalcraft.tag.ECTags;

public class PureOreCookingRecipeFactory<T extends AbstractCookingRecipe> extends AbstractPureOreRecipeFactory<Container, T> {

	private final Factory<T> factory;

	public PureOreCookingRecipeFactory(RecipeManager recipeManager, RecipeType<T> recipeType, Factory<T> factory) {
		super(recipeManager, recipeType);
		this.factory = factory;
	}

	@Override
	public T create(@NotNull RegistryAccess registry, @NotNull T recipe, @NotNull Ingredient ingredient) {
		return factory.create(recipe.getGroup(), recipe.category(), ingredient, getRecipeOutput(registry, recipe), recipe.getExperience(), recipe.getCookingTime());
	}

	public interface Factory<T extends AbstractCookingRecipe> {
		T create(String group, CookingBookCategory pCategory, Ingredient ingredient, ItemStack result, float experience, int cookTime);
	}

	@Override
	public boolean filter(RecipeHolder<T> holder, ItemStack stack) {
		return super.filter(holder, stack) && (stack.is(ECTags.Items.PURE_ORES_SOURCE_ORES) || stack.is(ECTags.Items.PURE_ORES_SPECIFICS));
	}
}
