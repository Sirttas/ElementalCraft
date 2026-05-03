package sirttas.elementalcraft.block.instrument.inscriber;

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
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.client.model.ECModelResolver;
import sirttas.elementalcraft.client.renderer.ECRendererHelper;
import sirttas.elementalcraft.rune.RuneModelResolver;

public class InscriberRenderer implements BlockEntityRenderer<@NotNull InscriberBlockEntity, @NotNull InscriberRenderState> {

    private final ItemModelResolver itemModelResolver;
    private final RuneModelResolver runeModelResolver;

    public InscriberRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
        this.runeModelResolver = ECModelResolver.get(RuneModelResolver.IDENTIFIER);
    }

    @Override
    public InscriberRenderState createRenderState() {
        return new InscriberRenderState();
    }

    @Override
    public void extractRenderState(InscriberBlockEntity blockEntity, InscriberRenderState renderState, float partialTicks, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTicks, cameraPosition, breakProgress);
        renderState.partialTicks = partialTicks;
        renderState.facing = blockEntity.getBlockState().getValue(InscriberBlock.FACING);
        renderState.runes.update(blockEntity, runeModelResolver, partialTicks);
        renderState.items.clear();

        Container inv = blockEntity.getInventory();

        itemModelResolver.updateForTopItem(renderState.runeSlate, inv.getItem(0), ItemDisplayContext.GROUND, blockEntity.getLevel(), null, 0);
        for (int i = 1; i < inv.getContainerSize(); i++) {
            var stack = inv.getItem(i);

            if (!stack.isEmpty()) {
                var state = new ItemStackRenderState();

                itemModelResolver.updateForTopItem(state, stack, ItemDisplayContext.GROUND, blockEntity.getLevel(), null, 0);
                renderState.items.add(state);
            }
        }
    }

    @Override
    public void submit(InscriberRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, @NotNull CameraRenderState cameraRenderState) {
        poseStack.translate(0F, 0.25F, 0F);
        renderState.runes.submit(renderState, poseStack, nodeCollector);
        poseStack.translate(0.5F, 0.15F, 0.5F);
        poseStack.mulPose(ECRendererHelper.getRotation(renderState.facing));
        submitRuneSlate(renderState, poseStack, nodeCollector);
        submitItems(renderState, poseStack, nodeCollector);
    }

    private void submitRuneSlate(InscriberRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector) {
        if (!renderState.runeSlate.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0F, 0F, 0.0625F);
            poseStack.mulPose(Axis.XP.rotationDegrees(22.5F));
            renderState.runeSlate.submit(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }

    private void submitItems(InscriberRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector) {
        float tick = ECRendererHelper.getClientTicks(renderState.partialTicks);

        poseStack.translate(-0.4F, -0.2F, -0.2F);
        for (var state : renderState.items) {
            if (!state.isEmpty()) {
                poseStack.pushPose();
                poseStack.mulPose(Axis.YP.rotationDegrees(tick));
                poseStack.scale(0.5F, 0.5F, 0.5F);
                state.submit(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                poseStack.popPose();
                poseStack.translate(0.4F, 0F, 0);
            }
        }
    }
}
