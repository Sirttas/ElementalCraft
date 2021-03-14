package sirttas.elementalcraft.interaction.jei.category.instrument;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.interaction.jei.category.AbstractRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public abstract class AbstractInstrumentRecipeCategory<K extends IInstrument, T extends IInstrumentRecipe<K>> extends AbstractRecipeCategory<K, T> {

	private IDrawable overlay;
	private int overlayX;
	private int overlayY;

	public void setOverlay(IDrawable overlay, int x, int y) {
		this.overlay = overlay;
		overlayX = x;
		overlayY = y;
	}

	@Override
	public void draw(T recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		RenderSystem.enableBlend();
		overlay.draw(matrixStack, overlayX, overlayY);
		RenderSystem.disableBlend();
	}

	@Override
	public void setIngredients(T recipe, IIngredients ingredients) {
		super.setIngredients(recipe, ingredients);
		ingredients.setInput(ECIngredientTypes.ELEMENT, new IngredientElementType(recipe.getElementType(), getGaugeValue(recipe.getElementAmount())));
	}

}
