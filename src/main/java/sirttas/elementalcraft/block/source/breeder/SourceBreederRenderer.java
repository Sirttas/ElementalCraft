package sirttas.elementalcraft.block.source.breeder;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import sirttas.elementalcraft.block.entity.renderer.IRuneRenderer;

import javax.annotation.Nonnull;

public class SourceBreederRenderer implements IRuneRenderer<SourceBreederBlockEntity> {

    @Override
    public void render(@Nonnull SourceBreederBlockEntity breeder, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        poseStack.translate(0, 1, 0);
        IRuneRenderer.super.render(breeder, partialTicks, poseStack, buffer, light, overlay);
    }
}
