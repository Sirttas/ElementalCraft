package sirttas.elementalcraft.block.pipe.upgrade.beam;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.block.pipe.section.ElementPipeSectionRenderState;
import sirttas.elementalcraft.block.pipe.upgrade.render.PipeUpgradeRenderer;
import sirttas.elementalcraft.block.pipe.upgrade.render.PipeUpgradeRendererProvider;
import sirttas.elementalcraft.client.model.SimpleStandaloneModelSupplier;
import sirttas.elementalcraft.client.renderer.ECRendererHelper;
import sirttas.elementalcraft.rune.RuneModelResolver;

import javax.annotation.Nonnull;
import java.util.List;

public class ElementBeamPipeUpgradeRenderer implements PipeUpgradeRenderer<ElementBeamPipeUpgrade, ElementBeamPipeUpgradeRenderState> {

    public static final SimpleStandaloneModelSupplier RING_1 = new SimpleStandaloneModelSupplier("element_beam_ring_1");
    public static final SimpleStandaloneModelSupplier RING_2 = new SimpleStandaloneModelSupplier("element_beam_ring_2");
    public static final SimpleStandaloneModelSupplier RING_3 = new SimpleStandaloneModelSupplier("element_beam_ring_3");

    private final BlockStateModelPart ring1Model;
    private final BlockStateModelPart ring2Model;
    private final BlockStateModelPart ring3Model;
    private final RuneModelResolver runeModelResolver;

    public ElementBeamPipeUpgradeRenderer(PipeUpgradeRendererProvider.Context context) {
        ring1Model = RING_1.loadModel();
        ring2Model = RING_2.loadModel();
        ring3Model = RING_3.loadModel();
        runeModelResolver = context.runeModelResolver();
    }

    @Override
    public ElementBeamPipeUpgradeRenderState createRenderState() {
        return new ElementBeamPipeUpgradeRenderState();
    }

    @Override
    public void extractRenderState(ElementBeamPipeUpgrade pipeUpgrade, ElementBeamPipeUpgradeRenderState state, float partialTicks, Vec3 cameraPosition, ElementPipeSectionRenderState sectionRenderState) {
        PipeUpgradeRenderer.super.extractRenderState(pipeUpgrade, state, partialTicks, cameraPosition, sectionRenderState);
        state.animationTime = ECRendererHelper.getClientTicks(partialTicks) % 50;
        state.linked = pipeUpgrade.isLinked();
        state.runes.update(pipeUpgrade.getRuneHandler(), runeModelResolver, partialTicks);
    }

    @Override
    public void submit(ElementBeamPipeUpgradeRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!state.linked) {
            ECRendererHelper.submitModel(List.of(ring1Model, ring2Model, ring3Model), poseStack, submitNodeCollector, state.lightCoords);
        } else {
            poseStack.pushPose();
            translateRing(state.animationTime, 0, 5, 20, 30, poseStack);
            ECRendererHelper.submitModel(ring1Model, poseStack, submitNodeCollector, state.lightCoords);
            poseStack.popPose();

            poseStack.pushPose();
            translateRing(state.animationTime, 5, 10, 25, 35, poseStack);
            ECRendererHelper.submitModel(ring2Model, poseStack, submitNodeCollector, state.lightCoords);
            poseStack.popPose();

            poseStack.pushPose();
            translateRing(state.animationTime, 10, 15, 30, 40, poseStack);
            ECRendererHelper.submitModel(ring3Model, poseStack, submitNodeCollector, state.lightCoords);
            poseStack.popPose();
        }
        poseStack.translate(0.25, 0.5, 0.25);
        poseStack.scale(0.5F, 0.5F, 0.5F);
        state.runes.submit(poseStack, submitNodeCollector, state.lightCoords);
    }

    private static void translateRing(float tick, int from, int to, int from2, int to2, @Nonnull PoseStack poseStack) {
        if (tick < to && tick >= from) {
            poseStack.translate(0, (from - tick) / 100F, 0);
        } else if (tick >= to && tick < from2) {
            poseStack.translate(0, -5 / 100F, 0);
        } else if (tick >= from2 && tick < to2) {
            poseStack.translate(0, -5 / 100F + (tick - from2) / 200F, 0);
        }
    }
}
