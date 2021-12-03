package sirttas.elementalcraft.api.pureore.injector;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import sirttas.elementalcraft.api.pureore.PureOreException;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractPureOreRecipeInjector<C extends Container, T extends Recipe<C>> extends ForgeRegistryEntry<AbstractPureOreRecipeInjector<?, ? extends Recipe<?>>> {

	public static final IForgeRegistry<AbstractPureOreRecipeInjector<?, ? extends Recipe<?>>> REGISTRY = RegistryManager.ACTIVE.getRegistry(AbstractPureOreRecipeInjector.class);

	private final RecipeType<T> recipeType;
	private final boolean modProcessing;

	private Map<ResourceLocation, T> recipes;
	private RecipeManager recipeManager;
	
	protected AbstractPureOreRecipeInjector(RecipeType<T> recipeType) {
		this(recipeType, true);
	}
	
	protected AbstractPureOreRecipeInjector(RecipeType<T> recipeType, boolean modProcessing) {
		this.recipeType = recipeType;
		this.recipes = null;
		this.recipeManager = null;
		this.modProcessing = modProcessing;
	}

	public static String buildRecipeId(ResourceLocation source) {
		return source.getNamespace() + "_pure_" + source.getPath().replace('/', '_');
	}

	@SuppressWarnings("unchecked")
	public void init(RecipeManager recipeManager) {
		this.recipeManager = recipeManager;
		this.recipes = recipeManager.byType(recipeType).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> (T) entry.getValue()));
	}

	public boolean filter(T recipe, ItemStack stack) {
		try {
			return recipe.getIngredients().get(0).test(stack);
		} catch (Exception e) {
			throw new PureOreException(MessageFormat.format("Error while reading ingredients for recipe {0}. Pleanse setup a custom filter for {1}", recipe.getId(), this), e);
		}
	}

	public void inject(Map<ResourceLocation, T> map) {
		recipeManager.recipes.put(recipeType, map.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue)));
	}

	public ItemStack getRecipeOutput(T recipe) {
		return recipe.getResultItem();
	}
	
	public abstract T build(T recipe, Ingredient ingredient);

	public Map<ResourceLocation, T> getRecipes() {
		return recipes;
	}

	public Optional<T> getRecipe(Item ore) {
		return getRecipes().values().stream()
				.filter(recipe -> filter(recipe, new ItemStack(ore)))
				.min(Comparator.comparing(Recipe::getId));
	}

	public RecipeType<T> getRecipeType() {
		return recipeType;
	}

	public ResourceLocation getRecipeTypeRegistryName() {
		return Registry.RECIPE_TYPE.getKey(recipeType);
	}

	@Override
	public String toString() {
		return getRecipeTypeRegistryName().toString();
	}
	
	public boolean isModProcessing() {
		return modProcessing;
	}
}
