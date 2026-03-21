package sirttas.elementalcraft.block.instrument.io.mill;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.airmill.AirMill;
import sirttas.elementalcraft.block.instrument.io.IOInstrumentRenderState;
import sirttas.elementalcraft.block.instrument.io.IOInstrumentRenderer;
import sirttas.elementalcraft.client.model.ECModelHelper;
import sirttas.elementalcraft.renderer.ECRendererHelper;

@OnlyIn(Dist.CLIENT)
public class MillRenderer<T extends AbstractMillBlockEntity<?>> extends IOInstrumentRenderer<T, IOInstrumentRenderState> {

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
    public @NotNull IOInstrumentRenderState createRenderState() {
        return new IOInstrumentRenderState();
    }

    @Override
    public void submit(@NotNull IOInstrumentRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, @NotNull CameraRenderState cameraRenderState) {
		float tick = ECRendererHelper.getClientTicks(renderState.partialTick);

        renderState.runes.submit(renderState, poseStack, nodeCollector);
		if (!(te instanceof AirMill airMill) || !airMill.isBroken()) {
			poseStack.pushPose();
			poseStack.translate(0, 1 / 4D, 0);
			if (te.isRunning()) {
				poseStack.translate(0.5, 0, 0.5);
				poseStack.mulPose(Axis.YP.rotationDegrees(-5 * tick));
				poseStack.translate(-0.5, 0, -0.5);
			}
			ECRendererHelper.renderModel(model, poseStack, buffer, te, light, overlay);
			poseStack.popPose();
		}
		if (!stack.isEmpty() || !stack2.isEmpty()) {
			poseStack.translate(0.5, 0.3, 0.5);
			poseStack.mulPose(ECRendererHelper.getRotation(te.getBlockState().getValue(AbstractMillBlock.FACING)));
			poseStack.translate(0, 0, -3 / 8D);
			if (!stack.isEmpty()) {
				poseStack.pushPose();
				poseStack.mulPose(Axis.YP.rotationDegrees(tick));
				ECRendererHelper.renderItem(stack, poseStack, buffer, light, overlay);
				poseStack.popPose();
			}
			if (!stack2.isEmpty()) {
				poseStack.translate(0, 0, 3 / 4D);
				poseStack.mulPose(Axis.YP.rotationDegrees(tick));
				ECRendererHelper.renderItem(stack2, poseStack, buffer, light, overlay);
			}
		}
	}
}
