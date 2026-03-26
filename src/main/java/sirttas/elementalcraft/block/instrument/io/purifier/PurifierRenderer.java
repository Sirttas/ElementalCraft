package sirttas.elementalcraft.block.instrument.io.purifier;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.instrument.io.IOInstrumentRenderState;
import sirttas.elementalcraft.block.instrument.io.IOInstrumentRenderer;
import sirttas.elementalcraft.renderer.ECRendererHelper;

@OnlyIn(Dist.CLIENT)
public class PurifierRenderer extends IOInstrumentRenderer<PurifierBlockEntity, IOInstrumentRenderState> {

    private static final AABB INPUT_SLOT = new AABB(0, 0, 1, 1, 1, 1);

    protected PurifierRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull IOInstrumentRenderState createRenderState() {
        return new IOInstrumentRenderState();
    }

    @Override
    public void submit(@NotNull IOInstrumentRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState cameraRenderState) {
        float tick = ECRendererHelper.getClientTicks(renderState.partialTick);

        renderState.runes.submit(renderState, poseStack, submitNodeCollector);
        if (!renderState.material.isEmpty() || !renderState.material.isEmpty()) {
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.mulPose(ECRendererHelper.getRotation(renderState.facing));
            if (!renderState.material.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0, -5D / 16, -6D / 16);
                if (renderState.material.getModelBoundingBox().equals(INPUT_SLOT)) { // TODO check if this is working
                    poseStack.translate(0, 2D / 16, 0);
                    poseStack.scale(0.5F, 0.5F, 0.5F);
                    poseStack.mulPose(Axis.YP.rotationDegrees(tick));
                }
                renderState.material.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                poseStack.popPose();
            }
            if (!renderState.result.isEmpty()) {
                poseStack.translate(0, 4.6 / 16, 0);
                poseStack.mulPose(Axis.YP.rotationDegrees(tick));
                renderState.result.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            }
        }
    }
}
