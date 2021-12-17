package sirttas.elementalcraft.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.config.ECConfig;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("resource")
public class GuiHelper {

	private static final ResourceLocation GAUGE = ElementalCraft.createRL("textures/gui/element_gauge.png");

	private GuiHelper() {}
	
	public static void blit(PoseStack matrixStack, int x, int y, int u, int v, int width, int height) {
		GuiComponent.blit(matrixStack, x, y, u, v, width, height, 256, 256);
	}

	private static int getElementTypeOffset(ElementType type) {
		return switch (type) {
			case WATER -> 1;
			case FIRE -> 2;
			case EARTH -> 3;
			case AIR -> 4;
			default -> 0;
		};
	}

	public static void renderElementGauge(PoseStack matrixStack, int x, int y, int amount, int max, ElementType type) {
		renderElementGauge(matrixStack, x, y, amount, max, type, true);
	}

	public static void renderElementGauge(PoseStack matrixStack, int x, int y, int amount, int max, ElementType type, boolean showDebugInfo) {
		RenderSystem.setShaderTexture(0, GAUGE);
		blit(matrixStack, x, y, 0, 0, 16, 16);

		int progress = Math.max(0, (int) ((double) Math.min(amount, max) / (double) max * 16));

		if (progress <= 1 && amount > 0) {
			progress = 2;
		}
		blit(matrixStack, x, y + 16 - progress, getElementTypeOffset(type) * 16, 16 - progress + (Boolean.TRUE.equals(ECConfig.CLIENT.usePaleElementGauge.get()) ? 16 : 0), 16, progress);
		if (showDebugInfo() && showDebugInfo) {
			Minecraft.getInstance().font.drawShadow(matrixStack, amount + "/" + max, x, y + 16F, 16777215);
		}
	}

	public static void renderCanCast(PoseStack matrixStack, int x, int y, boolean canCast) {
		RenderSystem.setShaderTexture(0, GAUGE);
		blit(matrixStack, x, y, canCast ? 0 : 6, 16, 6, 6);
	}

	public static boolean showDebugInfo() {
		Minecraft minecraft = Minecraft.getInstance();

		return minecraft.player.isCreative() && minecraft.options.advancedItemTooltips;
	}
}
