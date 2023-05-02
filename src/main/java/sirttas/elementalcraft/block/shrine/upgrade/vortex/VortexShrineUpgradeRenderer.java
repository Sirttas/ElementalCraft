package sirttas.elementalcraft.block.shrine.upgrade.vortex;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class VortexShrineUpgradeRenderer implements BlockEntityRenderer<VortexShrineUpgradeBlockEntity> {

	public static final ResourceLocation RING_LOCATION = ElementalCraft.createRL("block/shrine_upgrade_vortex_ring");

	private BakedModel ringModel;

	@Override
	public void render(@Nonnull VortexShrineUpgradeBlockEntity te, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		float angle = ECRendererHelper.getClientTicks(partialTicks);

		if (ringModel == null) {
			ringModel = Minecraft.getInstance().getModelManager().getModel(RING_LOCATION);
		}
		matrixStack.translate(0.5, 11D / 16, 0.5);
		matrixStack.pushPose();
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(angle));
		matrixStack.translate(-4D / 16, -3D / 16, -4D / 16);
		ECRendererHelper.renderModel(ringModel, matrixStack, buffer, te, light, overlay);
		matrixStack.popPose();

		matrixStack.pushPose();
		matrixStack.translate(0, 2D / 16, 0);
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(-angle));
		matrixStack.translate(-4D / 16, -3D / 16, -4D / 16);
		ECRendererHelper.renderModel(ringModel, matrixStack, buffer, te, light, overlay);
		matrixStack.popPose();
	}
	
}
