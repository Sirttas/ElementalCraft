package sirttas.elementalcraft.interaction.jei.category;

import java.util.Arrays;
import java.util.stream.Collectors;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import sirttas.elementalcraft.inventory.IInventoryTile;
import sirttas.elementalcraft.inventory.InventoryTileWrapper;
import sirttas.elementalcraft.item.ECItems;

public abstract class AbstractRecipeCategory<K extends IInventoryTile, T extends IRecipe<InventoryTileWrapper<K>>> implements IRecipeCategory<T> {

	protected ItemStack tank = new ItemStack(ECItems.TANK);

	@Override
	public void setIngredients(T recipe, IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, recipe.getIngredients().stream().map(i -> Arrays.asList(i.getMatchingStacks())).collect(Collectors.toList()));
		if (!recipe.getRecipeOutput().isEmpty()) {
			ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
		}
	}

	protected int getGaugeValue(int amount) {
		return (int) Math.log10(amount) - 1;
	}
}
