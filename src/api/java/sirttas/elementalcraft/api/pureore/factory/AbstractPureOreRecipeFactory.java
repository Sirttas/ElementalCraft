package sirttas.elementalcraft.api.pureore.factory;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import sirttas.elementalcraft.api.pureore.PureOreException;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractPureOreRecipeFactory<C extends Container, T extends Recipe<C>> implements IPureOreRecipeFactory<C, T> {

	private final RecipeType<T> recipeType;

	private final Map<ResourceLocation, RecipeHolder<T>> recipes;
	
	protected AbstractPureOreRecipeFactory(@Nonnull RecipeManager recipeManager, @Nonnull RecipeType<T> recipeType) {
		this.recipeType = recipeType;
		this.recipes = recipeManager.byType(recipeType);
	}

	@Override
	public boolean filter(RecipeHolder<T> holder, ItemStack stack) {
		try {
			return holder.value().getIngredients().get(0).test(stack);
		} catch (Exception e) {
			throw new PureOreException(MessageFormat.format("Error while reading ingredients for recipe {0}. Please setup a custom filter for {1}", holder.id(), this), e);
		}
	}

	@Override
	public ItemStack getRecipeOutput(@Nonnull RegistryAccess registry, @Nonnull T recipe) {
		return recipe.getResultItem(registry);
	}

	public List<RecipeHolder<T>> getRecipes(Collection<Item> ores) {
		var stacks = ores.stream()
				.map(ItemStack::new)
				.toList();

		return recipes.values().stream()
				.filter(h -> stacks.stream().anyMatch(s -> filter(h, s)))
				.toList();
	}

	@Override
	public RecipeType<T> getRecipeType() {
		return recipeType;
	}

	@Override
	public String toString() {
		var key = BuiltInRegistries.RECIPE_TYPE.getKey(recipeType);

		return key != null ? key.toString() : super.toString();
	}
}
