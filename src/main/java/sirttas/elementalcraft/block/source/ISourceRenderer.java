package sirttas.elementalcraft.block.source;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.entity.BlockEntity;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.renderer.ECRenderTypes;
import sirttas.elementalcraft.block.entity.renderer.IECRenderer;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public interface ISourceRenderer<T extends BlockEntity> extends IECRenderer<T> {
    Material OUTER = ECRendererHelper.getBlockMaterial(ElementalCraft.createRL("effect/source_outer"));
    Material MIDDLE = ECRendererHelper.getBlockMaterial(ElementalCraft.createRL("effect/source_middle"));

    default void renderSource(@Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, float partialTicks, int light, int overlay, ElementType elementType, boolean exhausted, float ratio) {
        float r = elementType.getRed();
        float g = elementType.getGreen();
        float b = elementType.getBlue();
        var angle = -(getClientTicks(partialTicks) % 360);

        poseStack.pushPose();
        poseStack.translate(0.5, 0.3, 0.5);
        poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().camera.rotation());
        poseStack.scale(0.016F, 0.016F, 0.016F);
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(angle));
        poseStack.translate(-16, -16, 0);
        if (exhausted) {
            this.renderIcon(poseStack, buffer, 0, 0, OUTER, 32, 32, r * 0.5f, g * 0.5f, b * 0.5f, light, overlay);
        } else {
            this.renderIcon(poseStack, buffer, 0, 0, OUTER, 32, 32, r, g, b, light, overlay);
            poseStack.translate(16, 16, 0);
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(angle * 5f * ratio));
            poseStack.translate(-16, -16, -0.01);
            this.renderIcon(poseStack, MIDDLE.buffer(buffer, ECRenderTypes::source), 0, 0, MIDDLE, 32, 32, r, g, b, light, overlay);
        }
        poseStack.popPose();
    }
}
