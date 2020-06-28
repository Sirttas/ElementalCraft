package sirttas.elementalcraft.entity.boss.earthgolem;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * Made with Blockbench 3.5.4
 */
public class EarthGolemModel extends SegmentedModel<EarthGolemEntity> {
	private final ModelRenderer ironGolemHead;
	private final ModelRenderer ironGolemBody;
	private final ModelRenderer ironGolemRightArm;
	private final ModelRenderer ironGolemLeftArm;

	public EarthGolemModel() {
		textureWidth = 128;
		textureHeight = 128;

		ironGolemHead = new ModelRenderer(this);
		ironGolemHead.setRotationPoint(0.0F, 9.0F, -2.0F);
		ironGolemHead.setTextureOffset(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F, 0.0F, false);

		ironGolemBody = new ModelRenderer(this);
		ironGolemBody.setRotationPoint(0.0F, 9.0F, 0.0F);
		ironGolemBody.setTextureOffset(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18.0F, 12.0F, 11.0F, 0.0F, false);
		ironGolemBody.setTextureOffset(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9.0F, 5.0F, 6.0F, 0.5F, false);

		ironGolemRightArm = new ModelRenderer(this);
		ironGolemRightArm.setRotationPoint(0.0F, 9.0F, 0.0F);
		setRotationAngle(ironGolemRightArm, -1.5708F, 0.0F, 0.0F);
		ironGolemRightArm.setTextureOffset(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F, 0.0F, false);

		ironGolemLeftArm = new ModelRenderer(this);
		ironGolemLeftArm.setRotationPoint(0.0F, 9.0F, 0.0F);
		setRotationAngle(ironGolemLeftArm, -1.5708F, 0.0F, 0.0F);
		ironGolemLeftArm.setTextureOffset(60, 58).addBox(9.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(EarthGolemEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// previously the render function, render code was moved to a method below
	}

	@Override
	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(this.ironGolemHead, this.ironGolemBody, this.ironGolemRightArm, this.ironGolemLeftArm);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}