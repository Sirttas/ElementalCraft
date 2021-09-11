package sirttas.elementalcraft.interaction.jei.ingredient.element;

import java.util.stream.StreamSupport;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.client.resources.language.I18n;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;

public class ElementIngredientHelper implements IIngredientHelper<IngredientElementType> {

	@Override
	public IngredientElementType getMatch(Iterable<IngredientElementType> ingredients, IngredientElementType ingredientToMatch, UidContext context) {
		return StreamSupport.stream(ingredients.spliterator(), false)
				.filter(type -> type.getElementType() == ingredientToMatch.getElementType())
				.findAny()
				.orElse(null);
	}

	@Override
	public String getDisplayName(IngredientElementType ingredient) {
		return I18n.get(ingredient.getElementType().getTranslationKey());
	}

	@Override
	public String getUniqueId(IngredientElementType ingredient, UidContext context) {
		return ingredient.getElementType().getSerializedName();
	}

	@Override
	public String getModId(IngredientElementType ingredient) {
		return ElementalCraftApi.MODID;
	}

	@Override
	public String getResourceId(IngredientElementType ingredient) {
		return ElementalCraft.createRL(ingredient.getElementType().getSerializedName()).toString();
	}

	@Override
	public IngredientElementType copyIngredient(IngredientElementType ingredient) {
		return ingredient.copy();
	}

	@Override
	public boolean isValidIngredient(IngredientElementType ingredient) {
		return ingredient.getElementType() != ElementType.NONE;
	}

	@Override
	public String getErrorInfo(IngredientElementType ingredient) {
		return ingredient != null && ingredient.getElementType() == ElementType.NONE ? "Element shouldn't be none" : "";
	}

	@Override
	public IIngredientType<IngredientElementType> getIngredientType() {
		return IngredientElementType.TYPE;
	}

}
