package sirttas.elementalcraft.block.synthesizer.vibration;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class VibrationSynthesizerRenderer implements BlockEntityRenderer<VibrationSynthesizerBlockEntity> {

	@Override
	public void render(VibrationSynthesizerBlockEntity vibrationSynthesizer, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource bufferSource, int light, int overlay) {
		ECRendererHelper.renderRunes(poseStack, bufferSource, vibrationSynthesizer.getRuneHandler(), ECRendererHelper.getClientTicks(partialTicks), light, overlay);

		if (vibrationSynthesizer.showsRange()) {
			BlockPos pos = vibrationSynthesizer.getBlockPos();

			LevelRenderer.renderLineBox(poseStack, bufferSource.getBuffer(RenderType.lines()), vibrationSynthesizer.getRange().move(-pos.getX(), -pos.getY(), -pos.getZ()), 1, 1, 0.6F, 1);
		}
	}
}
