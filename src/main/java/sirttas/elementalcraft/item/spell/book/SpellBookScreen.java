package sirttas.elementalcraft.item.spell.book;

import java.text.MessageFormat;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import sirttas.elementalcraft.config.ECConfig;

public class SpellBookScreen extends ContainerScreen<SpellBookContainer> implements IHasContainer<SpellBookContainer> {
	private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");

	public SpellBookScreen(SpellBookContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);
		this.passEvents = false;
		this.imageHeight = 114 + SpellBookContainer.ROW_COUNT * 18;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(MatrixStack matrixStack, int x, int y) {
		ITextComponent text = new StringTextComponent(MessageFormat.format("{0}/{1}", this.menu.getSpellCount(), ECConfig.COMMON.spellBookMaxSpell.get()));

		super.renderLabels(matrixStack, x, y);
		this.font.draw(matrixStack, text, (float) this.imageWidth - this.font.width(text.getVisualOrderText()) - this.titleLabelX, this.titleLabelY, 4210752);

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(CHEST_GUI_TEXTURE);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, i, j, 0, 0, this.imageWidth, SpellBookContainer.ROW_COUNT * 18 + 17);
		this.blit(matrixStack, i, j + SpellBookContainer.ROW_COUNT * 18 + 17, 0, 126, this.imageWidth, 96);
	}
}