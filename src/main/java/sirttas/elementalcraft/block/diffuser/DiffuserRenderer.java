package sirttas.elementalcraft.block.diffuser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Util;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import sirttas.elementalcraft.client.model.ECModelHelper;
import sirttas.elementalcraft.renderer.ECRendererHelper;

@OnlyIn(Dist.CLIENT)
public class DiffuserRenderer implements BlockEntityRenderer<@NotNull DiffuserBlockEntity, @NotNull DiffuserRenderState> {
	
	public static final StandaloneModelKey<@NotNull BlockModelPart> CUBE_LOCATION = ECModelHelper.createStandaloneKey("diffuser_cube");
	
	private static final Quaternionf ROTATION = Util.make(() -> {
        var axis = Axis.XP.rotationDegrees(45);

        axis.mul(Axis.ZP.rotationDegrees(45));
        return axis;
    });
	
	private final BlockStateModel cubeModel;

    public DiffuserRenderer() {
        cubeModel = ECModelHelper.loadStandaloneModel(CUBE_LOCATION);
    }

    @Override
    public DiffuserRenderState createRenderState() {
        return new DiffuserRenderState();
    }

    @Override
    public void submit(DiffuserRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, @NotNull CameraRenderState cameraRenderState) {
        renderState.range.submit();

        float angle = ECRendererHelper.getClientTicks(renderState.partialTick);

        renderState.runes.submit(renderState, poseStack, nodeCollector);
        poseStack.pushPose();
        poseStack.translate(0.5, 1.1, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));
        poseStack.mulPose(ROTATION);
        poseStack.translate(-3D / 16, -3D / 16, -3D / 16);
        nodeCollector.submitCustomGeometry(poseStack, RenderTypes.solidMovingBlock(), (pose, consumer) -> ModelBlockRenderer.renderModel(pose, consumer, cubeModel, 1, 1, 1, renderState.lightCoords, OverlayTexture.NO_OVERLAY));
        poseStack.popPose();
    }
}
