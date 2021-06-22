package sirttas.elementalcraft.block.diffuser;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.renderer.AbstractECRenderer;

@OnlyIn(Dist.CLIENT)
public class DiffuserRenderer extends AbstractECRenderer<DiffuserBlockEntity> {
	
	public static final ResourceLocation CUBE_LOCATION = ElementalCraft.createRL("block/diffuser_cube");
	
	private static final Quaternion ROTATION = Vector3f.XP.rotationDegrees(45);
	static {
		ROTATION.mul(Vector3f.ZP.rotationDegrees(45));
	}
	
	private IBakedModel cubeModel;
	
	public DiffuserRenderer(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	@Override
	public void render(DiffuserBlockEntity te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
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
