package sirttas.elementalcraft.block.diffuser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class DiffuserRenderer implements BlockEntityRenderer<DiffuserBlockEntity> {
	
	public static final ResourceLocation CUBE_LOCATION = ElementalCraftApi.createRL("block/diffuser_cube");
	
	private static final Quaternionf ROTATION = Axis.XP.rotationDegrees(45);
	static {
		ROTATION.mul(Axis.ZP.rotationDegrees(45));
	}
	
	private BakedModel cubeModel;
	
	@Override
	public void render(@Nonnull DiffuserBlockEntity te, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		float angle = ECRendererHelper.getClientTicks(partialTicks);
		
		if (cubeModel == null) {
			cubeModel = Minecraft.getInstance().getModelManager().getModel(CUBE_LOCATION);
		}
		ECRendererHelper.renderRunes(matrixStack, buffer, te.getRuneHandler(), angle, light, overlay);
		matrixStack.pushPose();
		matrixStack.translate(0.5, 1.1, 0.5);
		matrixStack.mulPose(Axis.YP.rotationDegrees(angle));
		matrixStack.mulPose(ROTATION);
		matrixStack.translate(-3D / 16, -3D / 16, -3D / 16);
		ECRendererHelper.renderModel(cubeModel, matrixStack, buffer, te, light, overlay);
		matrixStack.popPose();
	}
}
