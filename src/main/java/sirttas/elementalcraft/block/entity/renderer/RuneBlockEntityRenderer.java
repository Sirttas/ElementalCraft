package sirttas.elementalcraft.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public abstract class RuneBlockEntityRenderer<T extends BlockEntity, S extends RuneBlockEntityRenderState> implements BlockEntityRenderer<@NotNull T, @NotNull S> {

    public static <T extends BlockEntity> RuneBlockEntityRenderer<T, RuneBlockEntityRenderState> create() {
        return new DefaultRuneBlockEntityRenderer<>();
    }

    @Override
    public void extractRenderState(T blockEntity, S renderState, float partialTick, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.runes.update(blockEntity, partialTick);
    }

    @Override
    public void submit(S renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, @NotNull CameraRenderState cameraRenderState) {
        renderState.runes.submit(renderState, poseStack, nodeCollector);
    }

    private static class DefaultRuneBlockEntityRenderer<T extends BlockEntity> extends RuneBlockEntityRenderer<T, RuneBlockEntityRenderState> {
        @Override
        public @NotNull RuneBlockEntityRenderState createRenderState() {
            return new RuneBlockEntityRenderState();
        }
    }
}
