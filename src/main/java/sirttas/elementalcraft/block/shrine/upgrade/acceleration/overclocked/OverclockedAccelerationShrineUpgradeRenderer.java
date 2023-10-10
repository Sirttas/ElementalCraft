package sirttas.elementalcraft.block.shrine.upgrade.acceleration.overclocked;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.AccelerationShrineUpgradeRenderer;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class OverclockedAccelerationShrineUpgradeRenderer implements BlockEntityRenderer<OverclockedAccelerationShrineUpgradeBlockEntity> {

    private BakedModel clockModel;

    @Override
    public void render(@Nonnull OverclockedAccelerationShrineUpgradeBlockEntity te, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        var state = te.getBlockState();
        var facing = state.getValue(HorizontalDirectionalBlock.FACING);

        if (clockModel == null) {
            clockModel = Minecraft.getInstance().getModelManager().getModel(AccelerationShrineUpgradeRenderer.CLOCK_LOCATION);
        }
        poseStack.translate(0.5 + (facing.getStepX() * 2D / 16), 22D / 16, 0.5 + (facing.getStepZ() * 2D / 16));
        poseStack.mulPose(Axis.YN.rotation((float) Math.toRadians(ECRendererHelper.getClientTicks(partialTicks) * 2)));
        ECRendererHelper.renderModel(clockModel, poseStack, buffer, te, light, overlay);
    }
}
