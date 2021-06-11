package sirttas.elementalcraft.block.shrine;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.entity.renderer.AbstractECRenderer;

@OnlyIn(Dist.CLIENT)
public class RendererShrine<T extends AbstractShrineBlockEntity> extends AbstractECRenderer<T> {

	public RendererShrine(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(T shrine, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		if (shrine.showsRange()) {
			BlockPos pos = shrine.getBlockPos();
			
			WorldRenderer.renderLineBox(matrixStackIn, bufferIn.getBuffer(RenderType.lines()), shrine.getRangeBoundingBox().move(new BlockPos(-pos.getX(), -pos.getY(), -pos.getZ())), 1, 1,
					0.6F, 1);
		}

	}

}