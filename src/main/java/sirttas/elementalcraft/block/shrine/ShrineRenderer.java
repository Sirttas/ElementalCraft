package sirttas.elementalcraft.block.shrine;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.renderer.state.GhostBlockRenderState;
import sirttas.elementalcraft.tag.ECTags;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ShrineRenderer<T extends AbstractShrineBlockEntity> implements BlockEntityRenderer<@NotNull T, @NotNull ShrineRenderState> {

    private final BlockModelResolver blockModelResolver;

    public ShrineRenderer(BlockEntityRendererProvider.Context context) {
        this.blockModelResolver = context.blockModelResolver();
    }

    @Override
    public ShrineRenderState createRenderState() {
        return new ShrineRenderState();
    }


    @Override
    public void extractRenderState(T blockEntity, ShrineRenderState renderState, float partialTick, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        if (blockEntity.showsRange()) {
            renderState.range.update(blockEntity, blockEntity.getRange(), ARGB.colorFromFloat(1, 1, 1, 0.6F));
        } else {
            renderState.range.clear();
        }
        if (!ECConfig.CLIENT.renderInstrumentShadow.get()) {
            renderState.ghostUpgrades.values().forEach(GhostBlockRenderState::clear);
            return;
        }

        var player = Minecraft.getInstance().player;
        var level = blockEntity.getLevel();

        if (level == null || player == null) {
            return;
        }

        var pos = blockEntity.getBlockPos();

        for (var stack : List.of(player.getMainHandItem(), player.getOffhandItem())) {
            var upgrade = stack.getCapability(ElementalCraftCapabilities.ShrineUpgrades.ITEM);

            if (upgrade == null || !(stack.getItem() instanceof BlockItem blockItem) || !stack.is(ECTags.Items.SHRINE_UPGRADES)) {
                continue;
            }

            var block = blockItem.getBlock();

            for (var direction : blockEntity.getUpgradeDirections()) {
                var upgradePos = pos.relative(direction);

                if (!level.getBlockState(upgradePos).isAir() || !blockEntity.canReceiveUpgrade(direction, upgrade)) {
                    continue;
                }

                var state = block.getStateForPlacement(new DirectionalPlaceContext(level, upgradePos, direction.getOpposite(), stack, direction));

                if (state == null || !state.canSurvive(level, upgradePos)) {
                    continue;
                }
                renderState.ghostUpgrades.get(direction).update(blockModelResolver, level, state, upgradePos);
                return;
            }
        }
    }

    @Override
    public void submit(ShrineRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, @NotNull CameraRenderState cameraRenderState) {
        renderState.range.submit();

        poseStack.pushPose();
        renderState.ghostUpgrades.forEach((direction, ghostState) -> {
            poseStack.translate(direction.getStepX(), direction.getStepY(), direction.getStepZ());
            ghostState.submit(poseStack, nodeCollector, renderState.lightCoords);
        });
        poseStack.popPose();
	}
}
