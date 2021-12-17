package sirttas.elementalcraft.item.spell.book;

import java.text.MessageFormat;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import sirttas.elementalcraft.config.ECConfig;

import javax.annotation.Nonnull;

public class SpellBookScreen extends AbstractContainerScreen<SpellBookMenu> implements MenuAccess<SpellBookMenu> {
	private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");

	public SpellBookScreen(SpellBookMenu container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
		this.passEvents = false;
		this.imageHeight = 114 + SpellBookMenu.ROW_COUNT * 18;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(@Nonnull PoseStack matrixStack, int x, int y) {
		Component text = new TextComponent(MessageFormat.format("{0}/{1}", this.menu.getSpellCount(), ECConfig.COMMON.spellBookMaxSpell.get()));

		super.renderLabels(matrixStack, x, y);
		this.font.draw(matrixStack, text, (float) this.imageWidth - this.font.width(text.getVisualOrderText()) - this.titleLabelX, this.titleLabelY, 4210752);

	}

	@Override
	protected void renderBg(@Nonnull PoseStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, CHEST_GUI_TEXTURE);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, i, j, 0, 0, this.imageWidth, SpellBookMenu.ROW_COUNT * 18 + 17);
		this.blit(matrixStack, i, j + SpellBookMenu.ROW_COUNT * 18 + 17, 0, 126, this.imageWidth, 96);
	}
}
