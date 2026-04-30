package sirttas.elementalcraft.block.source;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.client.model.SimpleStandaloneModelSupplier;
import sirttas.elementalcraft.client.renderer.ECRendererHelper;

import java.util.List;

public class SourceRenderer implements BlockEntityRenderer<@NotNull SourceBlockEntity, @NotNull SourceBlockEntityRenderState> {

	public static final SimpleStandaloneModelSupplier STABILIZER = SimpleStandaloneModelSupplier.block("source_stabilizer");
	
	private final BlockStateModelPart stabilizerModel;

    public SourceRenderer() {
        stabilizerModel = STABILIZER.loadModel();
    }

    @Override
    public SourceBlockEntityRenderState createRenderState() {
        return new SourceBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(@NotNull SourceBlockEntity blockEntity, @NotNull SourceBlockEntityRenderState state, float partialTicks, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.source.update(blockEntity.getRemainingRatio(), blockEntity.getElementType(), partialTicks);
        state.stabilized = blockEntity.isStabilized();
        state.animationTime = ECRendererHelper.getClientTicks(partialTicks) / 20F;
        state.color = blockEntity.getElementType().getColor();
    }

    @Override
    public void submit(SourceBlockEntityRenderState state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState camera) {
        state.source.submit(poseStack, submitNodeCollector, camera, state.lightCoords);
		if (state.stabilized) {
			poseStack.translate(0.5, 0, 0.5);
			poseStack.mulPose(Axis.YP.rotation(ECRendererHelper.getClientTicks(state.animationTime) / 20F));
			poseStack.translate(-0.5, 0, -0.5);
			poseStack.translate(0, -1 / 4D, 0);
            ECRendererHelper.submitModel(stabilizerModel, poseStack, submitNodeCollector, state.lightCoords);
            submitNodeCollector.submitBlockModel(poseStack, RenderTypes.translucentMovingBlock(), List.of(stabilizerModel), new int[]{state.color}, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        }
	}
}
