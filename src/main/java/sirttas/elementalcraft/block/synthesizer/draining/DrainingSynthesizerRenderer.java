package sirttas.elementalcraft.block.synthesizer.draining;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class DrainingSynthesizerRenderer implements BlockEntityRenderer<DrainingSynthesizerBlockEntity> {

	@Override
	public void render(DrainingSynthesizerBlockEntity crackingSynthesizer, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource bufferSource, int light, int overlay) {
		ECRendererHelper.renderRunes(poseStack, bufferSource, crackingSynthesizer.getRuneHandler(), ECRendererHelper.getClientTicks(partialTicks), light, overlay);
	}

}
