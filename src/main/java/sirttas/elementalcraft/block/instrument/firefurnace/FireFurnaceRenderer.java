package sirttas.elementalcraft.block.instrument.firefurnace;

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
public class FireFurnaceRenderer implements IECRenderer<AbstractFireFurnaceBlockEntity<?>> {

	public FireFurnaceRenderer(Context context) {}

	@Override
	public void render(AbstractFireFurnaceBlockEntity<?> te, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
		Container inv = te.getInventory();
		ItemStack stack = inv.getItem(0);
		ItemStack stack2 = inv.getItem(1);
		matrixStack.translate(0.5F, 0.3F, 0.5F);
		float tick = getAngle(partialTicks);
		
		renderRunes(matrixStack, buffer, te.getRuneHandler(), tick, light, overlay);
		if (!stack.isEmpty() || !stack2.isEmpty()) {
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(tick));
			if (!stack.isEmpty()) {
				renderItem(stack, matrixStack, buffer, light, overlay);
			}
			if (!stack2.isEmpty()) {
				matrixStack.translate(0, 0.5F, 0);
				renderItem(stack2, matrixStack, buffer, light, overlay);
			}
		}
	}
}
