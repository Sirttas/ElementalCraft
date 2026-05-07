package sirttas.elementalcraft.client.renderer.state;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.client.renderer.ECRendererHelper;
import sirttas.elementalcraft.rune.RuneModelResolver;

import java.util.List;

public class RunesRenderState {

    private float animationTime;
    private List<Material.Baked> runes;

    public RunesRenderState() {
        animationTime = 0;
        runes = List.of();
    }

    public void update(@Nullable IRuneHandler runeHandler, RuneModelResolver runeModelResolver, float partialTicks) {
        this.animationTime = ECRendererHelper.getClientTicks(partialTicks) / 2;
        this.runes = runeHandler == null ? List.of() : runeHandler.getRunes().stream()
                .map(runeModelResolver::getSprite)
                .toList();
    }

    public void update(BlockEntity blockEntity, RuneModelResolver runeModelResolver, float partialTicks) {
        this.update(blockEntity.getLevel().getCapability(ElementalCraftCapabilities.RuneHandlers.BLOCK, blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, null), runeModelResolver, partialTicks);
    }

    public void clear() {
        this.animationTime = 0;
        this.runes.clear();
    }

    public void submit(@NotNull BlockEntityRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector) {
        this.submit(poseStack, nodeCollector, renderState.lightCoords);
    }

    public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, int lightCoords) {
        int runeCount = runes.size();

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.75F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(animationTime));
        for (var rune : runes) {
            poseStack.mulPose(Axis.YP.rotationDegrees(90F / runeCount));
            poseStack.pushPose();
            poseStack.translate(0.75F, 0F, 0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
            poseStack.scale(1F / 64F, 1F / 64F, 1F / 64F);
            ECRendererHelper.submitIcon(poseStack, nodeCollector, rune, 16, -16, lightCoords);
            poseStack.popPose();
        }
        poseStack.popPose();
    }
}
