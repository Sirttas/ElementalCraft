package sirttas.elementalcraft.jei;

import java.util.Arrays;
import java.util.stream.Collectors;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.inventory.IInventory;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.recipe.instrument.AbstractInstrumentRecipe;

public abstract class AbstractInstrumentRecipeCategory<K extends IInstrument & IInventory, T extends AbstractInstrumentRecipe<K>> implements IRecipeCategory<T> {

	@Override
	public void setIngredients(T recipe, IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, recipe.getIngredients().stream().map(i -> Arrays.asList(i.getMatchingStacks())).collect(Collectors.toList()));
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
	}
}
