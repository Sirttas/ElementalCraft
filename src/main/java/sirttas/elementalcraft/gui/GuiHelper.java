package sirttas.elementalcraft.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.config.ECConfig;

public class GuiHelper {

	private static final Identifier GAUGE = ElementalCraftApi.createRL("textures/gui/element_gauge.png");

	private GuiHelper() {}

	private static int getElementTypeOffset(ElementType type) {
		return switch (type) {
			case WATER -> 1;
			case FIRE -> 2;
			case EARTH -> 3;
			case AIR -> 4;
			default -> 0;
		};
	}

	public static void renderElementGauge(GuiGraphicsExtractor guiGraphics, Font font, int x, int y, int amount, int max, ElementType type) {
		renderElementGauge(guiGraphics, font, x, y, amount, max, type, true);
	}

	public static void renderElementGauge(GuiGraphicsExtractor guiGraphics, Font font, int x, int y, int amount, int max, ElementType type, boolean showDebugInfo) {
		blitGauge(guiGraphics, x, y, 0.0F, 0.0F, 16, 16);

		int progress = Math.max(0, (int) ((double) Math.min(amount, max) / (double) max * 16));

		if (progress <= 1 && amount > 0) {
			progress = 2;
		}
		blitGauge(guiGraphics, x, y + 16 - progress, getElementTypeOffset(type) * 16, 16 - progress + (ECConfig.CLIENT.usePaleElementGauge.get() ? 16 : 0), 16, progress);
		if (showDebugInfo() && showDebugInfo) {
			guiGraphics.text(font, amount + "/" + max, x, y + 16, -2039584, true);
		}
	}

	public static void renderCheck(GuiGraphicsExtractor guiGraphics, Check check, int x, int y) {
		blitGauge(guiGraphics, x, y, 0, 16 + check.offset, 6, 6);
	}

	public static boolean showDebugInfo() {
		Minecraft minecraft = Minecraft.getInstance();

		return minecraft.player.isCreative() && minecraft.options.advancedItemTooltips;
	}

	private static void blitGauge(GuiGraphicsExtractor guiGraphics, int x, int y, float u, float v, int width, int height) {
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GAUGE, x, y, u, v, width, height, 256, 256);
	}

	public enum Check {
		VALID(0),
		PAUSED(6),
		INVALID(12);

		private final int offset;

		Check(int offset) {
			this.offset = offset;
		}

		public int getOffset() {
			return offset;
		}
	}

}
