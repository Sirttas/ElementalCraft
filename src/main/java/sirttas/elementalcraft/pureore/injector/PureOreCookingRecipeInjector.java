package sirttas.elementalcraft.pureore.injector;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.tag.ECTags;

public class PureOreCookingRecipeInjector<T extends AbstractCookingRecipe> extends AbstractPureOreRecipeInjector<Container, T> {

	private final Factory<T> factory;

	public PureOreCookingRecipeInjector(RecipeType<T> recipeType, Factory<T> factory) {
		super(recipeType, false);
		this.factory = factory;
	}

	@Override
	public T build(@NotNull RegistryAccess registry, @NotNull T recipe, @NotNull Ingredient ingredient) {
		return factory.create(buildRecipeId(recipe.getId()), recipe.getGroup(), recipe.category(), ingredient, getRecipeOutput(registry, recipe), recipe.getExperience(), recipe.getCookingTime());
	}

	public interface Factory<T extends AbstractCookingRecipe> {
		T create(ResourceLocation id, String group, CookingBookCategory pCategory, Ingredient ingredient, ItemStack result, float experience, int cookTime);
	}

	@Override
	public boolean filter(T recipe, ItemStack stack) {
		return super.filter(recipe, stack) && (stack.is(ECTags.Items.PURE_ORES_SOURCE_ORES) || stack.is(ECTags.Items.PURE_ORES_SPECIFICS));
	}
}
