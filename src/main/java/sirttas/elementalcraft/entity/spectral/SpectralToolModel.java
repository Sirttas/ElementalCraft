package sirttas.elementalcraft.entity.spectral;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

public class SpectralToolModel<T extends SpectralTool> extends EntityModel<T> implements ArmedModel {

    private static final String ARM = "arm";

    private final ModelPart arm;
    HumanoidModel.ArmPose armPose;

    public SpectralToolModel(ModelPart root) {
        this.arm = root.getChild(ARM);
        this.armPose = HumanoidModel.ArmPose.ITEM;
    }

    @Override
    public void translateToHand(@NotNull HumanoidArm side, @NotNull PoseStack poseStack) {
        this.arm.translateAndRotate(poseStack);
        poseStack.translate(1 / 16F, -9 / 16F, 1 / 16F);
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean falling = entity.getFallFlyingTicks() > 4;

        this.arm.z = 0.0F;
        this.arm.x = 0.0F;
        float ratio = 1.0F;
        if (falling) {
            ratio = (float)entity.getDeltaMovement().lengthSqr();
            ratio /= 0.2F;
            ratio *= ratio * ratio;
        }

        if (ratio < 1.0F) {
            ratio = 1.0F;
        }

        this.arm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount * 0.25F / ratio;
        this.arm.zRot = 0.0F;
        if (this.riding) {
            this.arm.xRot += (float) (-Math.PI / 5);
        }

        this.arm.yRot = 0.0F;

        this.poseArm(entity);
        this.setupAttackAnimation();

        if (this.armPose != HumanoidModel.ArmPose.SPYGLASS) {
            AnimationUtils.bobModelPart(this.arm, ageInTicks, 1.0F);
        }

    }

    private void poseArm(T livingEntity) {
        switch (this.armPose) {
            case EMPTY:
                this.arm.xRot = this.arm.xRot * 0.5F - (float) Math.PI / 1.3F;
                this.arm.yRot = 0.0F;
                break;
            case ITEM:
                this.arm.xRot = this.arm.xRot * 0.5F - (float) Math.PI / 10F;
                this.arm.yRot = 0.0F;
                break;
            case BLOCK:
                this.arm.xRot = arm.xRot * 0.5F - 0.9424779F + Mth.clamp(0, (float) (-Math.PI * 4.0 / 9.0), 0.43633232F);
                this.arm.yRot = -30.0F * (float) (Math.PI / 180.0) + Mth.clamp(0, (float) (-Math.PI / 6), (float) (Math.PI / 6));
                break;
            case BOW_AND_ARROW:
                this.arm.yRot = -0.1F + 0;
                this.arm.xRot = (float) (-Math.PI / 2) + 0;
                break;
            case THROW_SPEAR:
                this.arm.xRot = this.arm.xRot * 0.5F - (float) Math.PI;
                this.arm.yRot = 0.0F;
                break;
            /* case CROSSBOW_CHARGE:
                AnimationUtils.animateCrossbowCharge(this.arm, this.leftArm, livingEntity, true);
                break;
            case CROSSBOW_HOLD:
                AnimationUtils.animateCrossbowHold(this.arm, this.leftArm, this.head, true);
                break; */
            case SPYGLASS:
                this.arm.xRot = Mth.clamp(0 - 1.9198622F - (livingEntity.isCrouching() ? (float) (Math.PI / 12) : 0.0F), -2.4F, 3.3F);
                this.arm.yRot = 0 - (float) (Math.PI / 12);
                break;
            case TOOT_HORN:
                this.arm.xRot = Mth.clamp(0, -1.2F, 1.2F) - 1.4835298F;
                this.arm.yRot = 0 - (float) (Math.PI / 6);
                break;
            case BRUSH:
                this.arm.xRot = this.arm.xRot * 0.5F - (float) (Math.PI / 5);
                this.arm.yRot = 0.0F;
        }
    }

    protected void setupAttackAnimation() {
        if (!(this.attackTime <= 0.0F)) {
            float f = this.attackTime;
            float bodyRot = Mth.sin(Mth.sqrt(f) * (float) (Math.PI * 2)) * 0.2F;

            this.arm.z = Mth.sin(bodyRot) * 5.0F;
            this.arm.x = -Mth.cos(bodyRot) * 5.0F;
            this.arm.yRot = this.arm.yRot + bodyRot;
            f = 1.0F - this.attackTime;
            f *= f;
            f *= f;
            f = 1.0F - f;
            float f1 = Mth.sin(f * (float) Math.PI);
            float f2 = Mth.sin(this.attackTime * (float) Math.PI) * -0.75F;
            this.arm.xRot -= f1 * 1.2F + f2;
            this.arm.yRot = this.arm.yRot + bodyRot * 2.0F;
            this.arm.zRot = this.arm.zRot + Mth.sin(this.attackTime * (float) Math.PI) * -0.4F;
        }
    }


    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        arm.render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    public static MeshDefinition createMesh() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild(
                ARM,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 3.0F, 0.0F)
        );

        return meshdefinition;
    }
}
