package sirttas.elementalcraft.block.shrine.upgrade.horizontal.fortune.greater;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import sirttas.elementalcraft.block.entity.renderer.IRuneRenderer;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class GreaterFortuneShrineUpgradeRenderer implements IRuneRenderer<GreaterFortuneShrineUpgradeBlockEntity> {

    @Override
    public void render(@Nonnull GreaterFortuneShrineUpgradeBlockEntity be, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        ECRendererHelper.renderRunes(poseStack, buffer, be.getRuneHandler(), ECRendererHelper.getClientTicks(partialTicks), light, overlay);
    }
}
