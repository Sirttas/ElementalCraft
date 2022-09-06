package sirttas.elementalcraft.block.sorter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import sirttas.elementalcraft.block.entity.renderer.IECRenderer;

import javax.annotation.Nonnull;
import java.util.List;

public class SorterRenderer implements IECRenderer<SorterBlockEntity> {


	@SuppressWarnings("resource")
	@Override
	public void render(SorterBlockEntity sorter, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		HitResult mouseOver = Minecraft.getInstance().hitResult;
		boolean sneeking =  Minecraft.getInstance().player.isShiftKeyDown();
		List<ItemStack> stacks = sorter.getStacks();

		if (mouseOver != null && mouseOver.getType() == HitResult.Type.BLOCK && !stacks.isEmpty()) {
			poseStack.pushPose();

			BlockHitResult result = (BlockHitResult) mouseOver;
			
			if (sorter.getBlockPos().equals(result.getBlockPos())) {
				int index = sorter.getIndex();
				Quaternion rotation = result.getDirection().getRotation();
				Vector3f newPos = new Vector3f(0, 2F / 16, 1F / 16);

				poseStack.translate(0.5, 0.5, 0.5);
				newPos.transform(rotation);
				poseStack.translate(newPos.x(), newPos.y(), newPos.z());
				poseStack.mulPose(rotation);
				poseStack.mulPose(Vector3f.XP.rotationDegrees(-90F));
				poseStack.scale(0.5F, 0.5F, 0.5F);
				this.renderItem(stacks.get(index), poseStack, buffer, light, overlay);
				poseStack.scale(0.5F, 0.5F, 0.5F);
				poseStack.pushPose();
				translate(poseStack, 0.5, sneeking);
				for (int i = index - 1; i >= 0; i--) {
					translate(poseStack, 0.5, sneeking);
					this.renderItem(stacks.get(i), poseStack, buffer, light, overlay);
				}
				poseStack.popPose();
				poseStack.pushPose();
				translate(poseStack, -0.5, sneeking);
				for (int i = index + 1; i < stacks.size(); i++) {
					translate(poseStack, -0.5, sneeking);
					this.renderItem(stacks.get(i), poseStack, buffer, light, overlay);
				}
				poseStack.popPose();
			}
			poseStack.popPose();
		}
		rendereRunes(sorter, partialTicks, poseStack, buffer, light, overlay);
	}

	private void rendereRunes(SorterBlockEntity sorter, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		poseStack.translate(0.5F, 0.5F, 0.5F);

		var state = sorter.getBlockState();
		var rotation1 = state.getValue(ISorterBlock.SOURCE).getOpposite().getRotation();
		var rotation2 = state.getValue(ISorterBlock.TARGET).getRotation();
		var rotation = new Quaternion(rotation1.i() + rotation2.i(), rotation1.j() + rotation2.j(), rotation1.k() + rotation2.k(), rotation1.r() + rotation2.r());

		rotation.normalize();
		poseStack.mulPose(rotation);

		poseStack.translate(-0.5F, -0.75F, -0.5F);
		renderRunes(poseStack, buffer, sorter.getRuneHandler(), getAngle(partialTicks), light, overlay);
	}

	private void translate(PoseStack matrixStack, double value, boolean sneeking) {
		if (sneeking) {
			matrixStack.translate(-value, 0, 0);
		} else {
			matrixStack.translate(0, value, 0);
		}
	}
}
