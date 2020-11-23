package sirttas.elementalcraft.interaction.jei.ingredient.element;

import java.util.stream.StreamSupport;

import mezz.jei.api.ingredients.IIngredientHelper;
import net.minecraft.client.resources.I18n;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;

public class ElementIngredientHelper implements IIngredientHelper<IngredientElementType> {

	@Override
	public IngredientElementType getMatch(Iterable<IngredientElementType> ingredients, IngredientElementType ingredientToMatch) {
		return StreamSupport.stream(ingredients.spliterator(), false).filter(type -> type.getType() == ingredientToMatch.getType()).findAny().orElse(null);
	}

	@Override
	public String getDisplayName(IngredientElementType ingredient) {
		return I18n.format(ingredient.getType().getTranslationKey());
	}

	@Override
	public String getUniqueId(IngredientElementType ingredient) {
		return ingredient.getType().getString();
	}

	@Override
	public String getModId(IngredientElementType ingredient) {
		return ElementalCraft.MODID;
	}

	@Override
	public String getResourceId(IngredientElementType ingredient) {
		return ElementalCraft.createRL(ingredient.getType().getString()).toString();
	}

	@Override
	public IngredientElementType copyIngredient(IngredientElementType ingredient) {
		return ingredient.copy();
	}

	@Override
	public boolean isValidIngredient(IngredientElementType ingredient) {
		return ingredient.getType() != ElementType.NONE;
	}

	@Override
	public String getErrorInfo(IngredientElementType ingredient) {
		return ingredient.getType() == ElementType.NONE ? "Element shouldn't be none" : "";
	}

}
