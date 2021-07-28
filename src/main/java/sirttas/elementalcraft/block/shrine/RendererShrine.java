package sirttas.elementalcraft.block.shrine;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.entity.renderer.IECRenderer;

@OnlyIn(Dist.CLIENT)
public class RendererShrine<T extends AbstractShrineBlockEntity> implements IECRenderer<T> {

	public RendererShrine(Context context) {}

	@Override
	public void render(T shrine, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
		if (shrine.showsRange()) {
			BlockPos pos = shrine.getBlockPos();
			
			LevelRenderer.renderLineBox(matrixStackIn, bufferIn.getBuffer(RenderType.lines()), shrine.getRangeBoundingBox().move(new BlockPos(-pos.getX(), -pos.getY(), -pos.getZ())), 1, 1,
					0.6F, 1);
		}

	}

}