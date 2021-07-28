package sirttas.elementalcraft.entity.boss.earthgolem;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class EarthGolemRenderer extends MobRenderer<EarthGolemEntity, EarthGolemModel> {

	private static final ResourceLocation TEXTURES = new ResourceLocation("textures/entity/iron_golem/iron_golem.png");

	private static final float SIZE = 5F;

	public EarthGolemRenderer(Context context) {
		super(context, new EarthGolemModel(), 5F);
	}

	@Override
	public ResourceLocation getTextureLocation(EarthGolemEntity entity) {
		return TEXTURES;
	}

	@Override
	public void render(EarthGolemEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
		matrixStack.scale(SIZE, SIZE, SIZE);
		super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
		matrixStack.scale(1F / SIZE, 1F / SIZE, 1F / SIZE);
	}
}
