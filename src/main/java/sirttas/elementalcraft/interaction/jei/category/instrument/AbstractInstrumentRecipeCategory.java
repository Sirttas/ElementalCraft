package sirttas.elementalcraft.interaction.jei.category.instrument;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.world.item.crafting.RecipeInput;
import sirttas.elementalcraft.interaction.jei.category.AbstractInventoryRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class AbstractInstrumentRecipeCategory<I extends RecipeInput, T extends IInstrumentRecipe<I>> extends AbstractInventoryRecipeCategory<I, T> {

	protected AbstractInstrumentRecipeCategory(String translationKey, IDrawable icon, IDrawable background) {
		super(translationKey, icon, background);
	}

	@Nonnull
	protected List<IngredientElementType> getElementTypeIngredients(@Nonnull T recipe) {
		return recipe.getValidElementTypes().stream()
				.map(t -> new IngredientElementType(t, IngredientElementType.getGaugeValue(recipe.getElementAmount())))
				.toList();
	}

}
