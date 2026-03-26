package sirttas.elementalcraft.block.diffuser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.util.Util;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.client.model.SimpleStandaloneModelSupplier;
import sirttas.elementalcraft.renderer.ECRendererHelper;

@OnlyIn(Dist.CLIENT)
public class DiffuserRenderer implements BlockEntityRenderer<@NotNull DiffuserBlockEntity, @NotNull DiffuserRenderState> {
	
	public static final SimpleStandaloneModelSupplier CUBE = new SimpleStandaloneModelSupplier("diffuser_cube");

	private static final Quaternionf ROTATION = Util.make(() -> {
        var axis = Axis.XP.rotationDegrees(45);

        axis.mul(Axis.ZP.rotationDegrees(45));
        return axis;
    });
	
	private final BlockStateModel cubeModel;

    public DiffuserRenderer() {
        cubeModel = CUBE.loadModel();
    }

    @Override
    public DiffuserRenderState createRenderState() {
        return new DiffuserRenderState();
    }

    @Override
    public void extractRenderState(DiffuserBlockEntity blockEntity, DiffuserRenderState renderState, float partialTick, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.partialTick = partialTick;
        renderState.runes.update(blockEntity.getRuneHandler(), partialTick);
        if (blockEntity.showsRange()) {
            renderState.range.update(blockEntity, blockEntity.getRange(), ARGB.colorFromFloat(1, 1, 1, 0.6F));
        } else {
            renderState.range.clear();
        }
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
        ECRendererHelper.submitModel(cubeModel, poseStack, nodeCollector, renderState.lightCoords);
        poseStack.popPose();
    }
}
