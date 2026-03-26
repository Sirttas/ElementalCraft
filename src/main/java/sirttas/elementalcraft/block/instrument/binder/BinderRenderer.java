package sirttas.elementalcraft.block.instrument.binder;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.renderer.ECRendererHelper;

@OnlyIn(Dist.CLIENT)
public class BinderRenderer<T extends BinderBlockEntity> implements BlockEntityRenderer<@NotNull T, @NotNull BinderRenderState> {

    private final ItemModelResolver itemModelResolver;

    public BinderRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public BinderRenderState createRenderState() {
        return new BinderRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, BinderRenderState renderState, float partialTick, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.partialTick = partialTick;
        renderState.runes.update(blockEntity, partialTick);
        renderState.items.clear();

        Container inv = blockEntity.getInventory();
        for (int i = 0; i < blockEntity.getItemCount(); i++) {
            var stack = inv.getItem(i);

            if (stack.isEmpty()) {
                var state = new ItemStackRenderState();

                itemModelResolver.updateForTopItem(state, stack, ItemDisplayContext.GROUND, blockEntity.getLevel(), null, 0);
                renderState.items.add(state);
            }
        }
    }

    @Override
    public void submit(BinderRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, @NotNull CameraRenderState cameraRenderState) {
        var tick = ECRendererHelper.getClientTicks(renderState.partialTick);
        var size = renderState.items.size();

        renderState.runes.submit(renderState, poseStack, nodeCollector);
        poseStack.translate(0.5F, 0.4F, 0.5F);
        if (size == 1) {
            poseStack.mulPose(Axis.YP.rotationDegrees(tick));
            renderState.items.getFirst().submit(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        } else {
            poseStack.scale(0.5F, 0.5F, 0.5F);
            for (var itemStackRenderState : renderState.items) {
                if (!itemStackRenderState.isEmpty()) {
                    poseStack.mulPose(Axis.YP.rotationDegrees(360F / size));
                    poseStack.pushPose();
                    poseStack.translate(0.7F, 0F, 0F);
                    poseStack.mulPose(Axis.YP.rotationDegrees(tick));
                    itemStackRenderState.submit(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                    poseStack.popPose();
                }
            }
        }
    }
}
