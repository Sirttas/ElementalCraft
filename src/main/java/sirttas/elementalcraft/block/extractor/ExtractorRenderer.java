package sirttas.elementalcraft.block.extractor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class ExtractorRenderer implements BlockEntityRenderer<ElementExtractorBlockEntity> {

    @Override
    public void render(@Nonnull ElementExtractorBlockEntity extractor, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay){
        ECRendererHelper.renderRunes(poseStack, buffer, extractor, partialTicks, light, overlay);
    }
}
