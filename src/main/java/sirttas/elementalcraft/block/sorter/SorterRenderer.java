package sirttas.elementalcraft.block.sorter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import sirttas.elementalcraft.block.entity.renderer.IRuneRenderer;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class SorterRenderer implements IRuneRenderer<SorterBlockEntity> {


	@SuppressWarnings("resource")
	@Override
	public void render(@Nonnull SorterBlockEntity sorter, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		var mouseOver = Minecraft.getInstance().hitResult;
		var shiftKeyDown =  Minecraft.getInstance().player.isShiftKeyDown();
		var stacks = sorter.getStacks();

		if (mouseOver != null && mouseOver.getType() == HitResult.Type.BLOCK && !stacks.isEmpty()) {
			poseStack.pushPose();

			BlockHitResult result = (BlockHitResult) mouseOver;
			
			if (sorter.getBlockPos().equals(result.getBlockPos())) {
				var index = sorter.getIndex();
				var rotation = result.getDirection().getRotation();
				var newPos = new Vector3f(0, 2F / 16, 1F / 16);

				poseStack.translate(0.5, 0.5, 0.5);
				rotation.transform(newPos);
				poseStack.translate(newPos.x(), newPos.y(), newPos.z());
				poseStack.mulPose(rotation);
				poseStack.mulPose(Axis.XP.rotationDegrees(-90F));
				poseStack.scale(0.5F, 0.5F, 0.5F);
				ECRendererHelper.renderItem(stacks.get(index), poseStack, buffer, light, overlay);
				poseStack.scale(0.5F, 0.5F, 0.5F);
				poseStack.pushPose();
				translate(poseStack, 0.5, shiftKeyDown);
				for (int i = index - 1; i >= 0; i--) {
					translate(poseStack, 0.5, shiftKeyDown);
					ECRendererHelper.renderItem(stacks.get(i), poseStack, buffer, light, overlay);
				}
				poseStack.popPose();
				poseStack.pushPose();
				translate(poseStack, -0.5, shiftKeyDown);
				for (int i = index + 1; i < stacks.size(); i++) {
					translate(poseStack, -0.5, shiftKeyDown);
					ECRendererHelper.renderItem(stacks.get(i), poseStack, buffer, light, overlay);
				}
				poseStack.popPose();
			}
			poseStack.popPose();
		}
		renderRunes(sorter, partialTicks, poseStack, buffer, light, overlay);
	}

	private void renderRunes(SorterBlockEntity sorter, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		poseStack.translate(0.5F, 0.5F, 0.5F);

		var state = sorter.getBlockState();
		var rotation1 = state.getValue(ISorterBlock.SOURCE).getOpposite().getRotation();
		var rotation2 = state.getValue(ISorterBlock.TARGET).getRotation();
		var rotation = new Quaternionf(rotation1.x() + rotation2.x(), rotation1.y() + rotation2.y(), rotation1.z() + rotation2.z(), rotation1.w() + rotation2.w());

		rotation.normalize();
		poseStack.mulPose(rotation);

		poseStack.translate(-0.5F, -0.75F, -0.5F);
		ECRendererHelper.renderRunes(poseStack, buffer, sorter.getRuneHandler(), ECRendererHelper.getClientTicks(partialTicks), light, overlay);
	}

	private void translate(PoseStack matrixStack, double value, boolean sneeking) {
		if (sneeking) {
			matrixStack.translate(-value, 0, 0);
		} else {
			matrixStack.translate(0, value, 0);
		}
	}
}
