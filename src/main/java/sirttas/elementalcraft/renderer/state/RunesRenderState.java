package sirttas.elementalcraft.renderer.state;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class RunesRenderState {

    private float partialTick;
    private List<Material.Baked> runes;

    public  RunesRenderState() {
        partialTick = 0;
        runes = List.of();
    }

    public void update(@Nullable IRuneHandler runeHandler, float partialTick) {
        this.partialTick = partialTick;
        this.runes = runeHandler == null ? List.of() : runeHandler.getRunes().stream()
                .map(r -> r.value().getSprite())
                .toList();
    }

    public void update(BlockEntity blockEntity, float partialTicks) {
        this.update(blockEntity.getLevel().getCapability(ElementalCraftCapabilities.RuneHandlers.BLOCK, blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, null), partialTicks);
    }

    public void clear() {
        this.runes.clear();
    }

    public void submit(@NotNull BlockEntityRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector) {
        this.submit(poseStack, nodeCollector, renderState.lightCoords);
    }

    public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, int lightCoords) {
        int runeCount = runes.size();

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.75F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(partialTick / 2));
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
