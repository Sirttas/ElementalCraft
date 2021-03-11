package sirttas.elementalcraft.block.instrument.firefurnace;

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
public class RendererFireFurnace extends AbstractRendererEC<AbstractTileFireFurnace<?>> {

	public RendererFireFurnace(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	@Override
	public void render(AbstractTileFireFurnace<?> te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		IInventory inv = te.getInventory();
		ItemStack stack = inv.getStackInSlot(0);
		ItemStack stack2 = inv.getStackInSlot(1);
		matrixStack.translate(0.5F, 0.3F, 0.5F);
		float tick = getAngle(partialTicks);
		
		renderRunes(matrixStack, buffer, te.getRuneHandler(), tick, light, overlay);
		if (!stack.isEmpty() || !stack2.isEmpty()) {
			matrixStack.rotate(Vector3f.YP.rotationDegrees(tick));
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
