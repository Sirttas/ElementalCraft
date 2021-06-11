package sirttas.elementalcraft.block.instrument.purifier;


import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.entity.renderer.AbstractECRenderer;

@OnlyIn(Dist.CLIENT)
public class PurifierRenderer extends AbstractECRenderer<PurifierBlockEntity> {

	public PurifierRenderer(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	@Override
	public void render(PurifierBlockEntity te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		IInventory inv = te.getInventory();
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
