package sirttas.elementalcraft.interaction.jei.ingredient.source;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.event.TickHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class SourceIngredientRenderer implements IIngredientRenderer<IngredientSource> {

	private static final ResourceLocation OUTER = ElementalCraft.createRL("textures/effect/source_outer.png");
	private static final ResourceLocation MIDDLE = ElementalCraft.createRL("textures/effect/source_middle.png");

	@Override
	public void render(@Nonnull PoseStack matrixStack, @Nonnull IngredientSource source) {
		ElementType elementType = source.getElementType();
		var angle = -(TickHandler.getTicksInGame() % 360);


		RenderSystem.enableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		RenderSystem.setShaderColor(elementType.getRed(), elementType.getGreen(), elementType.getBlue(), 1.0F);
		matrixStack.scale(0.5f, 0.5f, 0.5f);
		matrixStack.translate(16, 16, 0);
		matrixStack.mulPose(Vector3f.ZP.rotationDegrees(angle));
		matrixStack.translate(-16, -16, 0);
		RenderSystem.setShaderTexture(0, OUTER);
		GuiComponent.blit(matrixStack, 0,0,0,0, 32, 32, 32, 32);
		matrixStack.translate(16, 16, 0);
		matrixStack.mulPose(Vector3f.ZP.rotationDegrees(angle * 5f));
		matrixStack.translate(-16, -16, -0.01);

		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		RenderSystem.setShaderTexture(0, MIDDLE);
		GuiComponent.blit(matrixStack, 0,0,0,0, 32, 32, 32, 32);

		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(true);
	}

	@Nonnull
    @Override
	public List<Component> getTooltip(IngredientSource ingredient, @Nonnull TooltipFlag tooltipFlag) {
		return List.of(ingredient.getDisplayName());
	}

}
