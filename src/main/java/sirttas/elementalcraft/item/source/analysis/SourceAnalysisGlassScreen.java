package sirttas.elementalcraft.item.source.analysis;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.container.menu.screen.IRefreshedScreen;

import javax.annotation.Nonnull;

public class SourceAnalysisGlassScreen extends AbstractContainerScreen<SourceAnalysisGlassMenu> implements MenuAccess<SourceAnalysisGlassMenu>, IRefreshedScreen {
	
	private static final ResourceLocation SOURCE_ANALYSIS_GLASS_GUI_TEXTURE = ElementalCraft.createRL("textures/gui/container/source_analysis_glass.png");

	private TraitsList traitsList;

	public SourceAnalysisGlassScreen(SourceAnalysisGlassMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
		imageHeight = 180;
		inventoryLabelY += 14;
	}

	@Override
	protected void init() {
		super.init();
		this.traitsList = new TraitsList(this.minecraft);
		this.addRenderableWidget(this.traitsList);
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
	public void refresh() {
		traitsList.refresh();
	}

	private class TraitsList extends ObjectSelectionList<TraitsList.Entry> {

		private static final int WIDTH = 158;
		private static final int HEIGHT = 62;

		public TraitsList(Minecraft minecraft) {
			super(minecraft, WIDTH, HEIGHT, topPos + titleLabelY + 13, topPos + titleLabelY + 13 + HEIGHT, 11);

			this.setLeftPos(leftPos + titleLabelX + 2);
			this.setRenderBackground(false);
			this.setRenderTopAndBottom(false);
		}

		@Override
		public int getRowWidth() {
			return WIDTH - 7;
		}

		@Override
		protected int getScrollbarPosition() {
			return x1 - 7;
		}

		@Override
		protected int getRowTop(int index) {
			return super.getRowTop(index) - 4;
		}

		@Override
		protected void renderDecorations(@Nonnull PoseStack poseStack, int x, int y) {
			poseStack.pushPose();
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, SOURCE_ANALYSIS_GLASS_GUI_TEXTURE);
			this.blit(poseStack, this.x0, this.y0 - 11, 0, imageHeight, WIDTH, 11);
			poseStack.mulPose(Vector3f.ZP.rotationDegrees(180));
			this.blit(poseStack, -this.x1, -this.y1 -11, 0, imageHeight, WIDTH, 11);
			poseStack.popPose();
		}

		public void refresh() {
			this.clearEntries();
			menu.getTraits().values().forEach(value -> addEntry(new TraitsList.Entry(value)));
		}

		private class Entry extends ObjectSelectionList.Entry<Entry> {

			private final ISourceTraitValue value;

			private Entry(ISourceTraitValue value) {
				this.value = value;
			}

			@Nonnull
			@Override
			public Component getNarration() {
				return value.getDescription();
			}

			@Override
			public void render(@Nonnull PoseStack poseStack, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float partialTick) {
				font.draw(poseStack, getNarration(), left, top, -1);
			}
		}
	}
}
