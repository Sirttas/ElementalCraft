package sirttas.elementalcraft.entity.boss.earthgolem;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class EarthGolemRenderer extends MobRenderer<EarthGolemEntity, EarthGolemModel> {

	private static final ResourceLocation TEXTURES = new ResourceLocation("textures/entity/iron_golem/iron_golem.png");

	private static final float SIZE = 5F;

	public EarthGolemRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new EarthGolemModel(), 5F);
	}

	@Override
	public ResourceLocation getTextureLocation(EarthGolemEntity entity) {
		return TEXTURES;
	}

	@Override
	public void render(EarthGolemEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
		matrixStack.scale(SIZE, SIZE, SIZE);
		super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
		matrixStack.scale(1F / SIZE, 1F / SIZE, 1F / SIZE);
	}
}
