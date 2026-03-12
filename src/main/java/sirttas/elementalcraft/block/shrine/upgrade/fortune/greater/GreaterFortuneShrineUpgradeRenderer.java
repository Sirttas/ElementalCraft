package sirttas.elementalcraft.block.shrine.upgrade.fortune.greater;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class GreaterFortuneShrineUpgradeRenderer implements BlockEntityRenderer<@NotNull GreaterFortuneShrineUpgradeBlockEntity, @NotNull GreaterFortuneShrineUpgradeRenderState> {

    @Override
    public GreaterFortuneShrineUpgradeRenderState createRenderState() {
        return new GreaterFortuneShrineUpgradeRenderState();
    }

    @Override
    public void extractRenderState(GreaterFortuneShrineUpgradeBlockEntity blockEntity, GreaterFortuneShrineUpgradeRenderState renderState, float partialTick, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.runes.update(blockEntity.getRuneHandler(), partialTick);
    }

    @Override
    public void submit(GreaterFortuneShrineUpgradeRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, @NotNull CameraRenderState cameraRenderState) {
        renderState.runes.submit(renderState, poseStack, nodeCollector);
    }
}
