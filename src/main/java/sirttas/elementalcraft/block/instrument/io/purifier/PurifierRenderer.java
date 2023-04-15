package sirttas.elementalcraft.block.instrument.io.purifier;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.Container;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.renderer.ECRendererHelper;

@OnlyIn(Dist.CLIENT)
public class PurifierRenderer implements BlockEntityRenderer<PurifierBlockEntity> {

	@Override
	public void render(PurifierBlockEntity te, float partialTicks, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource buffer, int light, int overlay) {
		Container inv = te.getInventory();
		ItemStack stack = inv.getItem(0);
		ItemStack stack2 = inv.getItem(1);
		float tick = ECRendererHelper.getClientTicks(partialTicks);

		ECRendererHelper.renderRunes(matrixStack, buffer, te.getRuneHandler(), tick, light, overlay);
		if (!stack.isEmpty() || !stack2.isEmpty()) {
			matrixStack.translate(0.5, 0.5, 0.5);
			matrixStack.mulPose(ECRendererHelper.getRotation(te.getBlockState().getValue(PurifierBlock.FACING)));
			if (!stack.isEmpty()) {
				matrixStack.pushPose();
				matrixStack.translate(0, -5D / 16, -6D / 16);
				if (!(stack.getItem() instanceof BlockItem)) {
					matrixStack.translate(0, 2D / 16, 0);
					matrixStack.scale(0.5F, 0.5F, 0.5F);
					matrixStack.mulPose(Vector3f.YP.rotationDegrees(tick));
				}
				ECRendererHelper.renderItem(stack, matrixStack, buffer, light, overlay);
				matrixStack.popPose();
			}
			if (!stack2.isEmpty()) {
				matrixStack.translate(0, 4.6 / 16, 0);
				matrixStack.mulPose(Vector3f.YP.rotationDegrees(tick));
				ECRendererHelper.renderItem(stack2, matrixStack, buffer, light, overlay);
			}
		}
	}
}
