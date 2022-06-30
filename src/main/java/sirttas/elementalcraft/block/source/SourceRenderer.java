package sirttas.elementalcraft.block.source;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.renderer.ECRenderTypes;
import sirttas.elementalcraft.block.entity.renderer.IECRenderer;

import javax.annotation.Nonnull;

public class SourceRenderer implements IECRenderer<SourceBlockEntity> {

	public static final Material OUTER = ForgeHooksClient.getBlockMaterial(ElementalCraft.createRL("effect/source_outer"));
	public static final Material MIDDLE = ForgeHooksClient.getBlockMaterial(ElementalCraft.createRL("effect/source_middle"));

	public static final ResourceLocation STABILIZER_LOCATION = ElementalCraft.createRL("block/source_stabilizer");
	
	private BakedModel stabilizerModel;

	@SuppressWarnings("resource")
	@Override
	public void render(@Nonnull SourceBlockEntity source, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		Minecraft minecraft = Minecraft.getInstance();
		ElementType elementType = source.getElementType();
		float r = elementType.getRed();
		float g = elementType.getGreen();
		float b = elementType.getBlue();
		var angle = -(getClientTicks(partialTicks) % 360);

		if (stabilizerModel == null) {
			stabilizerModel = minecraft.getModelManager().getModel(STABILIZER_LOCATION);
		}

		matrixStack.pushPose();
		matrixStack.translate(0.5, 0.3, 0.5);
		matrixStack.mulPose(minecraft.getEntityRenderDispatcher().camera.rotation());
		matrixStack.scale(0.016F, 0.016F, 0.016F);
		matrixStack.mulPose(Vector3f.ZP.rotationDegrees(angle));
		matrixStack.translate(-16, -16, 0);
		if (source.isExhausted()) {
			this.renderIcon(matrixStack, buffer, 0, 0, OUTER, 32, 32, r * 0.5f, g * 0.5f, b * 0.5f, light, overlay);
		} else {
			this.renderIcon(matrixStack, buffer, 0, 0, OUTER, 32, 32, r, g, b, light, overlay);
			matrixStack.translate(16, 16, 0);
			matrixStack.mulPose(Vector3f.ZP.rotationDegrees(angle * 5f * source.getRemainingRatio()));
			matrixStack.translate(-16, -16, -0.01);
			this.renderIcon(matrixStack, MIDDLE.buffer(buffer, ECRenderTypes::source), 0, 0, MIDDLE, 32, 32, r, g, b, light, overlay);
		}
		matrixStack.popPose();

		if (source.isStabilized()) {
			matrixStack.translate(0.5, 0, 0.5);
			matrixStack.mulPose(Vector3f.YP.rotation(getClientTicks(partialTicks) / 20F));
			matrixStack.translate(-0.5, 0, -0.5);
			matrixStack.translate(0, -1 / 4D, 0);
			minecraft.getBlockRenderer().getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(RenderType.translucent()), source.getBlockState(), stabilizerModel, r, g, b, light,
					overlay, getModelData(stabilizerModel, source));
		}
	}
	
}
