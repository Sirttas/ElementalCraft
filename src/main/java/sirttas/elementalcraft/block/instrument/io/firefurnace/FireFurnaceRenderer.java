package sirttas.elementalcraft.block.instrument.io.firefurnace;

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
public class FireFurnaceRenderer implements BlockEntityRenderer<AbstractFireFurnaceBlockEntity<?>> {

	@Override
	public void render(AbstractFireFurnaceBlockEntity<?> te, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		Container inv = te.getInventory();
		ItemStack stack = inv.getItem(0);
		ItemStack stack2 = inv.getItem(1);
		float tick = ECRendererHelper.getClientTicks(partialTicks);

		ECRendererHelper.renderRunes(poseStack, buffer, te.getRuneHandler(), tick, light, overlay);
		poseStack.translate(0.5F, 0.3F, 0.5F);
		if (!stack.isEmpty() || !stack2.isEmpty()) {
			poseStack.mulPose(Axis.YP.rotationDegrees(tick));
			if (!stack.isEmpty()) {
				ECRendererHelper.renderItem(stack, poseStack, buffer, light, overlay);
			}
			if (!stack2.isEmpty()) {
				poseStack.translate(0, 0.5F, 0);
				ECRendererHelper.renderItem(stack2, poseStack, buffer, light, overlay);
			}
		}
	}
}
