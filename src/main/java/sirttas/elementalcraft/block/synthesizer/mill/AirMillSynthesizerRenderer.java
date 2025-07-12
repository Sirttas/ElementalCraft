package sirttas.elementalcraft.block.synthesizer.mill;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import sirttas.elementalcraft.client.model.ECModelHelper;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class AirMillSynthesizerRenderer implements BlockEntityRenderer<AirMillSynthesizerBlockEntity> {

	public static final ModelResourceLocation SHAFT_LOCATION = ECModelHelper.standalone("block/air_mill_synthesizer_shaft");

	private BakedModel shaftModel;

	@Override
	public void render(AirMillSynthesizerBlockEntity airMillSynthesizer, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource bufferSource, int light, int overlay) {
		ECRendererHelper.renderRunes(poseStack, bufferSource, airMillSynthesizer.getRuneHandler(), ECRendererHelper.getClientTicks(partialTicks), light, overlay);

		Minecraft minecraft = Minecraft.getInstance();

		if (airMillSynthesizer.isBroken()) {
			return;
		}

		if (shaftModel == null) {
			shaftModel = minecraft.getModelManager().getModel(SHAFT_LOCATION);
		}
		if (airMillSynthesizer.isWorking()) {
			poseStack.translate(0.5, 0, 0.5);
			poseStack.mulPose(Axis.YP.rotationDegrees(-5 * ECRendererHelper.getClientTicks(partialTicks)));
			poseStack.translate(-0.5, 0, -0.5);
		}
		ECRendererHelper.renderModel(shaftModel, poseStack, bufferSource, airMillSynthesizer, light, overlay);
	}
}
