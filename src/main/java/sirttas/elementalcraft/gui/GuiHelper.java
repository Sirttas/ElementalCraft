package sirttas.elementalcraft.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;

/**
 * This class come from Botania code
 * 
 * 
 * https://github.com/Vazkii/Botania
 */
public class GuiHelper {

	private static final ResourceLocation GAUGE = new ResourceLocation(ElementalCraft.MODID, "textures/gui/element_gauge.png");

	public static void blit(int x, int y, int u, int v, int width, int height) {
		AbstractGui.blit(x, y, u, v, width, height, 256, 256);
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

	@OnlyIn(Dist.CLIENT)
	public static void renderElementGauge(int x, int y, int amount, int max, ElementType type) {
		Minecraft mc = Minecraft.getInstance();

		mc.textureManager.bindTexture(GAUGE);
		blit(x, y, 0, 0, 17, 17);

		int progress = Math.max(0, (int) ((double) amount / (double) max * 17));

		if (progress <= 1 && amount > 0) {
			progress = 2;
		}
		blit(x, y + 17 - progress, getElementTypeOffset(type) * 17, 17 - progress, 17, progress);
		if (showDebugInfo()) {
			mc.fontRenderer.drawStringWithShadow(amount + "/" + max, x, y + 17, 16777215);
		}
	}

	public static boolean showDebugInfo() {
		Minecraft minecraft = Minecraft.getInstance();

		return minecraft.player.isCreative() && minecraft.gameSettings.advancedItemTooltips;
	}
}
