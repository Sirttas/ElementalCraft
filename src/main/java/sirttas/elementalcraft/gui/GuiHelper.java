package sirttas.elementalcraft.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
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
	
	public static void blit(MatrixStack matrixStack, int x, int y, int u, int v, int width, int height) {
		AbstractGui.blit(matrixStack, x, y, u, v, width, height, 256, 256);
	}

	private static int getElementTypeOffset(ElementType type) {
		switch (type) {
		case WATER:
			return 1;
		case FIRE:
			return 2;
		case EARTH:
			return 3;
		case AIR:
			return 4;
		default:
			return 0;
		}
	}

	public static void renderElementGauge(MatrixStack matrixStack, int x, int y, int amount, int max, ElementType type) {
		renderElementGauge(matrixStack, x, y, amount, max, type, true);
	}

	public static void renderElementGauge(MatrixStack matrixStack, int x, int y, int amount, int max, ElementType type, boolean showDebugInfo) {
		Minecraft mc = Minecraft.getInstance();

		mc.textureManager.bindTexture(GAUGE);
		blit(matrixStack, x, y, 0, 0, 16, 16);

		int progress = Math.max(0, (int) ((double) Math.min(amount, max) / (double) max * 16));

		if (progress <= 1 && amount > 0) {
			progress = 2;
		}
		blit(matrixStack, x, y + 16 - progress, getElementTypeOffset(type) * 16, 16 - progress + (Boolean.TRUE.equals(ECConfig.CLIENT.usePaleElementGauge.get()) ? 16 : 0), 16, progress);
		if (showDebugInfo() && showDebugInfo) {
			mc.fontRenderer.drawStringWithShadow(matrixStack, amount + "/" + max, x, y + 16F, 16777215);
		}
	}

	public static void renderCanCast(MatrixStack matrixStack, int x, int y, boolean canCast) {
		Minecraft.getInstance().textureManager.bindTexture(GAUGE);
		blit(matrixStack, x, y, canCast ? 0 : 6, 16, 6, 6);
	}

	public static boolean showDebugInfo() {
		Minecraft minecraft = Minecraft.getInstance();

		return minecraft.player.isCreative() && minecraft.gameSettings.advancedItemTooltips;
	}
}
