package sirttas.elementalcraft.item.pureore;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.pureore.PureOreInjectorIMCMessage;
import sirttas.elementalcraft.item.pureore.PureOreManager.Entry;

public class PureOreRecipeInjector<C extends IInventory, T extends IRecipe<C>> {

	private final IRecipeType<T> recipeType;
	private final BiFunction<T, Ingredient, T> builder;

	private BiPredicate<T, ItemStack> filter;
	private Map<ResourceLocation, IRecipe<C>> recipes;
	private RecipeManager recipeManager;

	private PureOreRecipeInjector(IRecipeType<T> recipeType, BiFunction<T, Ingredient, T> builder) {
		this.recipeType = recipeType;
		this.builder = builder;
		this.recipes = null;
		this.recipeManager = null;
		filter = null;
	}

	public static <C extends IInventory, T extends IRecipe<C>> PureOreRecipeInjector<C, T> create(IRecipeType<T> recipeType, BiFunction<T, Ingredient, T> builder) {
		return new PureOreRecipeInjector<>(recipeType, builder);
	}

	public static <C extends IInventory, T extends IRecipe<C>> PureOreRecipeInjector<C, T> create(Supplier<PureOreInjectorIMCMessage<C, T>> messageSuplier) {
		PureOreInjectorIMCMessage<C, T> message = messageSuplier.get();

		return create(message.getRecipeType(), message.getBuilder()).filter(message.getFilter());
	}

	public PureOreRecipeInjector<C, T> filter(BiPredicate<T, ItemStack> filter) {
		this.filter = filter;
		return this;
	}

	void init(RecipeManager recipeManager) {
		this.recipeManager = recipeManager;
		this.recipes = recipeManager.getRecipes(recipeType).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		if (filter == null) {
			filter = (recipe, stack) -> recipe.getIngredients().get(0).test(stack);
		}
	}

	public Map<ResourceLocation, IRecipe<C>> getRecipes() {
		return recipes;
	}

	@SuppressWarnings("unchecked")
	public Optional<T> getRecipe(Item ore) {
		return getRecipes().values().stream().map(recipe -> (T) recipe).filter(recipe -> filter.test(recipe, new ItemStack(ore))).findAny();
	}

	void inject(List<PureOreManager.Entry> entries) {
		Map<ResourceLocation, IRecipe<C>> map = getRecipes();

		map.putAll(entries.stream().distinct().map(this::injectEntry).filter(Objects::nonNull).collect(Collectors.toMap(IRecipe::getId, o -> o, (recipe1, recipe2) -> {
			ElementalCraft.LOGGER.warn("Duplicated key for type {}: {}", recipeType, recipe1.getId());
			return recipe1;
		})));
		recipeManager.recipes.put(recipeType, map.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue)));
	}

	public T injectEntry(Entry entry) {
		try {
			T recipe = entry.getRecipe(recipeType);

			return recipe != null ? builder.apply(recipe, entry.getIngredient()) : null;
		} catch (Exception e) {
			ElementalCraft.LOGGER.error("Error in pure ore recipe injection", e);
			return null;
		}
	}

	@Override
	public String toString() {
		return Registry.RECIPE_TYPE.getKey(recipeType).toString();
	}
}
