package sirttas.elementalcraft.block.source;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class SourceRenderer implements BlockEntityRenderer<SourceBlockEntity> {

	public static final ResourceLocation STABILIZER_LOCATION = ElementalCraftApi.createRL("block/source_stabilizer");
	
	private BakedModel stabilizerModel;

	@SuppressWarnings("resource")
	@Override
	public void render(@Nonnull SourceBlockEntity source, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		Minecraft minecraft = Minecraft.getInstance();
		ElementType elementType = source.getElementType();
		float r = elementType.getRed();
		float g = elementType.getGreen();
		float b = elementType.getBlue();

		if (stabilizerModel == null) {
			stabilizerModel = minecraft.getModelManager().getModel(STABILIZER_LOCATION);
		}

		SourceRendererHelper.renderSource(poseStack, buffer, partialTicks, light, overlay, elementType, source.isExhausted(), source.getRemainingRatio());
		if (source.isStabilized()) {
			poseStack.translate(0.5, 0, 0.5);
			poseStack.mulPose(Axis.YP.rotation(ECRendererHelper.getClientTicks(partialTicks) / 20F));
			poseStack.translate(-0.5, 0, -0.5);
			poseStack.translate(0, -1 / 4D, 0);
			minecraft.getBlockRenderer().getModelRenderer().renderModel(poseStack.last(), buffer.getBuffer(RenderType.translucent()), source.getBlockState(), stabilizerModel, r, g, b, light, overlay, ECRendererHelper.getModelData(stabilizerModel, source), RenderType.translucent());
		}
	}

}
