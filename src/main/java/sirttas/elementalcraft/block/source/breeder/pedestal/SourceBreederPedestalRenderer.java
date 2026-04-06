package sirttas.elementalcraft.block.source.breeder.pedestal;

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

public class SourceBreederPedestalRenderer implements BlockEntityRenderer<@NotNull SourceBreederPedestalBlockEntity, @NotNull SourceBreederPedestalRenderState> {

    private final RuneModelResolver runeModelResolver;

    public SourceBreederPedestalRenderer() {
        this.runeModelResolver = ECModelResolver.get(RuneModelResolver.IDENTIFIER);
    }

    @Override
    public SourceBreederPedestalRenderState createRenderState() {
        return new SourceBreederPedestalRenderState();
    }

    @Override
    public void extractRenderState(SourceBreederPedestalBlockEntity blockEntity, SourceBreederPedestalRenderState state, float partialTicks, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.runes.update(blockEntity, runeModelResolver, partialTicks);
        state.source.update(1, blockEntity.getElementType(), partialTicks);
    }

    @Override
    public void submit(SourceBreederPedestalRenderState state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState camera) {
        state.runes.submit(state, poseStack, submitNodeCollector);
        state.source.submit(poseStack, submitNodeCollector, camera, state.lightCoords);
    }
}
