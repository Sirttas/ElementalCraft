package sirttas.elementalcraft.block.pipe;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.block.pipe.section.ElementPipeSectionRenderState;
import sirttas.elementalcraft.block.pipe.section.ElementPipeSectionRenderer;

public class ElementPipeRenderer implements BlockEntityRenderer<@NotNull ElementPipeBlockEntity, @NotNull ElementPipeRenderState> {

    private final ElementPipeSectionRenderer sectionRenderer;
    private final BlockModelResolver blockModelResolver;

    public ElementPipeRenderer(BlockEntityRendererProvider.Context context) {
        this.sectionRenderer = new ElementPipeSectionRenderer(context);
        this.blockModelResolver = context.blockModelResolver();
    }

    @Override
    public ElementPipeRenderState createRenderState() {
        return new ElementPipeRenderState();
    }

    @Override
    public void extractRenderState(@NotNull ElementPipeBlockEntity blockEntity, @NotNull ElementPipeRenderState state, float partialTicks, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.sections.clear();
        for (Direction direction : Direction.values()) {
            state.sections.add(sectionRenderer.createSectionState(blockEntity, direction, partialTicks, cameraPosition, state));
        }
        state.cover.update(blockModelResolver, blockEntity);
    }

    @Override
    public void submit(ElementPipeRenderState state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState camera) {
        state.cover.submit(poseStack, submitNodeCollector, state.lightCoords);

        if (state.cover.showCover()) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        for (ElementPipeSectionRenderState section : state.sections) {
            sectionRenderer.submit(section, poseStack, submitNodeCollector, camera);
        }
        poseStack.popPose();
    }
}
