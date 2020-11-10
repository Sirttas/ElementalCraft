package sirttas.elementalcraft.interaction.jei.category.instrument;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.gui.GuiHelper;
import sirttas.elementalcraft.interaction.jei.category.AbstractRecipeCategory;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public abstract class AbstractInstrumentRecipeCategory<K extends IInstrument, T extends IInstrumentRecipe<K>> extends AbstractRecipeCategory<K, T> {

	private IDrawable overlay;
	private int overlayX;
	private int overlayY;
	private int gaugeX;
	private int gaugeY;

	public void setOverlay(IDrawable overlay, int x, int y) {
		this.overlay = overlay;
		overlayX = x;
		overlayY = y;
	}

	public void setGaugePos(int x, int y) {
		gaugeX = x;
		gaugeY = y;
	}

	@Override
	public void draw(T recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		RenderSystem.enableBlend();
		overlay.draw(matrixStack, overlayX, overlayY);
		renderElementGauge(matrixStack, gaugeX, gaugeY, recipe);
		RenderSystem.disableBlend();
	}

	@Override
	public List<ITextComponent> getTooltipStrings(T recipe, double mouseX, double mouseY) {
		if (mouseX >= gaugeX && mouseX <= gaugeX + 16 && mouseY >= gaugeY && mouseY <= gaugeY + 16) {
			List<ITextComponent> tooltips = Lists.newArrayList();

			tooltips.add(recipe.getElementType().getDisplayName());
			tooltips.add(new TranslationTextComponent("tooltip.elemntalcraft.element_amount." + getGaugeValue(recipe)).mergeStyle(TextFormatting.GREEN));
			return tooltips;
		}
		return super.getTooltipStrings(recipe, mouseX, mouseY);
	}

	protected int getGaugeValue(T recipe) {
		return (int) Math.log10(recipe.getElementPerTick() * recipe.getDuration()) - 1;
	}

	protected void renderElementGauge(MatrixStack matrixStack, int x, int y, T recipe) {
		GuiHelper.renderElementGauge(matrixStack, x, y, getGaugeValue(recipe), 4, recipe.getElementType());
	}

}
