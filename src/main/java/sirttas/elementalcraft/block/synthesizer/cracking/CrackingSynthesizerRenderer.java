package sirttas.elementalcraft.block.synthesizer.cracking;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelIdentifier;
import net.minecraft.core.BlockPos;
import sirttas.elementalcraft.client.model.ECModelHelper;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class CrackingSynthesizerRenderer<T extends AbstractCrackingSynthesizerBlockEntity<?>> implements BlockEntityRenderer<T> {

	public static final ModelIdentifier HEAD_LOCATION = ECModelHelper.standalone("block/cracking_earth_synthesizer_head");

	private BakedModel headModel;

	@Override
	public void render(T crackingSynthesizer, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource bufferSource, int light, int overlay) {
		ECRendererHelper.renderRunes(poseStack, bufferSource, crackingSynthesizer.getRuneHandler(), ECRendererHelper.getClientTicks(partialTicks), light, overlay);

		Minecraft minecraft = Minecraft.getInstance();

		if (crackingSynthesizer.showsRange()) {
			BlockPos pos = crackingSynthesizer.getBlockPos();

			LevelRenderer.renderLineBox(poseStack, bufferSource.getBuffer(RenderType.lines()), crackingSynthesizer.getRange().move(-pos.getX(), -pos.getY(), -pos.getZ()), 1, 1, 0.6F, 1);
		}
		if (headModel == null) {
			headModel = minecraft.getModelManager().getModel(HEAD_LOCATION);
		}
		if (crackingSynthesizer.isWorking()) {
			poseStack.translate(0.5, 0, 0.5);
			poseStack.mulPose(Axis.YP.rotationDegrees(-5 * ECRendererHelper.getClientTicks(partialTicks)));
			poseStack.translate(-0.5, 0, -0.5);
		}
		ECRendererHelper.renderModel(headModel, poseStack, bufferSource, crackingSynthesizer, light, overlay);
	}

}
