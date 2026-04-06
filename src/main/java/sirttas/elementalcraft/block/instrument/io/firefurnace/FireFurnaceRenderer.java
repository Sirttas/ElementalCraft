package sirttas.elementalcraft.block.instrument.io.firefurnace;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.instrument.io.IOInstrumentRenderState;
import sirttas.elementalcraft.block.instrument.io.IOInstrumentRenderer;
import sirttas.elementalcraft.client.renderer.ECRendererHelper;

@OnlyIn(Dist.CLIENT)
public class FireFurnaceRenderer<T extends AbstractFireFurnaceBlockEntity<?>> extends IOInstrumentRenderer<@NotNull T, @NotNull IOInstrumentRenderState> {

    public FireFurnaceRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull IOInstrumentRenderState createRenderState() {
        return new IOInstrumentRenderState();
    }

    @Override
    public void submit(IOInstrumentRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, @NotNull CameraRenderState cameraRenderState) {
        float tick = ECRendererHelper.getClientTicks(renderState.partialTicks);

        renderState.runes.submit(renderState, poseStack, nodeCollector);
        if (!renderState.material.isEmpty() || !renderState.result.isEmpty()) {
            poseStack.translate(0.5F, 0.3F, 0.5F);
            poseStack.mulPose(Axis.YP.rotationDegrees(tick));
            if (!renderState.material.isEmpty()) {
                renderState.material.submit(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            }
            if (!renderState.result.isEmpty()) {
                poseStack.translate(0, 0.5F, 0);
                renderState.result.submit(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            }
        }
    }
}
