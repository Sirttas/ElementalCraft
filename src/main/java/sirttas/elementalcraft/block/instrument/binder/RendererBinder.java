package sirttas.elementalcraft.block.instrument.binder;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.tile.renderer.RendererEC;
import sirttas.elementalcraft.item.ItemEC;

@OnlyIn(Dist.CLIENT)
public class RendererBinder extends RendererEC<TileBinder> {
	public RendererBinder(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	@Override
	public void render(TileBinder te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		float tick = getAngle(partialTicks);
		
		matrixStack.translate(0.5F, 0.4F, 0.5F);
		if (te.getItemCount() == 1) {
			matrixStack.rotate(Vector3f.YP.rotationDegrees(tick));
			renderItem(te.getStackInSlot(0), matrixStack, buffer, light, overlay);
		} else {
			matrixStack.scale(0.5F, 0.5F, 0.5F);
			for (int i = 0; i < te.getItemCount(); i++) {
				ItemStack stack = te.getStackInSlot(i);

				if (!ItemEC.isEmpty(stack)) {
					matrixStack.rotate(Vector3f.YP.rotationDegrees(360 / te.getItemCount()));
					matrixStack.translate(0.7F, 0F, 0F);
					matrixStack.rotate(Vector3f.YP.rotationDegrees(tick));
					renderItem(stack, matrixStack, buffer, light, overlay);
					matrixStack.rotate(Vector3f.YP.rotationDegrees(-tick));
					matrixStack.translate(-0.7F, 0F, 0F);
				}
			}
		}
	}
}
