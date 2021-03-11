package sirttas.elementalcraft.block.instrument.binder;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.tile.renderer.AbstractRendererEC;

@OnlyIn(Dist.CLIENT)
public class RendererBinder<T extends TileBinder> extends AbstractRendererEC<T> {
	public RendererBinder(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	@Override
	public void render(T te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		float tick = getAngle(partialTicks);
		IInventory inv = te.getInventory();
		
		renderRunes(matrixStack, buffer, te.getRuneHandler(), tick, light, overlay);
		matrixStack.translate(0.5F, 0.4F, 0.5F);
		if (te.getItemCount() == 1) {
			matrixStack.rotate(Vector3f.YP.rotationDegrees(tick));
			renderItem(inv.getStackInSlot(0), matrixStack, buffer, light, overlay);
		} else {
			matrixStack.scale(0.5F, 0.5F, 0.5F);
			for (int i = 0; i < te.getItemCount(); i++) {
				ItemStack stack = inv.getStackInSlot(i);

				if (!stack.isEmpty()) {
					matrixStack.rotate(Vector3f.YP.rotationDegrees(360F / te.getItemCount()));
					matrixStack.push();
					matrixStack.translate(0.7F, 0F, 0F);
					matrixStack.rotate(Vector3f.YP.rotationDegrees(tick));
					renderItem(stack, matrixStack, buffer, light, overlay);
					matrixStack.pop();
				}
			}
		}
	}
}
