package sirttas.elementalcraft.block.shrine.upgrade.vortex;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.block.entity.renderer.ECBlockEntityRenderState;
import sirttas.elementalcraft.client.model.SimpleStandaloneModelSupplier;
import sirttas.elementalcraft.client.renderer.ECRendererHelper;

@Deprecated
public class VortexShrineUpgradeRenderer implements BlockEntityRenderer<@NotNull VortexShrineUpgradeBlockEntity, @NotNull ECBlockEntityRenderState> {

	public static final SimpleStandaloneModelSupplier RING = new SimpleStandaloneModelSupplier("shrine_upgrade_vortex_ring");

    private final BlockStateModelPart ringModel;

    public VortexShrineUpgradeRenderer() {
        ringModel = RING.loadModel();
    }

    @Override
    public ECBlockEntityRenderState createRenderState() {
        return new ECBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(VortexShrineUpgradeBlockEntity blockEntity, ECBlockEntityRenderState state, float partialTicks, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.partialTicks = partialTicks;
    }

    @Override
    public void submit(ECBlockEntityRenderState state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState camera) {
		var angle = ECRendererHelper.getClientTicks(state.partialTicks);


        poseStack.translate(0.5, 11D / 16, 0.5);
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));
        poseStack.translate(-4D / 16, -3D / 16, -4D / 16);
        ECRendererHelper.submitModel(ringModel, poseStack, submitNodeCollector, state.lightCoords);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0, 2D / 16, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(-angle));
        poseStack.translate(-4D / 16, -3D / 16, -4D / 16);
        ECRendererHelper.submitModel(ringModel, poseStack, submitNodeCollector, state.lightCoords);
        poseStack.popPose();
	}
	
}
