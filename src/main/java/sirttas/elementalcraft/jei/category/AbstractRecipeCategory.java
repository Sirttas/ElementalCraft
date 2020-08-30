package sirttas.elementalcraft.jei.category;

import java.util.Arrays;
import java.util.stream.Collectors;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import sirttas.elementalcraft.item.ECItems;

public abstract class AbstractRecipeCategory<K extends IInventory, T extends IRecipe<K>> implements IRecipeCategory<T> {

	protected ItemStack tank = new ItemStack(ECItems.tank).copy();

	@Override
	public void setIngredients(T recipe, IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, recipe.getIngredients().stream().map(i -> Arrays.asList(i.getMatchingStacks())).collect(Collectors.toList()));
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
	}
}
