package sirttas.elementalcraft.item.source.analysis;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import sirttas.elementalcraft.ElementalCraft;

import javax.annotation.Nonnull;

public class SourceAnalysisGlassScreen extends AbstractContainerScreen<SourceAnalysisGlassMenu> implements MenuAccess<SourceAnalysisGlassMenu> {
	
	private static final ResourceLocation SOURCE_ANALYSIS_GLASS_GUI_TEXTURE = ElementalCraft.createRL("textures/gui/container/source_analysis_glass.png");

	public SourceAnalysisGlassScreen(SourceAnalysisGlassMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
		imageHeight = 180;
		inventoryLabelY += 14;
	}
	
	@Override
	public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderBg(@Nonnull PoseStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, SOURCE_ANALYSIS_GLASS_GUI_TEXTURE);
		this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
	}
	
	@Override
	protected void renderLabels(@Nonnull PoseStack poseStack, int x, int y) {
		var traits = this.menu.getTraits();
		int index = 0;
		
		super.renderLabels(poseStack, x, y);
		for (var value : traits.values()) {
			this.font.draw(poseStack, value.getDescription(), this.titleLabelX + 4f, this.titleLabelY + index * 11F + 15F, -1);
			index++;
		}
	}
}
