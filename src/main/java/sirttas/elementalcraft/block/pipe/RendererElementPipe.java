package sirttas.elementalcraft.block.pipe;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.AxisAlignedBB;
import sirttas.elementalcraft.block.pipe.BlockElementPipe.CoverType;
import sirttas.elementalcraft.block.tile.renderer.AbstractRendererEC;

public class RendererElementPipe extends AbstractRendererEC<TileElementPipe> {

	private static final AxisAlignedBB BOX = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	
	public RendererElementPipe(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@SuppressWarnings("resource")
	@Override
	public void render(TileElementPipe te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
		BlockState coverState = te.getCoverState();

		if (coverState != null) {
			if (BlockElementPipe.showCover(te.getBlockState(), Minecraft.getInstance().player)) {
				renderBlock(coverState, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
			} else {
				renderBlock(te.getBlockState().with(BlockElementPipe.COVER, CoverType.NONE), matrixStack, buffer, combinedLightIn, combinedOverlayIn);
				WorldRenderer.drawBoundingBox(matrixStack, buffer.getBuffer(RenderType.getLines()), BOX, 0F, 0F, 0F, 1);
			}
		}
	}
}
