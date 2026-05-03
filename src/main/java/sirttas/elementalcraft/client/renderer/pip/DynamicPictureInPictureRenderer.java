package sirttas.elementalcraft.client.renderer.pip;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.jspecify.annotations.NonNull;

public class DynamicPictureInPictureRenderer extends PictureInPictureRenderer<DynamicPictureInPictureRenderState> {

    private static final float SCALE = 30;

    public DynamicPictureInPictureRenderer(MultiBufferSource.BufferSource bufferSource) {
        super(bufferSource);
    }

    @Override
    public @NonNull Class<DynamicPictureInPictureRenderState> getRenderStateClass() {
        return DynamicPictureInPictureRenderState.class;
    }

    @Override
    protected void renderToTexture(@NonNull DynamicPictureInPictureRenderState renderState, @NonNull PoseStack poseStack) {

        poseStack.translate(-52, -16, 0);
        poseStack.scale(SCALE, -SCALE, -SCALE);
        poseStack.mulPose(Axis.XP.rotationDegrees(30.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(40.0F));

        var minecraft = Minecraft.getInstance();
        var gameRenderer = minecraft.gameRenderer;
        var featureRenderDispatcher = gameRenderer.getFeatureRenderDispatcher();
        var submitNodeStorage = featureRenderDispatcher.getSubmitNodeStorage();

        gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_3D);
        renderState.submit().apply(submitNodeStorage, poseStack);
        featureRenderDispatcher.renderAllFeatures();
    }

    @Override
    protected String getTextureLabel() {
        return "elementalcraft:block";
    }
}
