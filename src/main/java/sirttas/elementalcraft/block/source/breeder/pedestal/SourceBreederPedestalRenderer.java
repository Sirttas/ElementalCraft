package sirttas.elementalcraft.block.source.breeder.pedestal;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.source.SourceRendererHelper;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class SourceBreederPedestalRenderer implements BlockEntityRenderer<SourceBreederPedestalBlockEntity> {

    @Override
    public void render(@Nonnull SourceBreederPedestalBlockEntity pedestal, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        ECRendererHelper.renderRunes(poseStack, buffer, pedestal, partialTicks, light, overlay);

        var type = pedestal.getElementType();

        if (type == ElementType.NONE) {
            return;
        }
        poseStack.translate(0, 0.6, 0);
        SourceRendererHelper.renderSource(poseStack, buffer, partialTicks, light, overlay, type, 1);
    }
}
