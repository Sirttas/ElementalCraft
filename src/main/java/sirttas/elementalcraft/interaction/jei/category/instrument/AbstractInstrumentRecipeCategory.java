package sirttas.elementalcraft.interaction.jei.category.instrument;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.interaction.jei.category.AbstractBlockEntityRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public abstract class AbstractInstrumentRecipeCategory<K extends IInstrument, T extends IInstrumentRecipe<K>> extends AbstractBlockEntityRecipeCategory<K, T> {

	protected AbstractInstrumentRecipeCategory(String translationKey, IDrawable icon, IDrawable background) {
		super(translationKey, icon, background);
	}

	@Override
	public void setIngredients(T recipe, IIngredients ingredients) {
		super.setIngredients(recipe, ingredients);
		ingredients.setInput(ECIngredientTypes.ELEMENT, new IngredientElementType(recipe.getElementType(), getGaugeValue(recipe.getElementAmount())));
	}

}
