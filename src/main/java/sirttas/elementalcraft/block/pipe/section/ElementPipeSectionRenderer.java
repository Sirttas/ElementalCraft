package sirttas.elementalcraft.block.pipe.section;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.block.pipe.ConnectionType;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeRenderState;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.render.PipeUpgradeRenderState;
import sirttas.elementalcraft.block.pipe.upgrade.render.PipeUpgradeRenderer;
import sirttas.elementalcraft.block.pipe.upgrade.render.PipeUpgradeRendererProvider;
import sirttas.elementalcraft.block.pipe.upgrade.render.PipeUpgradeRenderers;
import sirttas.elementalcraft.block.pipe.upgrade.render.model.PipeUpgradeModel;
import sirttas.elementalcraft.block.pipe.upgrade.render.model.PipeUpgradeModelResolver;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;
import sirttas.elementalcraft.client.model.ECModelResolver;
import sirttas.elementalcraft.client.model.SimpleStandaloneModelSupplier;
import sirttas.elementalcraft.client.renderer.ECRendererHelper;

import java.util.Map;

public class ElementPipeSectionRenderer {

    public static final SimpleStandaloneModelSupplier SIDE = SimpleStandaloneModelSupplier.block("elementpipe_side");
    public static final SimpleStandaloneModelSupplier EXTRACT = SimpleStandaloneModelSupplier.block("elementpipe_extract");

    private final BlockStateModelPart sideModel;
    private final BlockStateModelPart extractModel;
    private final PipeUpgradeModelResolver pipeUpgradeModelResolver;
    private final Map<PipeUpgradeType<?>, PipeUpgradeRenderer<?, ?>> pipeUpgradeRenderers;

    public ElementPipeSectionRenderer(BlockEntityRendererProvider.Context context) {
        sideModel = SIDE.loadModel();
        extractModel = EXTRACT.loadModel();
        pipeUpgradeModelResolver = ECModelResolver.get(PipeUpgradeModelResolver.IDENTIFIER);
        pipeUpgradeRenderers = PipeUpgradeRenderers.createRenderers(new PipeUpgradeRendererProvider.Context(context));
    }

    @SuppressWarnings("unchecked")
    public <T extends PipeUpgrade, S extends PipeUpgradeRenderState> @Nullable PipeUpgradeRenderer<T, S> getRenderer(T pipeUpgrade) {
        return (PipeUpgradeRenderer<T, S>) this.pipeUpgradeRenderers.get(pipeUpgrade.getType());
    }

    @SuppressWarnings("unchecked")
    public <T extends PipeUpgrade, S extends PipeUpgradeRenderState> @Nullable PipeUpgradeRenderer<T, S> getRenderer(S state) {
        return (PipeUpgradeRenderer<T, S>) this.pipeUpgradeRenderers.get(state.type);
    }

    public ElementPipeSectionRenderState createSectionState(ElementPipeBlockEntity pipe, Direction side, float partialTicks, Vec3 cameraPosition, ElementPipeRenderState pipeRenderState) {
        var renderState = new ElementPipeSectionRenderState();

        renderState.side = side;
        renderState.connectionType = pipe.getConnection(side);
        renderState.lightCoords = pipeRenderState.lightCoords;

        var upgrade = pipe.getUpgrade(side);
        var upgradeModel = pipeUpgradeModelResolver.getModel(upgrade);

        if (upgradeModel != null) {
            extractUpgradeState(renderState, upgrade, upgradeModel, partialTicks, cameraPosition);
        }

        if (renderState.connectionType.isConnected()) {
            if (upgrade == null || !upgrade.replaceSection()) {
                renderState.parts.add(this.sideModel);
            }
            if (renderState.connectionType == ConnectionType.EXTRACT && (upgrade == null || !upgrade.replaceExtraction())) {
                renderState.parts.add(this.extractModel);
            }
        }
        return renderState;
    }

    private void extractUpgradeState(ElementPipeSectionRenderState renderState, PipeUpgrade upgrade, PipeUpgradeModel upgradeModel, float partialTicks, Vec3 cameraPosition) {
        renderState.parts.add(upgradeModel.getModel());

        var upgradeRenderer = getRenderer(upgrade);

        if (upgradeRenderer != null) {
            var upgradeState = upgradeRenderer.createRenderState();

            if (upgradeState != null) {
                upgradeRenderer.extractRenderState(upgrade, upgradeState, renderState, partialTicks, cameraPosition);
                renderState.upgradeState = upgradeState;
            }
        }
    }

    public void submit(ElementPipeSectionRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, @NotNull CameraRenderState cameraRenderState) {
        if (renderState.isEmpty()) {
            return;
        }
        poseStack.pushPose();
        poseStack.mulPose(renderState.side.getRotation());
        poseStack.translate(-0.5, -0.5, -0.5);
        if (!renderState.parts.isEmpty()) {
            ECRendererHelper.submitModel(renderState.parts, poseStack, nodeCollector, renderState.lightCoords);
        }

        if (renderState.upgradeState != null) {
            var upgradeRenderer = getRenderer(renderState.upgradeState);

            if (upgradeRenderer != null) {
                upgradeRenderer.submit(renderState.upgradeState, poseStack, nodeCollector, cameraRenderState);
            }
        }
        poseStack.popPose();
    }
}
