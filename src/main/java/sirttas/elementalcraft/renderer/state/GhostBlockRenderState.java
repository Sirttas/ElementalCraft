package sirttas.elementalcraft.renderer.state;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.renderer.ECRenderTypes;

import java.util.List;

public class GhostBlockRenderState {

    private final BlockRenderDispatcher blockRenderer;
    private final List<BlockModelPart> parts;
    private BlockPos pos;
    private BlockState blockState;
    private BlockAndTintGetter level;

    public GhostBlockRenderState(BlockRenderDispatcher blockRenderer) {
        this.blockRenderer = blockRenderer;
        this.pos = BlockPos.ZERO;
        this.blockState = Blocks.AIR.defaultBlockState();
        this.level = null;
        this.parts  = new ObjectArrayList<>();
    }

    public void update(BlockAndTintGetter level, BlockState blockState, BlockPos pos, RandomSource randomSource) {
        this.level = level;
        this.blockState = blockState;
        this.pos = pos;

        this.parts.clear();
        blockRenderer.getBlockModel(blockState).collectParts(level, pos, blockState, randomSource, parts);
    }

    public void clear() {
        this.blockState = Blocks.AIR.defaultBlockState();
        this.parts.clear();
    }

    public boolean isOccupied() {
        if (level == null) {
            return true;
        }
        return !level.getBlockState(pos).isAir();
    }

    public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector) {
        if (isOccupied() || blockState.isAir() || parts.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        nodeCollector.submitCustomGeometry(poseStack, ECRenderTypes.GHOST, (pose, consumer) -> blockRenderer.renderBatched(blockState, pos, level, poseStack, t -> consumer, false, parts));
        poseStack.popPose();
    }
}
