package sirttas.elementalcraft.block.spelldesk;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class SpellDeskScreen extends AbstractContainerScreen<SpellDeskMenu> implements MenuAccess<SpellDeskMenu> {
	
	private static final Identifier SPELL_DESK_GUI_TEXTURE = ElementalCraftApi.identifier("textures/gui/container/spell_desk.png");

	private Button previous;
	private Button next;

	public SpellDeskScreen(SpellDeskMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
	}

	@Override
	protected void init() {
		super.init();
		previous = Button.builder(Component.literal("<"), b -> menu.previousPage())
				.pos(leftPos + 107, topPos + 13)
				.size(10, 20)
				.build();
		next = Button.builder(Component.literal(">"), b -> menu.nextPage())
				.pos(leftPos + 151, topPos + 13)
				.size(10, 20)
				.build();
		addRenderableOnly(previous);
		addRenderableOnly(next);
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractRenderState(graphics, mouseX, mouseY, a);
		var page = menu.getPage();
		var pageCount = menu.getPageCount();

		previous.active = page > 0;
		next.active = page < pageCount - 1;
		Component pages = Component.literal(String.format("%d / %d", page + 1, pageCount));

		graphics.text(font, pages.getVisualOrderText(), leftPos + 136 - Math.round(font.width(pages) / 2F), topPos + 23 - Math.round(font.lineHeight / 2F), 4210752, false);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractBackground(graphics, mouseX, mouseY, a);
		graphics.blit(RenderPipelines.GUI_TEXTURED, SPELL_DESK_GUI_TEXTURE,  this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
	}
}
