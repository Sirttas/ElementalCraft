package sirttas.elementalcraft.block.pipe.upgrade.valve;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.block.pipe.section.ElementPipeSectionRenderState;
import sirttas.elementalcraft.block.pipe.upgrade.render.PipeUpgradeRenderer;
import sirttas.elementalcraft.client.model.SimpleStandaloneModelSupplier;
import sirttas.elementalcraft.client.renderer.ECRendererHelper;

public class ElementValvePipeUpgradeRenderer implements PipeUpgradeRenderer<ElementValvePipeUpgrade, ElementValvePipeUpgradeRenderState> {

    public static final SimpleStandaloneModelSupplier OPEN = SimpleStandaloneModelSupplier.pipeUpgrade("element_valve_open");
    public static final SimpleStandaloneModelSupplier CLOSE = SimpleStandaloneModelSupplier.pipeUpgrade("element_valve_close");

    private final BlockStateModelPart openModel;
    private final BlockStateModelPart closeModel;

    public ElementValvePipeUpgradeRenderer() {
        openModel = OPEN.loadModel();
        closeModel = CLOSE.loadModel();
    }

    @Override
    public ElementValvePipeUpgradeRenderState createRenderState() {
        return new ElementValvePipeUpgradeRenderState();
    }

    @Override
    public void extractRenderState(ElementValvePipeUpgrade pipeUpgrade, ElementValvePipeUpgradeRenderState state, ElementPipeSectionRenderState sectionRenderState, float partialTicks, Vec3 cameraPosition) {
        PipeUpgradeRenderer.super.extractRenderState(pipeUpgrade, state, sectionRenderState, partialTicks, cameraPosition);
        state.open = pipeUpgrade.isOpen();
    }

    @Override
    public void submit(ElementValvePipeUpgradeRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.open) {
            ECRendererHelper.submitModel(openModel, poseStack, submitNodeCollector, state.lightCoords);
        } else {
            ECRendererHelper.submitModel(closeModel, poseStack, submitNodeCollector, state.lightCoords);
        }
    }
}
