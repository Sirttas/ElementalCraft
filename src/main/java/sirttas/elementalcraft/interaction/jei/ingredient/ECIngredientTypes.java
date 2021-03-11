package sirttas.elementalcraft.interaction.jei.ingredient;

import mezz.jei.api.ingredients.IIngredientType;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;

public class ECIngredientTypes {
	
	public static final IIngredientType<IngredientElementType> ELEMENT = () -> IngredientElementType.class;

	private ECIngredientTypes() {}
	
}
