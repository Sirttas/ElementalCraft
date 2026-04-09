package sirttas.elementalcraft.block.synthesizer.cracking;

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
import sirttas.elementalcraft.client.model.ECModelResolver;
import sirttas.elementalcraft.client.model.SimpleStandaloneModelSupplier;
import sirttas.elementalcraft.client.renderer.ECRendererHelper;
import sirttas.elementalcraft.rune.RuneModelResolver;

public class CrackingSynthesizerRenderer<T extends AbstractCrackingSynthesizerBlockEntity<?>> implements BlockEntityRenderer<@NotNull T, @NotNull CrackingSynthesizerRenderState> {

	public static final SimpleStandaloneModelSupplier HEAD = new SimpleStandaloneModelSupplier("cracking_earth_synthesizer_head");

	private final BlockStateModelPart headModel;
    private final RuneModelResolver runeModelResolver;

    public CrackingSynthesizerRenderer() {
        this.runeModelResolver = ECModelResolver.get(RuneModelResolver.IDENTIFIER);
        this.headModel = HEAD.loadModel();
    }

    @Override
    public CrackingSynthesizerRenderState createRenderState() {
        return new CrackingSynthesizerRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, CrackingSynthesizerRenderState state, float partialTicks, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.range.update(blockEntity, blockEntity.getRange());
        state.runes.update(blockEntity.getRuneHandler(), runeModelResolver, partialTicks);
        state.animationTime = blockEntity.isRunning() ? -5 * ECRendererHelper.getClientTicks(partialTicks) : 0;
    }

    @Override
    public void submit(CrackingSynthesizerRenderState state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState camera) {
        state.runes.submit(poseStack, submitNodeCollector, state.lightCoords);
        state.range.submit();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.animationTime));
        poseStack.translate(-0.5, 0, -0.5);
        ECRendererHelper.submitModel(headModel, poseStack, submitNodeCollector, state.lightCoords);
	}
}
