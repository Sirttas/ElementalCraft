package sirttas.elementalcraft.block.source.displacement.plate;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ForgeHooksClient;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.renderer.IECRenderer;

import javax.annotation.Nonnull;

public class SourceDisplacementPlateRenderer implements IECRenderer<SourceDisplacementPlateBlockEntity> {

    public static final Material SOURCE_DISPLACEMENT = ForgeHooksClient.getBlockMaterial(ElementalCraft.createRL("effect/source_displacement"));
    public static final Material CIRCLE = ForgeHooksClient.getBlockMaterial(ElementalCraft.createRL("effect/source_displacement_circle"));

    @Override
    public void render(@Nonnull SourceDisplacementPlateBlockEntity blockEntity, float partialTick, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (!blockEntity.isRunning()) {
            return;
        }

        var type = blockEntity.getElementType();
        var vector = Vec3.atCenterOf(blockEntity.getBlockPos()).subtract(Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition()).multiply(1, 0, 1).normalize();
        var progress = blockEntity.getRunningTick() / 300f;
        var progressScale = 0.5f + (0.5f * (1 - progress));
        var progressScaleTranslate = -0.5 + 0.5 * (1 - progressScale);

        poseStack.pushPose();
        poseStack.translate(0.5, 0.8, 0.5);
        poseStack.scale(1/64f, 1/64f, 1/64f);
        poseStack.mulPose(Vector3f.YP.rotation((float) Math.acos(vector.z * (vector.x > 0 ? 1 : -1))));
        poseStack.translate(-32, 0, vector.x > 0 ? -10 : 10);
        this.renderIcon(poseStack, bufferSource, 0, 0, SOURCE_DISPLACEMENT, 64, 64, type.getRed(), type.getGreen(), type.getBlue(), packedLight, packedOverlay);
        poseStack.popPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(getClientTicks(partialTick)));
        poseStack.translate(progressScaleTranslate, 0.5 + (0.3 * progress), progressScaleTranslate);
        poseStack.scale(progressScale, 0, progressScale);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(90));
        poseStack.scale(1/64f, 1/64f, 1/64f);
        this.renderIcon(poseStack, bufferSource, 0, 0, CIRCLE, 64, 64, type.getRed(), type.getGreen(), type.getBlue(), packedLight, packedOverlay);
    }
}
