package sirttas.elementalcraft.block.pipe.upgrade.pump;

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

public class ElementPumpPipeUpgradeRenderer implements PipeUpgradeRenderer<ElementPumpPipeUpgrade, ElementPumpPipeUpgradeRenderState> {

    public static final SimpleStandaloneModelSupplier PUMP = new SimpleStandaloneModelSupplier("element_pump_pump");

    private final BlockStateModelPart pumpModel;
    private final RuneModelResolver runeModelResolver;

    public ElementPumpPipeUpgradeRenderer(PipeUpgradeRendererProvider.Context context)  {
        this.pumpModel = PUMP.loadModel();
        this.runeModelResolver = context.runeModelResolver();
    }


    @Override
    public ElementPumpPipeUpgradeRenderState createRenderState() {
        return new ElementPumpPipeUpgradeRenderState();
    }

    @Override
    public void extractRenderState(ElementPumpPipeUpgrade pipeUpgrade, ElementPumpPipeUpgradeRenderState state, float partialTicks, Vec3 cameraPosition, ElementPipeSectionRenderState sectionRenderState) {
        PipeUpgradeRenderer.super.extractRenderState(pipeUpgrade, state, partialTicks, cameraPosition, sectionRenderState);
        state.animationTime = ECRendererHelper.getClientTicks(partialTicks) % 30;
        state.runes.update(pipeUpgrade.getRuneHandler(), runeModelResolver, partialTicks);
    }

    @Override
    public void submit(ElementPumpPipeUpgradeRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        if (state.animationTime < 10 && state.animationTime >= 0) {
            poseStack.translate(0, (0 - state.animationTime) / 50F, 0);
        } else if (state.animationTime >= 10 && state.animationTime < 15) {
            poseStack.translate(0, -10 / 50F, 0);
        } else if (state.animationTime >= 15 && state.animationTime < 25) {
            poseStack.translate(0, -10 / 50F + (state.animationTime - 15) / 50F, 0);
        }

        ECRendererHelper.submitModel(pumpModel, poseStack, submitNodeCollector, state.lightCoords);
        poseStack.popPose();
        poseStack.translate(0.25, 0.5, 0.25);
        poseStack.scale(0.5F, 0.5F, 0.5F);
        state.runes.submit(poseStack, submitNodeCollector, state.lightCoords);
    }
}
