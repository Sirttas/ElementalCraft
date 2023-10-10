package sirttas.elementalcraft.block.source;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.Material;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.renderer.ECRenderTypes;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class SourceRendererHelper {

    public static final Material OUTER = ECRendererHelper.getBlockMaterial("effect/source_outer");
    public static final Material MIDDLE = ECRendererHelper.getBlockMaterial("effect/source_middle");

    private SourceRendererHelper() {}

    public static void renderSource(@Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, float partialTicks, int light, int overlay, ElementType elementType, boolean exhausted, float ratio) {
        float r = elementType.getRed();
        float g = elementType.getGreen();
        float b = elementType.getBlue();
        var angle = -(ECRendererHelper.getClientTicks(partialTicks) % 360);

        poseStack.pushPose();
        poseStack.translate(0.5, 0.3, 0.5);
        poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().camera.rotation());
        poseStack.scale(0.016F, 0.016F, 0.016F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(angle));
        poseStack.translate(-16, -16, 0);
        if (exhausted) {
            ECRendererHelper.renderIcon(poseStack, buffer, 0, 0, OUTER, 32, 32, r * 0.5f, g * 0.5f, b * 0.5f, light, overlay);
        } else {
            ECRendererHelper.renderIcon(poseStack, buffer, 0, 0, OUTER, 32, 32, r, g, b, light, overlay);
            poseStack.translate(16, 16, 0);
            poseStack.mulPose(Axis.ZP.rotationDegrees(angle * 5f * ratio));
            poseStack.translate(-16, -16, -0.01);
            ECRendererHelper.renderIcon(poseStack, MIDDLE.buffer(buffer, ECRenderTypes::source), 0, 0, 32, 32, r, g, b, light, overlay);
        }
        poseStack.popPose();
    }

}
