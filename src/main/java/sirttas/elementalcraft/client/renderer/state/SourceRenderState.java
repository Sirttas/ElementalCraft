package sirttas.elementalcraft.client.renderer.state;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.sprite.Material;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.client.renderer.ECRenderTypes;
import sirttas.elementalcraft.client.renderer.ECRendererHelper;

public class SourceRenderState {

    public static final Material OUTER = ECRendererHelper.getBlockMaterial("effect/source_outer");
    public static final Material MIDDLE = ECRendererHelper.getBlockMaterial("effect/source_middle");

    private float animationTime;
    private float remainingRatio;
    private float red;
    private float green;
    private float blue;
    private boolean isNone;

    public void update(float remainingRatio, ElementType elementType, float partialTicks) {
        this.isNone = elementType == ElementType.NONE;
        if (this.isNone) {
            return;
        }

        this.animationTime = -(ECRendererHelper.getClientTicks(partialTicks) % 360);
        this.remainingRatio = remainingRatio;
        this.red = elementType.getRed();
        this.green = elementType.getGreen();
        this.blue = elementType.getBlue();
    }

    public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState camera, int light) {
        if (this.isNone) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 0.3, 0.5);
        poseStack.mulPose(camera.orientation);
        poseStack.scale(0.016F, 0.016F, 0.016F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(animationTime));
        poseStack.translate(-16, -16, 0);
        ECRendererHelper.submitIcon(poseStack, submitNodeCollector, 0, 0, OUTER, 32, 32, red, green, blue, light);
        poseStack.translate(16, 16, 0);
        poseStack.mulPose(Axis.ZP.rotationDegrees(animationTime * 5f * remainingRatio));
        poseStack.translate(-16, -16, -0.01);
        ECRendererHelper.submitIcon(poseStack, submitNodeCollector, ECRenderTypes.source(MIDDLE.sprite()), 0, 0, 32, 32, red, green, blue, light);
        poseStack.popPose();
    }
}
