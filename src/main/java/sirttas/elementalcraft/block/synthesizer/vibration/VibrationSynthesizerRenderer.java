package sirttas.elementalcraft.block.synthesizer.vibration;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.client.model.ECModelResolver;
import sirttas.elementalcraft.rune.RuneModelResolver;

public class VibrationSynthesizerRenderer implements BlockEntityRenderer<@NotNull VibrationSynthesizerBlockEntity, @NotNull VibrationSynthesizerRenderState> {

    private final RuneModelResolver runeModelResolver;

    public VibrationSynthesizerRenderer() {
        this.runeModelResolver = ECModelResolver.get(RuneModelResolver.IDENTIFIER);
    }

    @Override
    public @NotNull VibrationSynthesizerRenderState createRenderState() {
        return new VibrationSynthesizerRenderState();
    }

    @Override
    public void extractRenderState(@NotNull VibrationSynthesizerBlockEntity blockEntity, @NotNull VibrationSynthesizerRenderState state, float partialTicks, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.range.update(blockEntity, blockEntity.getRange());
        state.runes.update(blockEntity.getRuneHandler(), runeModelResolver, partialTicks);
    }

    @Override
    public void submit(@NotNull VibrationSynthesizerRenderState state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState camera) {
        state.range.submit();
        state.runes.submit(poseStack, submitNodeCollector, state.lightCoords);
    }
}
