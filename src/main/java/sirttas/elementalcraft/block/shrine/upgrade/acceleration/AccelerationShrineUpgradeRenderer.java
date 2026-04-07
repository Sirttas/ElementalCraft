package sirttas.elementalcraft.block.shrine.upgrade.acceleration;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.block.shrine.upgrade.directional.DirectionalShrineUpgradeBlock;
import sirttas.elementalcraft.client.model.SimpleStandaloneModelSupplier;
import sirttas.elementalcraft.client.renderer.ECRendererHelper;

@Deprecated
public class AccelerationShrineUpgradeRenderer implements BlockEntityRenderer<@NotNull AccelerationShrineUpgradeBlockEntity, @NotNull AccelerationShrineUpgradeRenderState> {

	private static final Vector3f POSITION = new Vector3f(0, 2F / 16, 0);

    public static final SimpleStandaloneModelSupplier CLOCK = new SimpleStandaloneModelSupplier("shrine_upgrade_acceleration_clock");

	private final BlockStateModelPart clockModel;

    public AccelerationShrineUpgradeRenderer() {
        clockModel = CLOCK.loadModel();
    }

    @Override
    public AccelerationShrineUpgradeRenderState createRenderState() {
        return new AccelerationShrineUpgradeRenderState();
    }

    @Override
    public void extractRenderState(AccelerationShrineUpgradeBlockEntity blockEntity, AccelerationShrineUpgradeRenderState state, float partialTicks, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.partialTicks = partialTicks;
        state.facing = blockEntity.getBlockState().getValue(DirectionalShrineUpgradeBlock.FACING);
    }

    @Override
    public void submit(AccelerationShrineUpgradeRenderState state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState camera) {
        var rotation = state.facing.getRotation();
        var newPos = new Vector3f(POSITION);

        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(Axis.of(state.facing.step()).rotation((float) Math.toRadians(ECRendererHelper.getClientTicks(state.partialTicks))));
        rotation.transform(newPos);
        poseStack.translate(newPos.x(), newPos.y(), newPos.z());
        poseStack.mulPose(rotation);
        ECRendererHelper.submitModel(clockModel, poseStack, submitNodeCollector, state.lightCoords);
    }
}
