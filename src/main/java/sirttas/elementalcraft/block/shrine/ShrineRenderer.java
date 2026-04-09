package sirttas.elementalcraft.block.shrine;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.client.renderer.state.GhostBlockRenderState;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.tag.ECTags;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class ShrineRenderer<T extends AbstractShrineBlockEntity, S extends ShrineRenderState> implements BlockEntityRenderer<@NotNull T, @NotNull S> {

    private final BlockModelResolver blockModelResolver;

    protected ShrineRenderer(BlockEntityRendererProvider.Context context) {
        this.blockModelResolver = context.blockModelResolver();
    }

    public static <T extends AbstractShrineBlockEntity> ShrineRenderer<T, ShrineRenderState> create(BlockEntityRendererProvider.Context context) {
        return new Default<>(context);
    }

    @Override
    public void extractRenderState(T blockEntity, S renderState, float partialTick, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        if (blockEntity.showsRange()) {
            renderState.range.update(blockEntity, blockEntity.getRange());
        } else {
            renderState.range.clear();
        }
        if (!ECConfig.CLIENT.renderInstrumentShadow.get()) {
            renderState.ghostUpgrades.clear();
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
                var ghostState = new GhostBlockRenderState();

                ghostState.update(blockModelResolver, state, direction.step());
                renderState.ghostUpgrades.add(ghostState);
                return;
            }
        }
    }

    @Override
    public void submit(S renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, @NotNull CameraRenderState cameraRenderState) {
        renderState.range.submit();
        renderState.ghostUpgrades.forEach(ghostState-> ghostState.submit(poseStack, nodeCollector, renderState.lightCoords));
	}

    private static class Default<T extends AbstractShrineBlockEntity> extends ShrineRenderer<T , ShrineRenderState> {

        protected Default(BlockEntityRendererProvider.Context context) {
            super(context);
        }

        @Override
        public @NotNull ShrineRenderState createRenderState() {
            return new ShrineRenderState();
        }
    }
}
