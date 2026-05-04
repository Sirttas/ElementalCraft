package sirttas.elementalcraft.item.spell.book;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import sirttas.elementalcraft.config.ECConfig;

import java.text.MessageFormat;

public class SpellBookScreen extends AbstractContainerScreen<@NotNull SpellBookMenu> implements MenuAccess<@NotNull SpellBookMenu> {
	private static final Identifier CHEST_GUI_TEXTURE = Identifier.withDefaultNamespace("textures/gui/container/generic_54.png");

	public SpellBookScreen(SpellBookMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title, 176, 114 + SpellBookMenu.ROW_COUNT * 18);
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractBackground(graphics, mouseX, mouseY, a);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		graphics.blit(RenderPipelines.GUI_TEXTURED, CHEST_GUI_TEXTURE, i, j, 0, 0, this.imageWidth, SpellBookMenu.ROW_COUNT * 18 + 17, 256, 256);
		graphics.blit(RenderPipelines.GUI_TEXTURED, CHEST_GUI_TEXTURE, i, j + SpellBookMenu.ROW_COUNT * 18 + 17, 0, 126, this.imageWidth, 96, 256, 256);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
		Component text = Component.literal(MessageFormat.format("{0}/{1}", this.menu.getSpellCount(), ECConfig.SERVER.spellBookMaxSpell.get()));

		super.extractLabels(graphics, xm, ym);
		graphics.text(font, text, this.imageWidth - this.font.width(text.getVisualOrderText()) - this.titleLabelX, this.titleLabelY, 4210752);
	}
}
