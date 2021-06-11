package sirttas.elementalcraft.block.pipe;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.AxisAlignedBB;
import sirttas.elementalcraft.block.entity.renderer.AbstractECRenderer;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock.CoverType;

public class ElementPipeRenderer extends AbstractECRenderer<ElementPipeBlockEntity> {

	private static final AxisAlignedBB BOX = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	
	public ElementPipeRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@SuppressWarnings("resource")
	@Override
	public void render(ElementPipeBlockEntity te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
		BlockState coverState = te.getCoverState();

		if (coverState != null) {
			if (ElementPipeBlock.showCover(te.getBlockState(), Minecraft.getInstance().player)) {
				renderBlock(coverState, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
			} else {
				renderBlock(te.getBlockState().setValue(ElementPipeBlock.COVER, CoverType.NONE), matrixStack, buffer, combinedLightIn, combinedOverlayIn);
				WorldRenderer.renderLineBox(matrixStack, buffer.getBuffer(RenderType.lines()), BOX, 0F, 0F, 0F, 1);
			}
		}
	}
}
