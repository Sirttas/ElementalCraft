package sirttas.elementalcraft.block.instrument.binder;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.entity.renderer.IECRenderer;

@OnlyIn(Dist.CLIENT)
public class BinderRenderer<T extends BinderBlockEntity> implements IECRenderer<T> {
	public BinderRenderer(Context context) {}

	@Override
	public void render(T te, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
		float tick = getAngle(partialTicks);
		Container inv = te.getInventory();
		
		renderRunes(matrixStack, buffer, te.getRuneHandler(), tick, light, overlay);
		matrixStack.translate(0.5F, 0.4F, 0.5F);
		if (te.getItemCount() == 1) {
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(tick));
			renderItem(inv.getItem(0), matrixStack, buffer, light, overlay);
		} else {
			matrixStack.scale(0.5F, 0.5F, 0.5F);
			for (int i = 0; i < te.getItemCount(); i++) {
				ItemStack stack = inv.getItem(i);

				if (!stack.isEmpty()) {
					matrixStack.mulPose(Vector3f.YP.rotationDegrees(360F / te.getItemCount()));
					matrixStack.pushPose();
					matrixStack.translate(0.7F, 0F, 0F);
					matrixStack.mulPose(Vector3f.YP.rotationDegrees(tick));
					renderItem(stack, matrixStack, buffer, light, overlay);
					matrixStack.popPose();
				}
			}
		}
	}
}
