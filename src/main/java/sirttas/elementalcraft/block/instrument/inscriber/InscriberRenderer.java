package sirttas.elementalcraft.block.instrument.inscriber;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.instrument.InstrumentContainer;
import sirttas.elementalcraft.block.instrument.io.purifier.PurifierBlock;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class InscriberRenderer implements BlockEntityRenderer<InscriberBlockEntity> {

	@Override
	public void render(InscriberBlockEntity te, float partialTicks, PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		float tick = ECRendererHelper.getClientTicks(partialTicks);
		InstrumentContainer inv = (InstrumentContainer) te.getInventory();

		matrixStack.translate(0F, 0.25F, 0F);
		ECRendererHelper.renderRunes(matrixStack, buffer, te.getRuneHandler(), tick, light, overlay);
		matrixStack.translate(0.5F, 0.15F, 0.5F);
		matrixStack.mulPose(ECRendererHelper.getRotation(te.getBlockState().getValue(PurifierBlock.FACING)));
		renderRuneSlate(matrixStack, buffer, light, overlay, inv.getItem(0));
		renderItems(matrixStack, buffer, light, overlay, tick, inv);
	}

	private void renderRuneSlate(PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay, ItemStack stack) {
		if (!stack.isEmpty()) {
			matrixStack.pushPose();
			matrixStack.translate(0F, 0F, 0.0625F);
			matrixStack.mulPose(Axis.XP.rotationDegrees(22.5F));
			ECRendererHelper.renderItem(stack, matrixStack, buffer, light, overlay);
			matrixStack.popPose();
		}
	}

	private void renderItems(PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay, float tick, InstrumentContainer inv) {
		matrixStack.translate(-0.4F, -0.2F, -0.2F);
		for (int i = 1; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);

			if (!stack.isEmpty()) {
				matrixStack.pushPose();
				matrixStack.mulPose(Axis.YP.rotationDegrees(tick));
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				ECRendererHelper.renderItem(stack, matrixStack, buffer, light, overlay);
				matrixStack.popPose();
				matrixStack.translate(0.4F, 0F, 0);
			}
		}
	}
}
