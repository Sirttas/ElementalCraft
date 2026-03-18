package sirttas.elementalcraft.block.synthesizer.solar;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelIdentifier;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.client.model.ECModelHelper;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class SolarSynthesizerRenderer implements BlockEntityRenderer<SolarSynthesizerBlockEntity> {

	public static final Material BEAM = ECRendererHelper.getBlockMaterial("effect/solar_fire_synthesizer_beam");
	public static final ModelIdentifier LENS_LOCATION = ECModelHelper.createStandaloneKey("block/solar_fire_synthesizer_lens");

	private static final float RED = ElementType.FIRE.getRed();
	private static final float GREEN = ElementType.FIRE.getGreen();
	private static final float BLUE = ElementType.FIRE.getBlue();

	private static BakedModel lensModel;

	@Override
	public void render(SolarSynthesizerBlockEntity solarSynthesizer, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		ECRendererHelper.renderRunes(matrixStack, buffer, solarSynthesizer.getRuneHandler(), ECRendererHelper.getClientTicks(partialTicks), light, overlay);

		Minecraft minecraft = Minecraft.getInstance();
		boolean receivingSkyLight = solarSynthesizer.isReceivingSkyLight();

		matrixStack.pushPose();
		matrixStack.translate(0.5, 14.5 / 16, 0.5);
		if (receivingSkyLight) {
			matrixStack.mulPose(Axis.ZP.rotation(solarSynthesizer.getLevel().getSunAngle(partialTicks)));
		} else {
			matrixStack.mulPose(Axis.ZP.rotationDegrees(90));
		}
		matrixStack.translate(-3D / 16, -1D / 32, -3D / 16);
		minecraft.getBlockRenderer().getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(RenderType.translucent()), solarSynthesizer.getBlockState(), getLensModel(), RED, GREEN, BLUE, light, overlay, ECRendererHelper.getModelData(getLensModel(), solarSynthesizer), RenderType.translucent());
		matrixStack.popPose();
		if (receivingSkyLight) {
			Vec3 beamVect = Vec3.atCenterOf(solarSynthesizer.getBlockPos()).subtract(minecraft.getEntityRenderDispatcher().camera.getPosition()).multiply(1, 0, 1).normalize();

			matrixStack.pushPose();
			matrixStack.translate(0.5, 0.5, 0.5);
			matrixStack.mulPose(Axis.YP.rotation((float) Math.acos(beamVect.z * (beamVect.x > 0 ? 1 : -1))));
			matrixStack.scale(0.006F, 0.006F, 0.006F);
			ECRendererHelper.renderIcon(matrixStack, buffer, -21, 38, BEAM, 42, -76, RED, GREEN, BLUE, light, overlay);
			matrixStack.popPose();
		}
	}

	protected ElementType getElementType(SolarSynthesizerBlockEntity te) {
		return ElementType.getElementType(te.getInventory().getItem(0));
	}

	private synchronized BakedModel getLensModel() {
		if (lensModel == null) {
			Minecraft minecraft = Minecraft.getInstance();

			lensModel = minecraft.getModelManager().getModel(LENS_LOCATION);
		}
		return lensModel;
	}
}
