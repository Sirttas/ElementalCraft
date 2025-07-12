package sirttas.elementalcraft.block.diffuser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import sirttas.elementalcraft.client.model.ECModelHelper;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class DiffuserRenderer implements BlockEntityRenderer<DiffuserBlockEntity> {
	
	public static final ModelResourceLocation CUBE_LOCATION = ECModelHelper.standalone("block/diffuser_cube");
	
	private static final Quaternionf ROTATION = Axis.XP.rotationDegrees(45);
	static {
		ROTATION.mul(Axis.ZP.rotationDegrees(45));
	}
	
	private BakedModel cubeModel;
	
	@Override
	public void render(@Nonnull DiffuserBlockEntity diffuser, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource bufferSource, int light, int overlay) {
		if (diffuser.showsRange()) {
			BlockPos pos = diffuser.getBlockPos();

			LevelRenderer.renderLineBox(poseStack, bufferSource.getBuffer(RenderType.lines()), diffuser.getRange().move(-pos.getX(), -pos.getY(), -pos.getZ()), 1, 1, 0.6F, 1);
		}

		float angle = ECRendererHelper.getClientTicks(partialTicks);

		if (cubeModel == null) {
			cubeModel = Minecraft.getInstance().getModelManager().getModel(CUBE_LOCATION);
		}
		ECRendererHelper.renderRunes(poseStack, bufferSource, diffuser.getRuneHandler(), angle, light, overlay);
		poseStack.pushPose();
		poseStack.translate(0.5, 1.1, 0.5);
		poseStack.mulPose(Axis.YP.rotationDegrees(angle));
		poseStack.mulPose(ROTATION);
		poseStack.translate(-3D / 16, -3D / 16, -3D / 16);
		ECRendererHelper.renderModel(cubeModel, poseStack, bufferSource, diffuser, light, overlay);
		poseStack.popPose();
	}
}
