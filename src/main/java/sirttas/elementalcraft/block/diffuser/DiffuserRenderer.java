package sirttas.elementalcraft.block.diffuser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.renderer.IECRenderer;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class DiffuserRenderer implements IECRenderer<DiffuserBlockEntity> {
	
	public static final ResourceLocation CUBE_LOCATION = ElementalCraft.createRL("block/diffuser_cube");
	
	private static final Quaternion ROTATION = Vector3f.XP.rotationDegrees(45);
	static {
		ROTATION.mul(Vector3f.ZP.rotationDegrees(45));
	}
	
	private BakedModel cubeModel;
	
	@Override
	public void render(@Nonnull DiffuserBlockEntity te, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		float angle = getAngle(partialTicks);
		
		if (cubeModel == null) {
			cubeModel = Minecraft.getInstance().getModelManager().getModel(CUBE_LOCATION);
		}
		renderRunes(matrixStack, buffer, te.getRuneHandler(), angle, light, overlay);
		matrixStack.pushPose();
		matrixStack.translate(0.5, 1.1, 0.5);
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(angle));
		matrixStack.mulPose(ROTATION);
		matrixStack.translate(-3D / 16, -3D / 16, -3D / 16);
		renderModel(cubeModel, matrixStack, buffer, te, light, overlay);
		matrixStack.popPose();
	}
}
