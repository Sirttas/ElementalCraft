package sirttas.elementalcraft.block.instrument.crystallizer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.instrument.InstrumentContainer;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class CrystallizerRenderer implements BlockEntityRenderer<CrystallizerBlockEntity> {

	@Override
	public void render(CrystallizerBlockEntity te, float partialTicks, PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		float tick = ECRendererHelper.getClientTicks(partialTicks);
		InstrumentContainer inv = (InstrumentContainer) te.getInventory();

		matrixStack.translate(0F, 0.25F, 0F);
		ECRendererHelper.renderRunes(matrixStack, buffer, te.getRuneHandler(), tick, light, overlay);
		matrixStack.translate(0.5F, 0.15F, 0.5F);
		matrixStack.scale(0.5F, 0.5F, 0.5F);
		renderGem(matrixStack, buffer, light, overlay, tick, inv.getItem(0));
		renderCrystal(matrixStack, buffer, light, overlay, tick, inv.getItem(1));
		renderShards(matrixStack, buffer, light, overlay, tick, inv);
	}

	private void renderGem(PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay, float tick, ItemStack stack) {
		if (!stack.isEmpty()) {
			matrixStack.pushPose();
			matrixStack.translate(0F, -0.15F, 0F);
			matrixStack.mulPose(Axis.YP.rotationDegrees(tick));
			ECRendererHelper.renderItem(stack, matrixStack, buffer, light, overlay);
			matrixStack.popPose();
		}
	}

	private void renderCrystal(PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay, float tick, ItemStack stack) {
		if (!stack.isEmpty()) {
			matrixStack.pushPose();
			matrixStack.translate(0F, 0.9F, 0F);
			matrixStack.mulPose(Axis.YP.rotationDegrees(-tick));
			ECRendererHelper.renderItem(stack, matrixStack, buffer, light, overlay);
			matrixStack.popPose();
		}
	}

	private void renderShards(PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay, float tick, InstrumentContainer inv) {
		matrixStack.mulPose(Axis.YP.rotationDegrees(tick * 2));
		for (int i = 2; i < inv.getItemCount(); i++) {
			ItemStack stack = inv.getItem(i);

			if (!stack.isEmpty()) {
				matrixStack.mulPose(Axis.YP.rotationDegrees(360F / (inv.getItemCount() - 2)));
				matrixStack.pushPose();
				matrixStack.translate(1F, 0F, 0F);
				ECRendererHelper.renderItem(stack, matrixStack, buffer, light, overlay);
				matrixStack.popPose();
			}
		}
	}

}
