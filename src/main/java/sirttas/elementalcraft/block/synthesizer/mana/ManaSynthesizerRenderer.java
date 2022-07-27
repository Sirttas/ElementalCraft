package sirttas.elementalcraft.block.synthesizer.mana;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerRenderer;

import javax.annotation.Nonnull;

public class ManaSynthesizerRenderer extends SolarSynthesizerRenderer<ManaSynthesizerBlockEntity> {

	@Override
	public void render(ManaSynthesizerBlockEntity te, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		float tick = getClientTicks(partialTicks);
		renderRunes(matrixStack, buffer, te.getRuneHandler(), tick, light, overlay);

		var elementType = getElementType(te);

		if (elementType != ElementType.NONE) {
			float r = elementType.getRed();
			float g = elementType.getGreen();
			float b = elementType.getBlue();

			matrixStack.translate(0.5, 0.5, 0.5);
			if (te.isWorking()) {
				matrixStack.translate(0, 3D / 16, 0);
				matrixStack.mulPose(Vector3f.YP.rotationDegrees(tick));
			}
			matrixStack.translate(-3D / 16, -1D / 32, -3D / 16);
			Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(RenderType.translucent()), te.getBlockState(), getLenseModel(), r, g, b, light, overlay, getModelData(getLenseModel(), te), RenderType.translucent());
		}
	}
}
