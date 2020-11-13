package sirttas.elementalcraft.api.pureore;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class PureOreInjectorIMCMessage<C extends IInventory, T extends IRecipe<C>> {

	public static final String METHOD = "pure_ore_injector";

	private final IRecipeType<T> recipeType;
	private final BiFunction<T, Ingredient, T> builder;

	private BiPredicate<T, ItemStack> filter;

	private PureOreInjectorIMCMessage(IRecipeType<T> recipeType, BiFunction<T, Ingredient, T> builder) {
		this.recipeType = recipeType;
		this.builder = builder;
		filter = null;
	}

	public static <C extends IInventory, T extends IRecipe<C>> PureOreInjectorIMCMessage<C, T> create(IRecipeType<T> recipeType, BiFunction<T, Ingredient, T> builder) {
		return new PureOreInjectorIMCMessage<>(recipeType, builder);
	}

	public static String buildRecipeId(ResourceLocation source) {
		return source.getNamespace() + "_pure_" + source.getPath().replace('/', '_');
	}

	public PureOreInjectorIMCMessage<C, T> filter(BiPredicate<T, ItemStack> filter) {
		this.filter = filter;
		return this;
	}

	public IRecipeType<T> getRecipeType() {
		return recipeType;
	}

	public BiFunction<T, Ingredient, T> getBuilder() {
		return builder;
	}

	public BiPredicate<T, ItemStack> getFilter() {
		return filter;
	}
}
