package sirttas.elementalcraft.client.renderer.pip;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.LightCoordsUtil;
import org.jspecify.annotations.NonNull;

public class GuiBlockRenderer extends PictureInPictureRenderer<GuiBlockRenderState> {

    public GuiBlockRenderer(MultiBufferSource.BufferSource bufferSource) {
        super(bufferSource);
    }

    @Override
    public @NonNull Class<GuiBlockRenderState> getRenderStateClass() {
        return GuiBlockRenderState.class;
    }

    @Override
    protected void renderToTexture(@NonNull GuiBlockRenderState renderState, @NonNull PoseStack poseStack) {
        var minecraft = Minecraft.getInstance();
        var gameRenderer = minecraft.gameRenderer;
        var featureRenderDispatcher = gameRenderer.getFeatureRenderDispatcher();
        var submitNodeStorage = featureRenderDispatcher.getSubmitNodeStorage();

        gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_3D);
        renderState.blockModelRenderState().submit(poseStack, submitNodeStorage, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
        featureRenderDispatcher.renderAllFeatures();
    }

    @Override
    protected String getTextureLabel() {
        return "elementalcraft:block";
    }
}
