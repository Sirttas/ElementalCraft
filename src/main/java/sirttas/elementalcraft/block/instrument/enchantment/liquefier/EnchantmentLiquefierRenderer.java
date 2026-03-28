package sirttas.elementalcraft.block.instrument.enchantment.liquefier;

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
public class EnchantmentLiquefierRenderer extends IOInstrumentRenderer<@NotNull EnchantmentLiquefierBlockEntity, @NotNull IOInstrumentRenderState> {

    public EnchantmentLiquefierRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public IOInstrumentRenderState createRenderState() {
        return new IOInstrumentRenderState();
    }

    @Override
    public void submit(IOInstrumentRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, @NotNull CameraRenderState cameraRenderState) {
        float tick = ECRendererHelper.getClientTicks(renderState.partialTick);

        poseStack.translate(0F, 0.25F, 0F);
        renderState.runes.submit(renderState, poseStack, nodeCollector);
        poseStack.translate(0.5F, 0.45F, 0.5F);
        poseStack.pushPose();
        poseStack.scale(0.75F, 0.75F, 0.75F);
        poseStack.mulPose(Axis.YP.rotationDegrees(tick));
        renderState.material.submit(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
        poseStack.translate(0F, 0.75F, 0F);
        poseStack.scale(0.75F, 0.75F, 0.75F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-tick));
        renderState.result.submit(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
    }
}
