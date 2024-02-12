package sirttas.elementalcraft.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.config.ECConfig;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("resource")
public class GuiHelper {

	private static final ResourceLocation GAUGE = ElementalCraftApi.createRL("textures/gui/element_gauge.png");

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

	public static void renderElementGauge(GuiGraphics guiGraphics, Font font, int x, int y, int amount, int max, ElementType type) {
		renderElementGauge(guiGraphics, font, x, y, amount, max, type, true);
	}

	public static void renderElementGauge(GuiGraphics guiGraphics, Font font, int x, int y, int amount, int max, ElementType type, boolean showDebugInfo) {
		guiGraphics.blit(GAUGE, x, y, 0, 0, 16, 16);

		int progress = Math.max(0, (int) ((double) Math.min(amount, max) / (double) max * 16));

		if (progress <= 1 && amount > 0) {
			progress = 2;
		}
		guiGraphics.blit(GAUGE, x, y + 16 - progress, getElementTypeOffset(type) * 16, 16 - progress + (Boolean.TRUE.equals(ECConfig.CLIENT.usePaleElementGauge.get()) ? 16 : 0), 16, progress);
		if (showDebugInfo() && showDebugInfo) {
			guiGraphics.drawString(font, amount + "/" + max, x, y + 16, 16777215, true);
		}
	}

	public static void renderCheck(GuiGraphics guiGraphics, Check check, int x, int y) {
		guiGraphics.blit(GAUGE, x, y, 0, 16 + check.offset, 6, 6);
	}


	public static boolean showDebugInfo() {
		Minecraft minecraft = Minecraft.getInstance();

		return minecraft.player.isCreative() && minecraft.options.advancedItemTooltips;
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
