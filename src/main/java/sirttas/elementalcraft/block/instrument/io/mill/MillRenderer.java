package sirttas.elementalcraft.block.instrument.io.mill;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.block.airmill.AirMill;
import sirttas.elementalcraft.block.instrument.io.IOInstrumentRenderer;
import sirttas.elementalcraft.client.model.SimpleStandaloneModelSupplier;
import sirttas.elementalcraft.client.renderer.ECRendererHelper;

@OnlyIn(Dist.CLIENT)
public class MillRenderer<T extends AbstractMillBlockEntity<?>> extends IOInstrumentRenderer<T, MillRenderState> {

	public static final SimpleStandaloneModelSupplier WATER_MILL_GRINDSTONE_SHAFT = new SimpleStandaloneModelSupplier("water_mill_grindstone_shaft");
	public static final SimpleStandaloneModelSupplier AIR_MILL_GRINDSTONE_SHAFT = new SimpleStandaloneModelSupplier("air_mill_grindstone_shaft");
	public static final SimpleStandaloneModelSupplier WATER_MILL_WOOD_SAW_SHAFT = new SimpleStandaloneModelSupplier("water_mill_wood_saw_shaft");
	public static final SimpleStandaloneModelSupplier AIR_MILL_WOOD_SAW_SHAFT = new SimpleStandaloneModelSupplier("air_mill_wood_saw_shaft");

    private final BlockStateModelPart model;

	public MillRenderer(BlockEntityRendererProvider.@NotNull Context context, SimpleStandaloneModelSupplier model) {
        super(context);
        this.model = model.loadModel();
	}

    @Override
    public @NotNull MillRenderState createRenderState() {
        return new MillRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, MillRenderState renderState, float partialTick, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.shaft.update(this.model, (blockEntity instanceof AirMill airMill) && airMill.isBroken(), blockEntity.isRunning(), partialTick);
        renderState.animationTime = ECRendererHelper.getClientTicks(partialTick);
    }

    @Override
    public void submit(@NotNull MillRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, @NotNull CameraRenderState cameraRenderState) {
        renderState.runes.submit(renderState, poseStack, nodeCollector);
		if (!renderState.shaft.isBroken()) {
			poseStack.pushPose();
			poseStack.translate(0, 1 / 4D, 0);
            renderState.shaft.submit(renderState, poseStack, nodeCollector);
			poseStack.popPose();
		}
		if (!renderState.material.isEmpty() || !renderState.result.isEmpty()) {
			poseStack.translate(0.5, 0.3, 0.5);
			poseStack.mulPose(ECRendererHelper.getRotation(renderState.facing));
			poseStack.translate(0, 0, -3 / 8D);
			if (!renderState.material.isEmpty()) {
				poseStack.pushPose();
				poseStack.mulPose(Axis.YP.rotationDegrees(renderState.animationTime));
                renderState.material.submit(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
				poseStack.popPose();
			}
			if (!renderState.result.isEmpty()) {
				poseStack.translate(0, 0, 3 / 4D);
				poseStack.mulPose(Axis.YP.rotationDegrees(renderState.animationTime));
                renderState.result.submit(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			}
		}
	}
}
