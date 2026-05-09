package sirttas.elementalcraft.item.source.analysis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.container.menu.screen.IRefreshedScreen;

import javax.annotation.Nonnull;

public class SourceAnalysisGlassScreen extends AbstractContainerScreen<@NotNull SourceAnalysisGlassMenu> implements MenuAccess<@NotNull SourceAnalysisGlassMenu>, IRefreshedScreen {
	
	private static final Identifier SOURCE_ANALYSIS_GLASS_GUI_TEXTURE = ElementalCraftApi.identifier("textures/gui/container/source_analysis_glass.png");

	private TraitsList traitsList;

	public SourceAnalysisGlassScreen(SourceAnalysisGlassMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title, 176, 180);
		inventoryLabelY += 14;
	}

	@Override
	protected void init() {
		super.init();
		this.traitsList = new TraitsList(this.minecraft);
		this.addRenderableWidget(this.traitsList);
	}

	@Override
	public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractBackground(graphics, mouseX, mouseY, a);
		graphics.blit(RenderPipelines.GUI_TEXTURED, SOURCE_ANALYSIS_GLASS_GUI_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
	}

	@Override
	public void refresh() {
		traitsList.refresh();
	}

	private class TraitsList extends ObjectSelectionList<TraitsList.@NotNull Entry> {

		private static final int WIDTH = 158;
		private static final int HEIGHT = 62;

		public TraitsList(Minecraft minecraft) {
			super(minecraft, WIDTH, HEIGHT, topPos + titleLabelY + 13, 11);

			this.setX(leftPos + titleLabelX + 2);
		}

		@Override
		public int getRowWidth() {
			return WIDTH - 7;
		}

		@Override
		protected int scrollBarX() {
			return this.getRight() - 7;
		}

        @Override
        public int getRowTop(int index) {
			return super.getRowTop(index) - 4;
		}

		@Override
		public void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
			super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
			extractBorders(graphics);
		}

		private void extractBorders(@NonNull GuiGraphicsExtractor graphics) {
			var poseStack = graphics.pose();

			poseStack.pushMatrix();
			graphics.blit(RenderPipelines.GUI_TEXTURED, SOURCE_ANALYSIS_GLASS_GUI_TEXTURE, this.getX(), this.getY() - 11, 0, imageHeight, WIDTH, 11, 256, 256);
			poseStack.rotate((float) Math.PI);
			graphics.blit(RenderPipelines.GUI_TEXTURED, SOURCE_ANALYSIS_GLASS_GUI_TEXTURE, -this.getRight(), -this.getBottom() -11, 0, imageHeight, WIDTH, 11, 256, 256);
			poseStack.popMatrix();
		}

		public void refresh() {
			this.clearEntries();
			menu.getTraits().values().forEach(value -> addEntry(new TraitsList.Entry(value)));
		}

		private class Entry extends ObjectSelectionList.Entry<@NotNull Entry> {

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
            public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
                graphics.text(font, getNarration(), getContentX(), getContentY(), -1);
            }
		}
	}
}
