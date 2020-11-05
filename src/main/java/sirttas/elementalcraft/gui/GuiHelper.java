package sirttas.elementalcraft.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.config.ECConfig;

/**
 * This class come from Botania code
 * 
 * 
 * https://github.com/Vazkii/Botania
 */
@SuppressWarnings("resource")
public class GuiHelper {

	@OnlyIn(Dist.CLIENT)
	private static final ResourceLocation GAUGE = ElementalCraft.createRL("textures/gui/element_gauge.png");

	@OnlyIn(Dist.CLIENT)
	public static void blit(MatrixStack matrixStack, int x, int y, int u, int v, int width, int height) {
		AbstractGui.blit(matrixStack, x, y, u, v, width, height, 256, 256);
	}

	@OnlyIn(Dist.CLIENT)
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

	@OnlyIn(Dist.CLIENT)
	public static void renderElementGauge(MatrixStack matrixStack, int x, int y, int amount, int max, ElementType type) {
		Minecraft mc = Minecraft.getInstance();

		mc.textureManager.bindTexture(GAUGE);
		blit(matrixStack, x, y, 0, 0, 17, 17);

		int progress = Math.max(0, (int) ((double) Math.min(amount, max) / (double) max * 17));

		if (progress <= 1 && amount > 0) {
			progress = 2;
		}
		blit(matrixStack, x, y + 17 - progress, getElementTypeOffset(type) * 17, 17 - progress + (Boolean.TRUE.equals(ECConfig.CLIENT.usePaleElementGauge.get()) ? 17 : 0), 17, progress);
		if (showDebugInfo()) {
			mc.fontRenderer.drawStringWithShadow(matrixStack, amount + "/" + max, x, y + 17, 16777215);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void renderCanCast(MatrixStack matrixStack, int x, int y, boolean canCast) {
		Minecraft.getInstance().textureManager.bindTexture(GAUGE);
		blit(matrixStack, x, y, canCast ? 0 : 6, 17, 6, 6);
	}

	public static boolean showDebugInfo() {
		Minecraft minecraft = Minecraft.getInstance();

		return minecraft.player.isCreative() && minecraft.gameSettings.advancedItemTooltips;
	}

	public static int colorFromRGB(int r, int g, int b) {
		return (r << 16) | (g << 8) | b;
	}
}
