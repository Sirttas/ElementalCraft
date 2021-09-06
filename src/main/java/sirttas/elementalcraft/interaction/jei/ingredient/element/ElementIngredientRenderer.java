package sirttas.elementalcraft.interaction.jei.ingredient.element;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import sirttas.elementalcraft.gui.GuiHelper;

public class ElementIngredientRenderer implements IIngredientRenderer<IngredientElementType> {

	@Override
	public void render(PoseStack matrixStack, int x, int y, IngredientElementType ingredient) {
		if (ingredient != null) {
			int amount = ingredient.getAmount();

			GuiHelper.renderElementGauge(matrixStack, x, y, amount == -1 ? 4 : amount, 4, ingredient.getType(), false);
		}

	}

	@Override
	public List<Component> getTooltip(IngredientElementType ingredient, TooltipFlag tooltipFlag) {
		List<Component> tooltips = Lists.newArrayList();

		int amount = ingredient.getAmount();

		tooltips.add(ingredient.getDisplayName());
		if (amount != -1) {
			tooltips.add(new TranslatableComponent("tooltip.elemntalcraft.element_amount." + amount).withStyle(ChatFormatting.GREEN));
		}
		return tooltips;
	}

}
