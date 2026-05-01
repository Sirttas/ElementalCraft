package sirttas.elementalcraft.interaction.jei.ingredient.element;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.gui.ElementGaugeGui;

import javax.annotation.Nonnull;
import java.util.List;

public class ElementIngredientRenderer implements IIngredientRenderer<IngredientElementType> {

	@Override
	public void render(@Nonnull GuiGraphicsExtractor guiGraphics, @Nonnull IngredientElementType ingredient) {
		int amount = ingredient.amount();

		ElementGaugeGui.renderElementGauge(guiGraphics, Minecraft.getInstance().font, 0, 0, amount == -1 ? 4 : amount, 4, ingredient.getElementType());
	}

	@Override
	public List<Component> getTooltip(@NotNull IngredientElementType ingredientElementType, @NotNull TooltipFlag tooltipFlag) {
		return List.of();
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, IngredientElementType ingredient, @Nonnull TooltipFlag tooltipFlag) {
		int amount = ingredient.amount();

		tooltip.add(ingredient.getDisplayName());
		if (amount != -1) {
			tooltip.add(Component.translatable("tooltip.elementalcraft.element_amount." + amount).withStyle(ChatFormatting.GREEN));
		}
	}

}
