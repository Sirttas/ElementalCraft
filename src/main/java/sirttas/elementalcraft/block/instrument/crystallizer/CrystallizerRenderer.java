package sirttas.elementalcraft.block.instrument.crystallizer;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.entity.renderer.AbstractECRenderer;
import sirttas.elementalcraft.block.instrument.InstrumentInventory;

@OnlyIn(Dist.CLIENT)
public class CrystallizerRenderer extends AbstractECRenderer<CrystallizerBlockEntity> {
	public CrystallizerRenderer(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	@Override
	public void render(CrystallizerBlockEntity te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		float tick = getAngle(partialTicks);
		InstrumentInventory inv = (InstrumentInventory) te.getInventory();

		matrixStack.translate(0F, 0.25F, 0F);
		renderRunes(matrixStack, buffer, te.getRuneHandler(), tick, light, overlay);
		matrixStack.translate(0.5F, 0.15F, 0.5F);
		matrixStack.scale(0.5F, 0.5F, 0.5F);
		renderGem(matrixStack, buffer, light, overlay, tick, inv.getItem(0));
		renderCrystal(matrixStack, buffer, light, overlay, tick, inv.getItem(1));
		renderShards(matrixStack, buffer, light, overlay, tick, inv);
	}

	private void renderGem(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay, float tick, ItemStack stack) {
		if (!stack.isEmpty()) {
			matrixStack.pushPose();
			matrixStack.translate(0F, -0.15F, 0F);
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(tick));
			renderItem(stack, matrixStack, buffer, light, overlay);
			matrixStack.popPose();
		}
	}

	private void renderCrystal(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay, float tick, ItemStack stack) {
		if (!stack.isEmpty()) {
			matrixStack.pushPose();
			matrixStack.translate(0F, 0.9F, 0F);
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(-tick));
			renderItem(stack, matrixStack, buffer, light, overlay);
			matrixStack.popPose();
		}
	}

	private void renderShards(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay, float tick, InstrumentInventory inv) {
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(tick * 2));
		for (int i = 2; i < inv.getItemCount(); i++) {
			ItemStack stack = inv.getItem(i);

			if (!stack.isEmpty()) {
				matrixStack.mulPose(Vector3f.YP.rotationDegrees(360F / (inv.getItemCount() - 2)));
				matrixStack.pushPose();
				matrixStack.translate(1F, 0F, 0F);
				renderItem(stack, matrixStack, buffer, light, overlay);
				matrixStack.popPose();
			}
		}
	}

}
