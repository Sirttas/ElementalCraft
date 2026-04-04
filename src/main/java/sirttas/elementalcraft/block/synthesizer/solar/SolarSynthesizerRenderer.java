package sirttas.elementalcraft.block.synthesizer.solar;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.client.model.ECModelResolver;
import sirttas.elementalcraft.client.model.SimpleStandaloneModelSupplier;
import sirttas.elementalcraft.client.renderer.ECRendererHelper;
import sirttas.elementalcraft.rune.RuneModelResolver;

public class SolarSynthesizerRenderer implements BlockEntityRenderer<@NotNull SolarSynthesizerBlockEntity, @NotNull SolarSynthesizerRenderState> {

	public static final Material BEAM = ECRendererHelper.getBlockMaterial("effect/solar_fire_synthesizer_beam");
    public static final SimpleStandaloneModelSupplier LENS = new SimpleStandaloneModelSupplier("solar_fire_synthesizer_lens");

	private static final float RED = ElementType.FIRE.getRed();
	private static final float GREEN = ElementType.FIRE.getGreen();
	private static final float BLUE = ElementType.FIRE.getBlue();

    private final BlockStateModelPart lensModel;
    private final RuneModelResolver runeModelResolver;


    public SolarSynthesizerRenderer() {
        this.runeModelResolver = ECModelResolver.get(RuneModelResolver.IDENTIFIER);
        lensModel = LENS.loadModel();
    }

    @Override
    public SolarSynthesizerRenderState createRenderState() {
        return new SolarSynthesizerRenderState();
    }

    @Override
    public void extractRenderState(@NotNull SolarSynthesizerBlockEntity blockEntity, @NotNull SolarSynthesizerRenderState state, float partialTicks, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.runes.update(blockEntity, runeModelResolver, partialTicks);
        if (blockEntity.isReceivingSkyLight()) {
            state.lensRotation = Axis.ZP.rotation(Minecraft.getInstance().gameRenderer.getGameRenderState().levelRenderState.skyRenderState.sunAngle);
            state.running = blockEntity.isWorking();
        } else {
            state.lensRotation = Axis.ZP.rotationDegrees(90);
            state.running = false;
        }

    }

    @Override
    public void submit(SolarSynthesizerRenderState state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState cameraRenderState) {
        state.runes.submit(state, poseStack, submitNodeCollector);
		poseStack.pushPose();
		poseStack.translate(0.5, 14.5 / 16, 0.5);
        poseStack.mulPose(state.lensRotation);
		poseStack.translate(-3D / 16, -1D / 32, -3D / 16);
        ECRendererHelper.submitModel(lensModel, poseStack, submitNodeCollector, state.lightCoords);
		poseStack.popPose();
		if (state.running) {
			Vec3 beamVect = Vec3.atCenterOf(state.blockPos).subtract(cameraRenderState.pos).multiply(1, 0, 1).normalize();

			poseStack.pushPose();
			poseStack.translate(0.5, 0.5, 0.5);
			poseStack.mulPose(Axis.YP.rotation((float) Math.acos(beamVect.z * (beamVect.x > 0 ? 1 : -1))));
			poseStack.scale(0.006F, 0.006F, 0.006F);
			ECRendererHelper.submitIcon(poseStack, submitNodeCollector, -21, 38, BEAM, 42, -76, RED, GREEN, BLUE, state.lightCoords);
			poseStack.popPose();
		}
	}

	protected ElementType getElementType(SolarSynthesizerBlockEntity te) {
		return ElementType.getElementType(te.getInventory().getItem(0));
	}
}
