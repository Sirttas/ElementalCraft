package sirttas.elementalcraft.block.synthesizer.mill;

import com.mojang.blaze3d.vertex.PoseStack;
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
import sirttas.elementalcraft.rune.RuneModelResolver;

public class AirMillSynthesizerRenderer implements BlockEntityRenderer<@NotNull AirMillSynthesizerBlockEntity, @NotNull AirMillSynthesizerRenderState> {

    public static final SimpleStandaloneModelSupplier SHAFT = new SimpleStandaloneModelSupplier("air_mill_synthesizer_shaft");

    private final BlockStateModelPart shaft;
    private final RuneModelResolver runeModelResolver;

    public AirMillSynthesizerRenderer() {
        shaft = SHAFT.loadModel();
        this.runeModelResolver = ECModelResolver.get(RuneModelResolver.IDENTIFIER);
    }

    @Override
    public AirMillSynthesizerRenderState createRenderState() {
        return new AirMillSynthesizerRenderState();
    }

    @Override
    public void extractRenderState(@NotNull AirMillSynthesizerBlockEntity blockEntity, @NotNull AirMillSynthesizerRenderState state, float partialTicks, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.shaft.update(this.shaft, blockEntity.isBroken(), blockEntity.isWorking(), partialTicks);
        state.runes.update(blockEntity, runeModelResolver, partialTicks);
    }

    @Override
    public void submit(AirMillSynthesizerRenderState state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState camera) {
        state.runes.submit(state, poseStack, submitNodeCollector);
        state.shaft.submit(state, poseStack, submitNodeCollector);
	}
}
