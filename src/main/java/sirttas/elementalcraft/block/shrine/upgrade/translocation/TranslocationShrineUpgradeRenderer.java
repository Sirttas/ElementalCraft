package sirttas.elementalcraft.block.shrine.upgrade.translocation;

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
import sirttas.elementalcraft.block.shrine.upgrade.directional.DirectionalShrineUpgradeBlock;
import sirttas.elementalcraft.client.model.SimpleStandaloneModelSupplier;
import sirttas.elementalcraft.client.renderer.ECRendererHelper;

@Deprecated
public class TranslocationShrineUpgradeRenderer implements BlockEntityRenderer<@NotNull TranslocationShrineUpgradeBlockEntity, @NotNull TranslocationShrineUpgradeRenderState> {

    public static final SimpleStandaloneModelSupplier RING = new SimpleStandaloneModelSupplier("shrine_upgrade_translocation_ring");

    private final BlockStateModelPart ringModel;


    public TranslocationShrineUpgradeRenderer() {
        ringModel = RING.loadModel();
    }

    @Override
    public TranslocationShrineUpgradeRenderState createRenderState() {
        return new TranslocationShrineUpgradeRenderState();
    }

    @Override
    public void extractRenderState(TranslocationShrineUpgradeBlockEntity blockEntity, TranslocationShrineUpgradeRenderState state, float partialTicks, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.partialTicks = partialTicks;
        state.facing = blockEntity.getBlockState().getValue(DirectionalShrineUpgradeBlock.FACING);
    }

    @Override
    public void submit(TranslocationShrineUpgradeRenderState state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState camera) {
        var rotation = state.facing.getRotation();

        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(rotation);
        poseStack.mulPose(Axis.YP.rotationDegrees(ECRendererHelper.getClientTicks(state.partialTicks) * 2));
        poseStack.translate(-0.5, -0.5, -0.5);
        ECRendererHelper.submitModel(ringModel, poseStack, submitNodeCollector, state.lightCoords);
    }
}
