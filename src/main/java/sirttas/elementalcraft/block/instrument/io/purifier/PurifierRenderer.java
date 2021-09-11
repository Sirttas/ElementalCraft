package sirttas.elementalcraft.block.instrument.io.purifier;


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
public class PurifierRenderer implements IECRenderer<PurifierBlockEntity> {

	public PurifierRenderer(Context context) {}

	@Override
	public void render(PurifierBlockEntity te, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
		Container inv = te.getInventory();
		ItemStack stack = inv.getItem(0);
		ItemStack stack2 = inv.getItem(1);
		float tick = getAngle(partialTicks);
		
		renderRunes(matrixStack, buffer, te.getRuneHandler(), tick, light, overlay);
		if (!stack.isEmpty() || !stack2.isEmpty()) {
			matrixStack.translate(0.5, 0.5, 0.5);
			matrixStack.mulPose(getRotation(te.getBlockState().getValue(PurifierBlock.FACING)));
			matrixStack.translate(0, -5D / 16, -6D / 16);
			if (!stack.isEmpty()) {
				renderItem(stack, matrixStack, buffer, light, overlay);
			}
			if (!stack2.isEmpty()) {
				matrixStack.translate(0, 0.6, 6D / 16);
				matrixStack.mulPose(Vector3f.YP.rotationDegrees(tick));
				renderItem(stack2, matrixStack, buffer, light, overlay);
			}
		}
	}
}
