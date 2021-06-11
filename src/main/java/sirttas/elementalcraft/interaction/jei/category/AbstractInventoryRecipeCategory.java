package sirttas.elementalcraft.interaction.jei.category;

import java.util.Arrays;
import java.util.stream.Collectors;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import sirttas.elementalcraft.item.ECItems;

public abstract class AbstractInventoryRecipeCategory<I extends IInventory, T extends IRecipe<I>> extends AbstractECRecipeCategory<T> {

	protected ItemStack tank = new ItemStack(ECItems.TANK);
	
	protected AbstractInventoryRecipeCategory(String translationKey, IDrawable icon, IDrawable background) {
		super(translationKey, icon, background);
	}

	@Override
	public void setIngredients(T recipe, IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, recipe.getIngredients().stream().map(i -> Arrays.asList(i.getItems())).collect(Collectors.toList()));
		if (!recipe.getResultItem().isEmpty()) {
			ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
		}
	}

	protected int getGaugeValue(int amount) {
		return (int) Math.log10(amount) - 1;
	}
}
