package sirttas.elementalcraft.api.pureore.injector;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Optional;
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
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import sirttas.elementalcraft.api.pureore.PureOreException;
import sirttas.elementalcraft.tag.ECTags;

public abstract class PureOreRecipeInjector<C extends IInventory, T extends IRecipe<C>> extends ForgeRegistryEntry<PureOreRecipeInjector<?, ? extends IRecipe<?>>> {

	public static final IForgeRegistry<PureOreRecipeInjector<?, ? extends IRecipe<?>>> REGISTRY = RegistryManager.ACTIVE.getRegistry(PureOreRecipeInjector.class);

	private final IRecipeType<T> recipeType;
	private final boolean modProcessing;

	private Map<ResourceLocation, T> recipes;
	private RecipeManager recipeManager;

	protected PureOreRecipeInjector(IRecipeType<T> recipeType) {
		this(recipeType, true);
	}
	
	protected PureOreRecipeInjector(IRecipeType<T> recipeType, boolean modProcessing) {
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
		this.recipes = recipeManager.getRecipes(recipeType).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> (T) entry.getValue()));
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
		return recipe.getRecipeOutput();
	}
	
	public abstract T build(T recipe, Ingredient ingredient);

	public Map<ResourceLocation, T> getRecipes() {
		return recipes;
	}

	public Optional<T> getRecipe(Item ore) {
		if (!isModProcessing() || !ECTags.Items.PURE_ORES_MOD_PROCESSING_BLACKLIST.contains(ore)) {
			return getRecipes().values().stream().map(recipe -> recipe).filter(recipe -> filter(recipe, new ItemStack(ore))).findAny();
		}
		return Optional.empty();
	}

	public IRecipeType<T> getRecipeType() {
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
