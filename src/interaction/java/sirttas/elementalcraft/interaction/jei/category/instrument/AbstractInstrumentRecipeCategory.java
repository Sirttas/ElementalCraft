package sirttas.elementalcraft.interaction.jei.category.instrument;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.world.item.crafting.RecipeInput;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.recipe.instrument.InstrumentRecipe;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class AbstractInstrumentRecipeCategory<I extends RecipeInput, T extends InstrumentRecipe<I>> extends AbstractECRecipeCategory<T> {

	protected AbstractInstrumentRecipeCategory(String translationKey, IDrawable icon, int width, int height) {
		super(translationKey, icon, width, height);
	}

	@Nonnull
	protected List<IngredientElementType> getElementTypeIngredients(@Nonnull T recipe) {
		return recipe.getValidElementTypes().stream()
				.map(t -> new IngredientElementType(t, IngredientElementType.getGaugeValue(recipe.getElementAmount())))
				.toList();
	}

}
