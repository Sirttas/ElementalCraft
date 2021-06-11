package sirttas.elementalcraft.block.instrument.inscriber;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.entity.renderer.AbstractECRenderer;
import sirttas.elementalcraft.block.instrument.InstrumentInventory;
import sirttas.elementalcraft.block.instrument.purifier.PurifierBlock;

@OnlyIn(Dist.CLIENT)
public class InscriberRenderer extends AbstractECRenderer<InscriberBlockEntity> {
	public InscriberRenderer(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	@Override
	public void render(InscriberBlockEntity te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		float tick = getAngle(partialTicks);
		InstrumentInventory inv = (InstrumentInventory) te.getInventory();

		matrixStack.translate(0F, 0.25F, 0F);
		renderRunes(matrixStack, buffer, te.getRuneHandler(), tick, light, overlay);
		matrixStack.translate(0.5F, 0.15F, 0.5F);
		matrixStack.mulPose(getRotation(te.getBlockState().getValue(PurifierBlock.FACING)));
		renderRuneSlate(matrixStack, buffer, light, overlay, inv.getItem(0));
		renderItems(matrixStack, buffer, light, overlay, tick, inv);
	}

	private void renderRuneSlate(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay, ItemStack stack) {
		if (!stack.isEmpty()) {
			matrixStack.pushPose();
			matrixStack.translate(0F, 0F, 0.0625F);
			matrixStack.mulPose(Vector3f.XP.rotationDegrees(22.5F));
			renderItem(stack, matrixStack, buffer, light, overlay);
			matrixStack.popPose();
		}
	}

	private void renderItems(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay, float tick, InstrumentInventory inv) {
		matrixStack.translate(-0.4F, -0.2F, -0.2F);
		for (int i = 1; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);

			if (!stack.isEmpty()) {
				matrixStack.pushPose();
				matrixStack.mulPose(Vector3f.YP.rotationDegrees(tick));
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				renderItem(stack, matrixStack, buffer, light, overlay);
				matrixStack.popPose();
				matrixStack.translate(0.4F, 0F, 0);
			}
		}
	}
}
