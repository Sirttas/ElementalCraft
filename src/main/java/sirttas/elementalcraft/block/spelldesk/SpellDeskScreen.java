package sirttas.elementalcraft.block.spelldesk;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import sirttas.elementalcraft.ElementalCraft;

import javax.annotation.Nonnull;

public class SpellDeskScreen extends AbstractContainerScreen<SpellDeskMenu> implements MenuAccess<SpellDeskMenu> {
	
	private static final ResourceLocation SPELL_DESK_GUI_TEXTURE = ElementalCraft.createRL("textures/gui/container/spell_desk.png");

	private Button previous;
	private Button next;


	public SpellDeskScreen(SpellDeskMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
	}
	@Override
	protected void init() {
		super.init();
		previous = new Button(leftPos + 107, topPos + 13, 10, 20, Component.literal("<"), b -> menu.previousPage());
		next = new Button(leftPos + 151, topPos + 13, 10, 20, Component.literal(">"), b -> menu.nextPage());
		addRenderableOnly(previous);
		addRenderableOnly(next);
	}
	
	@Override
	public void render(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(poseStack, mouseX, mouseY);

		var page = menu.getPage();
		var pageCount = menu.getPageCount();

		previous.active = page > 0;
		next.active = page < pageCount - 1;
		Component pages = Component.literal(String.format("%d / %d", page + 1, pageCount));

		font.draw(poseStack, pages.getVisualOrderText(), leftPos + 136 - (font.width(pages) / 2F), topPos + 23 - (font.lineHeight / 2F), 4210752);
	}

	@Override
	protected void renderBg(@Nonnull PoseStack poseStack, float partialTicks, int x, int y) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, SPELL_DESK_GUI_TEXTURE);
		this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
	}
}
