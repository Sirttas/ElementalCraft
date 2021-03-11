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
		this.ySize = 114 + SpellBookContainer.ROW_COUNT * 18;
		this.playerInventoryTitleY = this.ySize - 94;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
		ITextComponent text = new StringTextComponent(MessageFormat.format("{0}/{1}", this.container.getSpellCount(), ECConfig.COMMON.spellBookMaxSpell.get()));

		super.drawGuiContainerForegroundLayer(matrixStack, x, y);
		this.font.drawText(matrixStack, text, (float) this.xSize - this.font.func_243245_a(text.func_241878_f()) - this.titleX, this.titleY, 4210752);

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.blit(matrixStack, i, j, 0, 0, this.xSize, SpellBookContainer.ROW_COUNT * 18 + 17);
		this.blit(matrixStack, i, j + SpellBookContainer.ROW_COUNT * 18 + 17, 0, 126, this.xSize, 96);
	}
}