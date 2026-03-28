package sirttas.elementalcraft.block.shrine.upgrade.acceleration.overclocked;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.AccelerationShrineUpgradeRenderState;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.AccelerationShrineUpgradeRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.directional.AbstractDirectionalShrineUpgradeBlock;
import sirttas.elementalcraft.client.renderer.ECRendererHelper;

@Deprecated
public class OverclockedAccelerationShrineUpgradeRenderer implements BlockEntityRenderer<OverclockedAccelerationShrineUpgradeBlockEntity, AccelerationShrineUpgradeRenderState> {

    private final BlockStateModel clockModel;

    public OverclockedAccelerationShrineUpgradeRenderer() {
        clockModel = AccelerationShrineUpgradeRenderer.CLOCK.loadModel();
    }

    @Override
    public AccelerationShrineUpgradeRenderState createRenderState() {
        return new AccelerationShrineUpgradeRenderState();
    }

    @Override
    public void extractRenderState(OverclockedAccelerationShrineUpgradeBlockEntity blockEntity, AccelerationShrineUpgradeRenderState state, float partialTicks, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.partialTicks = partialTicks;
        state.facing = blockEntity.getBlockState().getValue(AbstractDirectionalShrineUpgradeBlock.FACING);
    }

    @Override
    public void submit(AccelerationShrineUpgradeRenderState state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState camera) {
        poseStack.translate(0.5 + (state.facing.getStepX() * 2D / 16), 22D / 16, 0.5 + (state.facing.getStepZ() * 2D / 16));
        poseStack.mulPose(Axis.YN.rotation((float) Math.toRadians(ECRendererHelper.getClientTicks(state.partialTicks) * 2)));
        ECRendererHelper.submitModel(clockModel, poseStack, submitNodeCollector, state.lightCoords);
    }
}
