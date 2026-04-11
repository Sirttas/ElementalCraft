package sirttas.elementalcraft.item.spell.book;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class SpellBookScreen extends AbstractContainerScreen<@NotNull SpellBookMenu> implements MenuAccess<@NotNull SpellBookMenu> {
	private static final Identifier CHEST_GUI_TEXTURE = Identifier.withDefaultNamespace("textures/gui/container/generic_54.png");

	public SpellBookScreen(SpellBookMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title, 176, 114 + SpellBookMenu.ROW_COUNT * 18);
		this.inventoryLabelY = this.imageHeight - 94;
	}

    /* TODO

	@Override
	public void render(@Nonnull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(@Nonnull GuiGraphicsExtractor guiGraphics, int x, int y) {
		Component text = Component.literal(MessageFormat.format("{0}/{1}", this.menu.getSpellCount(), ECConfig.SERVER.spellBookMaxSpell.get()));

		super.renderLabels(guiGraphics, x, y);
		guiGraphics.drawString(font, text, this.imageWidth - this.font.width(text.getVisualOrderText()) - this.titleLabelX, this.titleLabelY, 4210752);

	}

	@Override
	protected void renderBg(@Nonnull GuiGraphicsExtractor guiGraphics, float partialTicks, int x, int y) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		guiGraphics.blit(CHEST_GUI_TEXTURE, i, j, 0, 0, this.imageWidth, SpellBookMenu.ROW_COUNT * 18 + 17);
		guiGraphics.blit(CHEST_GUI_TEXTURE, i, j + SpellBookMenu.ROW_COUNT * 18 + 17, 0, 126, this.imageWidth, 96);
	}

     */
}
