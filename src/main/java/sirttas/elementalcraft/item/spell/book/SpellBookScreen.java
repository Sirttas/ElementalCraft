package sirttas.elementalcraft.item.spell.book;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import sirttas.elementalcraft.config.ECConfig;

import javax.annotation.Nonnull;
import java.text.MessageFormat;

public class SpellBookScreen extends AbstractContainerScreen<SpellBookMenu> implements MenuAccess<SpellBookMenu> {
	private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");

	public SpellBookScreen(SpellBookMenu container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
		this.imageHeight = 114 + SpellBookMenu.ROW_COUNT * 18;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(@Nonnull GuiGraphics guiGraphics, int x, int y) {
		Component text = Component.literal(MessageFormat.format("{0}/{1}", this.menu.getSpellCount(), ECConfig.COMMON.spellBookMaxSpell.get()));

		super.renderLabels(guiGraphics, x, y);
		guiGraphics.drawString(font, text, this.imageWidth - this.font.width(text.getVisualOrderText()) - this.titleLabelX, this.titleLabelY, 4210752);

	}

	@Override
	protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTicks, int x, int y) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		guiGraphics.blit(CHEST_GUI_TEXTURE, i, j, 0, 0, this.imageWidth, SpellBookMenu.ROW_COUNT * 18 + 17);
		guiGraphics.blit(CHEST_GUI_TEXTURE, i, j + SpellBookMenu.ROW_COUNT * 18 + 17, 0, 126, this.imageWidth, 96);
	}
}
