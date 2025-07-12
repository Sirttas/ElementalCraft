package sirttas.elementalcraft.api.pureore.factory;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public abstract class AbstractPureOreRecipeFactory<C extends RecipeInput, T extends Recipe<C>> implements IPureOreRecipeFactory<C, T> {

	private final RecipeType<T> recipeType;
	private final Collection<RecipeHolder<T>> recipes;
	
	protected AbstractPureOreRecipeFactory(@Nonnull RecipeManager recipeManager, @Nonnull RecipeType<T> recipeType) {
		this.recipeType = recipeType;
		this.recipes = recipeManager.byType(recipeType);
	}

	public List<RecipeHolder<T>> getRecipes(Collection<Holder<Item>> ores) {
		var stacks = ores.stream()
				.map(ItemStack::new)
				.toList();

		return recipes.stream()
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
