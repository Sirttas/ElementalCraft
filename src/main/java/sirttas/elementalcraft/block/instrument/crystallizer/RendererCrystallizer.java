package sirttas.elementalcraft.block.instrument.crystallizer;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.instrument.InstrumentInventory;
import sirttas.elementalcraft.block.tile.renderer.AbstractRendererEC;

@OnlyIn(Dist.CLIENT)
public class RendererCrystallizer extends AbstractRendererEC<TileCrystallizer> {
	public RendererCrystallizer(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	@Override
	public void render(TileCrystallizer te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		float tick = getAngle(partialTicks);
		InstrumentInventory inv = (InstrumentInventory) te.getInventory();

		matrixStack.translate(0F, 0.25F, 0F);
		renderRunes(matrixStack, buffer, te.getRuneHandler(), tick, light, overlay);
		matrixStack.translate(0.5F, 0.15F, 0.5F);
		matrixStack.scale(0.5F, 0.5F, 0.5F);
		renderGem(matrixStack, buffer, light, overlay, tick, inv.getStackInSlot(0));
		renderCrystal(matrixStack, buffer, light, overlay, tick, inv.getStackInSlot(1));
		renderShards(matrixStack, buffer, light, overlay, tick, inv);
	}

	private void renderGem(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay, float tick, ItemStack stack) {
		if (!stack.isEmpty()) {
			matrixStack.push();
			matrixStack.translate(0F, -0.15F, 0F);
			matrixStack.rotate(Vector3f.YP.rotationDegrees(tick));
			renderItem(stack, matrixStack, buffer, light, overlay);
			matrixStack.pop();
		}
	}

	private void renderCrystal(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay, float tick, ItemStack stack) {
		if (!stack.isEmpty()) {
			matrixStack.push();
			matrixStack.translate(0F, 0.9F, 0F);
			matrixStack.rotate(Vector3f.YP.rotationDegrees(-tick));
			renderItem(stack, matrixStack, buffer, light, overlay);
			matrixStack.pop();
		}
	}

	private void renderShards(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay, float tick, InstrumentInventory inv) {
		matrixStack.rotate(Vector3f.YP.rotationDegrees(tick * 2));
		for (int i = 2; i < inv.getItemCount(); i++) {
			ItemStack stack = inv.getStackInSlot(i);

			if (!stack.isEmpty()) {
				matrixStack.rotate(Vector3f.YP.rotationDegrees(360F / (inv.getItemCount() - 2)));
				matrixStack.push();
				matrixStack.translate(1F, 0F, 0F);
				renderItem(stack, matrixStack, buffer, light, overlay);
				matrixStack.pop();
			}
		}
	}

}
