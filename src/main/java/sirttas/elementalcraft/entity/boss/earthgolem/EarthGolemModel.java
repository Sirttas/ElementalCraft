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
		texWidth = 128;
		texHeight = 128;

		ironGolemHead = new ModelRenderer(this);
		ironGolemHead.setPos(0.0F, 9.0F, -2.0F);
		ironGolemHead.texOffs(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F, 0.0F, false);

		ironGolemBody = new ModelRenderer(this);
		ironGolemBody.setPos(0.0F, 9.0F, 0.0F);
		ironGolemBody.texOffs(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18.0F, 12.0F, 11.0F, 0.0F, false);
		ironGolemBody.texOffs(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9.0F, 5.0F, 6.0F, 0.5F, false);

		ironGolemRightArm = new ModelRenderer(this);
		ironGolemRightArm.setPos(0.0F, 9.0F, 0.0F);
		setRotationAngle(ironGolemRightArm, -1.5708F, 0.0F, 0.0F);
		ironGolemRightArm.texOffs(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F, 0.0F, false);

		ironGolemLeftArm = new ModelRenderer(this);
		ironGolemLeftArm.setPos(0.0F, 9.0F, 0.0F);
		setRotationAngle(ironGolemLeftArm, -1.5708F, 0.0F, 0.0F);
		ironGolemLeftArm.texOffs(60, 58).addBox(9.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(EarthGolemEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// previously the render function, render code was moved to a method below
	}

	@Override
	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.ironGolemHead, this.ironGolemBody, this.ironGolemRightArm, this.ironGolemLeftArm);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}