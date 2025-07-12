package sirttas.elementalcraft.block.shrine.upgrade.fortune.greater;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class GreaterFortuneShrineUpgradeRenderer implements BlockEntityRenderer<GreaterFortuneShrineUpgradeBlockEntity> {

    @Override
    public void render(@Nonnull GreaterFortuneShrineUpgradeBlockEntity be, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        ECRendererHelper.renderRunes(poseStack, buffer, be.getRuneHandler(), ECRendererHelper.getClientTicks(partialTicks), light, overlay);
    }
}
