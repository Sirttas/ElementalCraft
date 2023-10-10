package sirttas.elementalcraft.block.instrument.binder;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class BinderRenderer<T extends BinderBlockEntity> implements BlockEntityRenderer<T> {

	@Override
	public void render(@Nonnull T te, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		float tick = ECRendererHelper.getClientTicks(partialTicks);
		Container inv = te.getInventory();

		ECRendererHelper.renderRunes(matrixStack, buffer, te.getRuneHandler(), tick, light, overlay);
		matrixStack.translate(0.5F, 0.4F, 0.5F);
		if (te.getItemCount() == 1) {
			matrixStack.mulPose(Axis.YP.rotationDegrees(tick));
			ECRendererHelper.renderItem(inv.getItem(0), matrixStack, buffer, light, overlay);
		} else {
			matrixStack.scale(0.5F, 0.5F, 0.5F);
			for (int i = 0; i < te.getItemCount(); i++) {
				ItemStack stack = inv.getItem(i);

				if (!stack.isEmpty()) {
					matrixStack.mulPose(Axis.YP.rotationDegrees(360F / te.getItemCount()));
					matrixStack.pushPose();
					matrixStack.translate(0.7F, 0F, 0F);
					matrixStack.mulPose(Axis.YP.rotationDegrees(tick));
					ECRendererHelper.renderItem(stack, matrixStack, buffer, light, overlay);
					matrixStack.popPose();
				}
			}
		}
	}
}
