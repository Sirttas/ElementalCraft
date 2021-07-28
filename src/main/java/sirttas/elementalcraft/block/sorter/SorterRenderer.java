package sirttas.elementalcraft.block.sorter;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import sirttas.elementalcraft.block.entity.renderer.IECRenderer;

public class SorterRenderer implements IECRenderer<SorterBlockEntity> {

	public SorterRenderer(Context context) {}

	@SuppressWarnings("resource")
	@Override
	public void render(SorterBlockEntity sorter, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
		HitResult mouseOver = Minecraft.getInstance().hitResult;
		boolean sneeking =  Minecraft.getInstance().player.isShiftKeyDown();
		List<ItemStack> stacks = sorter.getStacks();

		if (mouseOver != null && mouseOver.getType() == HitResult.Type.BLOCK && !stacks.isEmpty()) {
			BlockHitResult result = (BlockHitResult) mouseOver;
			
			if (sorter.getBlockPos().equals(result.getBlockPos())) {
				int index = sorter.getIndex();
				Quaternion rotation = result.getDirection().getRotation();
				Vector3f newPos = new Vector3f(0, 2F / 16, 1F / 16);

				matrixStack.translate(0.5, 0.5, 0.5);
				newPos.transform(rotation);
				matrixStack.translate(newPos.x(), newPos.y(), newPos.z());
				matrixStack.mulPose(rotation);
				matrixStack.mulPose(Vector3f.XP.rotationDegrees(-90F));
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				this.renderItem(stacks.get(index), matrixStack, buffer, light, overlay);
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				matrixStack.pushPose();
				translate(matrixStack, 0.5, sneeking);
				for (int i = index - 1; i >= 0; i--) {
					translate(matrixStack, 0.5, sneeking);
					this.renderItem(stacks.get(i), matrixStack, buffer, light, overlay);
				}
				matrixStack.popPose();
				matrixStack.pushPose();
				translate(matrixStack, -0.5, sneeking);
				for (int i = index + 1; i < stacks.size(); i++) {
					translate(matrixStack, -0.5, sneeking);
					this.renderItem(stacks.get(i), matrixStack, buffer, light, overlay);
				}
				matrixStack.popPose();
			}
		}
	}
	
	private void translate(PoseStack matrixStack, double value, boolean sneeking) {
		if (sneeking) {
			matrixStack.translate(-value, 0, 0);
		} else {
			matrixStack.translate(0, value, 0);
		}
	}
}
