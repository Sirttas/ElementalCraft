package sirttas.elementalcraft.block.shrine;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.tile.renderer.AbstractRendererEC;

@OnlyIn(Dist.CLIENT)
public class RendererShrine<T extends AbstractTileShrine> extends AbstractRendererEC<T> {

	public RendererShrine(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(T shrine, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		if (shrine.showsRange()) {
			BlockPos pos = shrine.getPos();
			
			WorldRenderer.drawBoundingBox(matrixStackIn, bufferIn.getBuffer(RenderType.getLines()), shrine.getRangeBoundingBox().offset(new BlockPos(-pos.getX(), -pos.getY(), -pos.getZ())), 1, 1,
					0.6F, 1);
		}

	}

}