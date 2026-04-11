package sirttas.elementalcraft.interaction.jei.ingredient.source;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.event.TickHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class SourceIngredientRenderer implements IIngredientRenderer<IngredientSource> {

	private static final Identifier OUTER = ElementalCraftApi.createRL("textures/effect/source_outer.png");
	private static final Identifier MIDDLE = ElementalCraftApi.createRL("textures/effect/source_middle.png");

	@Override
	public void render(@Nonnull GuiGraphicsExtractor guiGraphics, @Nonnull IngredientSource source) {
		var elementType = source.getElementType();
		var poseStack = guiGraphics.pose();
		var angle = -(TickHandler.getTicksInGame() % 360);


		RenderSystem.enableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		RenderSystem.setShaderColor(elementType.getRed(), elementType.getGreen(), elementType.getBlue(), 1.0F);
		poseStack.scale(0.5f, 0.5f, 0.5f);
		poseStack.translate(16, 16, 0);
		poseStack.mulPose(Axis.ZP.rotationDegrees(angle));
		poseStack.translate(-16, -16, 0);
		guiGraphics.blit(OUTER, 0, 0, 0, 0, 32, 32, 32, 32);
		poseStack.translate(16, 16, 0);
		poseStack.mulPose(Axis.ZP.rotationDegrees(angle * 5f));
		poseStack.translate(-16, -16, -0.01);

		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		guiGraphics.blit(MIDDLE, 0,0,0,0, 32, 32, 32, 32);

		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(true);
	}

	@Override
	public List<Component> getTooltip(@NotNull IngredientSource ingredientSource, @NotNull TooltipFlag tooltipFlag) {
		return List.of();
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, IngredientSource ingredient, @NotNull TooltipFlag tooltipFlag) {
		tooltip.add(ingredient.getDisplayName());
	}

}
