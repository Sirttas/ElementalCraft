package sirttas.elementalcraft.interaction.jei.ingredient.element;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import sirttas.elementalcraft.gui.GuiHelper;

public class ElementIngredientRenderer implements IIngredientRenderer<IngredientElementType> {

	@Override
	public void render(MatrixStack matrixStack, int x, int y, IngredientElementType ingredient) {
		if (ingredient != null) {
			int amount = ingredient.getAmount();

			GuiHelper.renderElementGauge(matrixStack, x, y, amount == -1 ? 4 : amount, 4, ingredient.getType(), false);
		}

	}

	@Override
	public List<ITextComponent> getTooltip(IngredientElementType ingredient, ITooltipFlag tooltipFlag) {
		List<ITextComponent> tooltips = Lists.newArrayList();

		int amount = ingredient.getAmount();

		tooltips.add(ingredient.getDisplayName());
		if (amount != -1) {
			tooltips.add(new TranslationTextComponent("tooltip.elemntalcraft.element_amount." + amount).mergeStyle(TextFormatting.GREEN));
		}
		return tooltips;
	}

}
