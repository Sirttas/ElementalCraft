package sirttas.elementalcraft.block.container;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.block.container.reservoir.ReservoirBlockEntity;
import sirttas.elementalcraft.config.ECConfig;

import java.util.List;

public class ContainerRenderer<T extends AbstractElementContainerBlockEntity> implements BlockEntityRenderer<@NotNull T, @NotNull ContainerRenderState> {

    @Override
    public ContainerRenderState createRenderState() {
        return new ContainerRenderState(Minecraft.getInstance().getBlockRenderer()); // TODO use BlockEntityRendererProvider.Context
    }

    @Override
    public void extractRenderState(T container, ContainerRenderState renderState, float partialTick, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(container, renderState, partialTick, cameraPosition, breakProgress);

        if (!ECConfig.CLIENT.renderInstrumentShadow.get()) {
            renderState.ghostBlockRenderState.clear();
            return;
        }

        var player = Minecraft.getInstance().player;
        var level = container.getLevel();

        if (level == null || player == null) {
            return;
        }

        var instrumentPos = container.getBlockPos().above(container instanceof ReservoirBlockEntity ? 2 : 1);
        var itemsInHands = List.of(player.getMainHandItem(), player.getOffhandItem());

        for (var stack : itemsInHands) {
            if (stack.getItem() instanceof BlockItem blockItem) {
                var block = blockItem.getBlock();

                if (level.getBlockState(instrumentPos).isAir()) {
                    var state = block.getStateForPlacement(new DirectionalPlaceContext(level, instrumentPos, Direction.DOWN, stack, Direction.UP));

                    if (state != null && state.canSurvive(level, instrumentPos) && state.is(container.getCompatibleTools())) {
                        renderState.ghostBlockRenderState.update(level, state, instrumentPos, level.getRandom());
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void submit(ContainerRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, @NotNull CameraRenderState cameraRenderState) {
        renderState.ghostBlockRenderState.submit(poseStack, nodeCollector);
    }
}
