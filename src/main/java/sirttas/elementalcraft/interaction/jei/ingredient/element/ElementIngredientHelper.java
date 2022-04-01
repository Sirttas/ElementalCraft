package sirttas.elementalcraft.interaction.jei.ingredient.element;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.client.resources.language.I18n;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;

import javax.annotation.Nonnull;
import java.util.stream.StreamSupport;

public class ElementIngredientHelper implements IIngredientHelper<IngredientElementType> {

	@Override
	public IngredientElementType getMatch(Iterable<IngredientElementType> ingredients, @Nonnull IngredientElementType ingredientToMatch, @Nonnull UidContext context) {
		return StreamSupport.stream(ingredients.spliterator(), false)
				.filter(type -> type.getElementType() == ingredientToMatch.getElementType())
				.findAny()
				.orElse(null);
	}

	@Nonnull
    @Override
	public String getDisplayName(IngredientElementType ingredient) {
		return I18n.get(ingredient.getElementType().getTranslationKey());
	}

	@Nonnull
    @Override
	public String getUniqueId(IngredientElementType ingredient, @Nonnull UidContext context) {
		return ingredient.getElementType().getSerializedName();
	}

	@Nonnull
    @Override
	public String getModId(@Nonnull IngredientElementType ingredient) {
		return ElementalCraftApi.MODID;
	}

	@Nonnull
    @Override
	public String getResourceId(IngredientElementType ingredient) {
		return ingredient.getElementType().getSerializedName();
	}

	@Nonnull
    @Override
	public IngredientElementType copyIngredient(IngredientElementType ingredient) {
		return ingredient.copy();
	}

	@Override
	public boolean isValidIngredient(IngredientElementType ingredient) {
		return ingredient.getElementType() != ElementType.NONE;
	}

	@Nonnull
    @Override
	public String getErrorInfo(IngredientElementType ingredient) {
		return ingredient != null && ingredient.getElementType() == ElementType.NONE ? "Element shouldn't be none" : "";
	}

	@Nonnull
    @Override
	public IIngredientType<IngredientElementType> getIngredientType() {
		return IngredientElementType.TYPE;
	}

}
