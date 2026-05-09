package sirttas.elementalcraft.block.sorter.ordered;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.block.sorter.ISorterBlock;
import sirttas.elementalcraft.client.model.ECModelResolver;
import sirttas.elementalcraft.rune.RuneModelResolver;

public class OrderedSorterRenderer implements BlockEntityRenderer<OrderedSorterBlockEntity, OrderedSorterRenderState> {

    private final ItemModelResolver itemModelResolver;
    private final RuneModelResolver runeModelResolver;

    public OrderedSorterRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
        this.runeModelResolver = ECModelResolver.get(RuneModelResolver.IDENTIFIER);
    }

    @Override
    public OrderedSorterRenderState createRenderState() {
        return new OrderedSorterRenderState();
    }

    @Override
    public void extractRenderState(OrderedSorterBlockEntity blockEntity, OrderedSorterRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.runes.update(blockEntity, runeModelResolver, partialTicks);

        var blockState = blockEntity.getBlockState();
        var sourceRotation = blockState.getValue(ISorterBlock.SOURCE).getOpposite().getRotation();
        var targetRotation = blockState.getValue(ISorterBlock.TARGET).getRotation();
        state.runeRotation = new Quaternionf(sourceRotation.x() + targetRotation.x(), sourceRotation.y() + targetRotation.y(), sourceRotation.z() + targetRotation.z(), sourceRotation.w() + targetRotation.w());
        state.runeRotation.normalize();

        state.items.clear();

        var stacks = blockEntity.getStacks();
        if (stacks.isEmpty()) {
            return;
        }

        var mouseOver = Minecraft.getInstance().hitResult;
        if (mouseOver == null || mouseOver.getType() != HitResult.Type.BLOCK) {
            return;
        }

        var result = (BlockHitResult) mouseOver;
        if (!blockEntity.getBlockPos().equals(result.getBlockPos())) {
            return;
        }

        var rotation = result.getDirection().getRotation();
        var newPos = new Vector3f(0, 2F / 16, 1F / 16);

        rotation.transform(newPos);

        state.rotation = rotation;
        state.facePosition = newPos;
        state.useAlternativeDirection = Minecraft.getInstance().player.isShiftKeyDown();
        state.index = blockEntity.getIndex();

        for ( var stack : stacks) {
            if (!stack.isEmpty()) {
                var itemState = new ItemStackRenderState();

                itemModelResolver.updateForTopItem(itemState, stack, ItemDisplayContext.GROUND, blockEntity.getLevel(), null, 0);
                state.items.add(itemState);
            }
        }
    }

    @Override
    public void submit(OrderedSorterRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
		if (!state.items.isEmpty()) {
			poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.translate(state.facePosition.x(), state.facePosition.y(), state.facePosition.z());
            poseStack.mulPose(state.rotation);
            poseStack.mulPose(Axis.XP.rotationDegrees(-90F));
            poseStack.scale(0.5F, 0.5F, 0.5F);
            state.items.get(state.index).submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.pushPose();
            translate(state, poseStack, 0.5F);
            for (int i = state.index - 1; i >= 0; i--) {
                translate(state, poseStack, 0.5F);
                state.items.get(i).submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            }
            poseStack.popPose();
            poseStack.pushPose();
            translate(state, poseStack, -0.5F);
            for (int i = state.index + 1; i < state.items.size(); i++) {
                translate(state, poseStack, -0.5F);
                state.items.get(i).submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            }
            poseStack.popPose();
			poseStack.popPose();
		}
		submitRunes(state, poseStack, submitNodeCollector);
	}

	private void submitRunes(OrderedSorterRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
		poseStack.translate(0.5F, 0.5F, 0.5F);
		poseStack.mulPose(state.runeRotation);
		poseStack.translate(-0.5F, -0.75F, -0.5F);
        state.runes.submit(poseStack, submitNodeCollector, state.lightCoords);
	}

	private void translate(OrderedSorterRenderState state, PoseStack matrixStack, float amount) {
		if (state.useAlternativeDirection) {
			matrixStack.translate(-amount, 0, 0);
		} else {
			matrixStack.translate(0, amount, 0);
		}
	}
}
