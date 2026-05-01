package sirttas.elementalcraft.block.source.breeder;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.BooleanUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.entity.renderer.SingleItemBlockEntityRenderer;
import sirttas.elementalcraft.client.renderer.state.GhostBlockRenderState;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;
import sirttas.elementalcraft.tag.ECTags;

public class SourceBreederRenderer extends SingleItemBlockEntityRenderer<SourceBreederBlockEntity, SourceBreederRenderState> {

    private final BlockModelResolver blockModelResolver;
    private final BlockState pedestalState;

    public SourceBreederRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new Vec3(0.5, 0, 0.5));
        pedestalState = ECBlocks.SOURCE_BREEDER_PEDESTAL.get().defaultBlockState();
        blockModelResolver = context.blockModelResolver();
    }

    @Override
    public @NotNull SourceBreederRenderState createRenderState() {
        return new SourceBreederRenderState();
    }

    @Override
    public void extractRenderState(SourceBreederBlockEntity blockEntity, SourceBreederRenderState renderState, float partialTick, @NotNull Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.ghostPedestals.clear();
        if (BooleanUtils.isTrue(ECConfig.CLIENT.renderPedestalShadow.get())) {
            var directions = blockEntity.getPedestalsDirections();

            if (directions.size() < 2) {
                for (var direction : Direction.Plane.HORIZONTAL) {
                    if (directions.contains(direction)) {
                        continue;
                    }
                    var ghostState = new GhostBlockRenderState();

                    ghostState.update(blockModelResolver, pedestalState, direction.step().mul(2));
                    renderState.ghostPedestals.add(ghostState);
                }
            }
        }

        var stack = blockEntity.getInventory().getItem(0);

        if (stack.is(ECTags.Items.FULL_RECEPTACLES)) {
            renderState.source.update(1, ReceptacleHelper.getElementType(stack), partialTick);
        }
    }

    @Override
    protected ItemStack getItemStack(SourceBreederBlockEntity blockEntity) {
        var stack = blockEntity.getInventory().getItem(0);

        if (stack.is(ECTags.Items.FULL_RECEPTACLES)) {
            return ItemStack.EMPTY;
        }
        return stack;
    }

    @Override
    public void submit(SourceBreederRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, @NotNull CameraRenderState cameraRenderState) {
        poseStack.translate(0, 1, 0);
        super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
        poseStack.translate(0, -1, 0);
        renderState.ghostPedestals.forEach(ghostState -> ghostState.submit(poseStack, nodeCollector, renderState.lightCoords));
        renderState.source.submit(poseStack, nodeCollector, cameraRenderState, renderState.lightCoords);
    }
}
