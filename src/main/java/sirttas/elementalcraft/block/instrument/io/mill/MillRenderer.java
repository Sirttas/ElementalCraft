package sirttas.elementalcraft.block.instrument.io.mill;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.block.airmill.AirMill;
import sirttas.elementalcraft.block.instrument.io.IOInstrumentRenderer;
import sirttas.elementalcraft.client.model.ECModelHelper;
import sirttas.elementalcraft.renderer.ECRendererHelper;

@OnlyIn(Dist.CLIENT)
public class MillRenderer<T extends AbstractMillBlockEntity<?>> extends IOInstrumentRenderer<T, MillRenderState> {

	public static final StandaloneModelKey<@NotNull BlockModelPart> WATER_MILL_GRINDSTONE_SHAFT_LOCATION = ECModelHelper.createStandaloneKey("water_mill_grindstone_shaft");
	public static final StandaloneModelKey<@NotNull BlockModelPart> AIR_MILL_GRINDSTONE_SHAFT_LOCATION = ECModelHelper.createStandaloneKey("air_mill_grindstone_shaft");
	public static final StandaloneModelKey<@NotNull BlockModelPart> WATER_MILL_WOOD_SAW_SHAFT_LOCATION = ECModelHelper.createStandaloneKey("water_mill_wood_saw_shaft");
	public static final StandaloneModelKey<@NotNull BlockModelPart> AIR_MILL_WOOD_SAW_SHAFT_LOCATION = ECModelHelper.createStandaloneKey("air_mill_wood_saw_shaft");

    private final BlockStateModel model;

	public MillRenderer(BlockEntityRendererProvider.@NotNull Context context, StandaloneModelKey<@NotNull BlockModelPart> key) {
        super(context);
        model = ECModelHelper.loadStandaloneModel(key);
	}

    @Override
    public @NotNull MillRenderState createRenderState() {
        return new MillRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, MillRenderState renderState, float partialTick, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.broken = (blockEntity instanceof AirMill airMill) && airMill.isBroken();
        renderState.running = blockEntity.isRunning();
    }

    @Override
    public void submit(@NotNull MillRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, @NotNull CameraRenderState cameraRenderState) {
		float tick = ECRendererHelper.getClientTicks(renderState.partialTick);

        renderState.runes.submit(renderState, poseStack, nodeCollector);
		if (!renderState.broken) {
			poseStack.pushPose();
			poseStack.translate(0, 1 / 4D, 0);
			if (renderState.running) {
				poseStack.translate(0.5, 0, 0.5);
				poseStack.mulPose(Axis.YP.rotationDegrees(-5 * tick));
				poseStack.translate(-0.5, 0, -0.5);
			}
            ECRendererHelper.submitModel(model, poseStack, nodeCollector, renderState.lightCoords);
			poseStack.popPose();
		}
		if (!renderState.material.isEmpty() || !renderState.result.isEmpty()) {
			poseStack.translate(0.5, 0.3, 0.5);
			poseStack.mulPose(ECRendererHelper.getRotation(renderState.facing));
			poseStack.translate(0, 0, -3 / 8D);
			if (!renderState.material.isEmpty()) {
				poseStack.pushPose();
				poseStack.mulPose(Axis.YP.rotationDegrees(tick));
                renderState.material.submit(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
				poseStack.popPose();
			}
			if (!renderState.result.isEmpty()) {
				poseStack.translate(0, 0, 3 / 4D);
				poseStack.mulPose(Axis.YP.rotationDegrees(tick));
                renderState.result.submit(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			}
		}
	}
}
