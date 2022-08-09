package sirttas.elementalcraft.block.instrument.io.mill.saw;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.entity.renderer.IECRenderer;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class WaterMillWoodSawRenderer implements IECRenderer<WaterMillWoodSawBlockEntity> {

	@Override
	public void render(@Nonnull WaterMillWoodSawBlockEntity te, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		Container inv = te.getInventory();
		ItemStack stack = inv.getItem(0);
		ItemStack stack2 = inv.getItem(1);
		float tick = getClientTicks(partialTicks);
		
		renderRunes(matrixStack, buffer, te.getRuneHandler(), tick, light, overlay);
		matrixStack.pushPose();
		matrixStack.translate(0, 1 / 4D, 0);
		if (te.isRunning()) {
			matrixStack.translate(0.5, 0, 0.5);
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(-5 * tick));
			matrixStack.translate(-0.5, 0, -0.5);
		}
		matrixStack.popPose();
		if (!stack.isEmpty() || !stack2.isEmpty()) {
			matrixStack.translate(0.5, 0.3, 0.5);
			matrixStack.mulPose(getRotation(te.getBlockState().getValue(WaterMillWoodSawBlock.FACING)));
			matrixStack.translate(0, 0, -3 / 8D);
			if (!stack.isEmpty()) {
				matrixStack.pushPose();
				matrixStack.mulPose(Vector3f.YP.rotationDegrees(tick));
				renderItem(stack, matrixStack, buffer, light, overlay);
				matrixStack.popPose();
			}
			if (!stack2.isEmpty()) {
				matrixStack.translate(0, 0, 3 / 4D);
				matrixStack.mulPose(Vector3f.YP.rotationDegrees(tick));
				renderItem(stack2, matrixStack, buffer, light, overlay);
			}
		}
	}
}
