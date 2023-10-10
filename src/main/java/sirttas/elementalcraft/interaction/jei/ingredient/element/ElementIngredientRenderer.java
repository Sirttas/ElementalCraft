package sirttas.elementalcraft.interaction.jei.ingredient.element;

import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import sirttas.elementalcraft.gui.GuiHelper;

import javax.annotation.Nonnull;
import java.util.List;

public class ElementIngredientRenderer implements IIngredientRenderer<IngredientElementType> {

	@Override
	public void render(@Nonnull GuiGraphics guiGraphics, @Nonnull IngredientElementType ingredient) {
		int amount = ingredient.amount();

		GuiHelper.renderElementGauge(guiGraphics, Minecraft.getInstance().font, 0, 0, amount == -1 ? 4 : amount, 4, ingredient.getElementType(), false);
	}

	@Nonnull
    @Override
	public List<Component> getTooltip(IngredientElementType ingredient, @Nonnull TooltipFlag tooltipFlag) {
		List<Component> tooltips = Lists.newArrayList();

		int amount = ingredient.amount();

		tooltips.add(ingredient.getDisplayName());
		if (amount != -1) {
			tooltips.add(Component.translatable("tooltip.elemntalcraft.element_amount." + amount).withStyle(ChatFormatting.GREEN));
		}
		return tooltips;
	}

}
