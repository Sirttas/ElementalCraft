package sirttas.elementalcraft.jei.category;

import com.mojang.blaze3d.matrix.MatrixStack;

import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.gui.GuiHelper;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public abstract class AbstractInstrumentRecipeCategory<K extends IInstrument, T extends IInstrumentRecipe<K>> extends AbstractRecipeCategory<K, T> {

	protected int getGaugeValue(T recipe) {
		return (int) Math.log10(recipe.getElementPerTick() * recipe.getDuration()) - 2;
	}

	protected void renderElementGauge(MatrixStack matrixStack, int x, int y, T recipe) {
		GuiHelper.renderElementGauge(matrixStack, x, y, getGaugeValue(recipe), 4, recipe.getElementType());
	}
}
