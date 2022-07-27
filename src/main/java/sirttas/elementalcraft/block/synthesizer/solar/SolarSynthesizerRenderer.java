package sirttas.elementalcraft.block.synthesizer.solar;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.block.entity.renderer.IECRenderer;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class SolarSynthesizerRenderer<T extends SolarSynthesizerBlockEntity> implements IECRenderer<T> {

	public static final Material BEAM = ECRendererHelper.getBlockMaterial(ElementalCraft.createRL("effect/solar_synthesizer_beam"));
	public static final ResourceLocation LENSE_LOCATION = ElementalCraft.createRL("block/solar_synthesizer_lense");

	private static BakedModel lenseModel;

	@Override
	public void render(T te, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		renderRunes(matrixStack, buffer, te.getRuneHandler(), getClientTicks(partialTicks), light, overlay);

		var elementType = getElementType(te);

		if (elementType != ElementType.NONE) {
			Minecraft minecraft = Minecraft.getInstance();
			float r = elementType.getRed();
			float g = elementType.getGreen();
			float b = elementType.getBlue();
			boolean isWorking = te.isWorking();

			matrixStack.pushPose();
			matrixStack.translate(0.5, 14.5 / 16, 0.5);
			if (isWorking) {
				matrixStack.mulPose(Vector3f.ZP.rotation(te.getLevel().getSunAngle(partialTicks)));
			} else {
				matrixStack.mulPose(Vector3f.ZP.rotationDegrees(90));
			}
			matrixStack.translate(-3D / 16, -1D / 32, -3D / 16);
			minecraft.getBlockRenderer().getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(RenderType.translucent()), te.getBlockState(), getLenseModel(), r, g, b, light, overlay, getModelData(getLenseModel(), te), RenderType.translucent());
			matrixStack.popPose();
			if (isWorking) {
				Vec3 beamVect = Vec3.atCenterOf(te.getBlockPos()).subtract(minecraft.getEntityRenderDispatcher().camera.getPosition()).multiply(1, 0, 1).normalize();

				matrixStack.pushPose();
				matrixStack.translate(0.5, 0.5, 0.5);
				matrixStack.mulPose(Vector3f.YP.rotation((float) Math.acos(beamVect.z * (beamVect.x > 0 ? 1 : -1))));
				matrixStack.scale(0.006F, 0.006F, 0.006F);
				this.renderIcon(matrixStack, buffer, -21, 38, BEAM, 42, -76, r, g, b, light, overlay);
				matrixStack.popPose();
			}
		}
	}

	protected ElementType getElementType(T te) {
		ItemStack stack = te.getInventory().getItem(0);

		if (!stack.isEmpty()) {
			Item item = stack.getItem();

			return item instanceof IElementTypeProvider elementTypeProvider ? elementTypeProvider.getElementType() : ElementType.NONE;
		}
		return ElementType.NONE;
	}

	protected synchronized BakedModel getLenseModel() {
		if (lenseModel == null) {
			Minecraft minecraft = Minecraft.getInstance();

			lenseModel = minecraft.getModelManager().getModel(LENSE_LOCATION);
		}
		return lenseModel;
	}
}
