package sirttas.elementalcraft.client.renderer.state;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.client.renderer.ECRenderTypes;

public class GhostBlockRenderState {
    public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();

    private final BlockModelRenderState blockModelRenderState;
    private BlockPos pos;
    private BlockState blockState;
    private BlockGetter level;

    public GhostBlockRenderState() {
        this.pos = BlockPos.ZERO;
        this.blockState = Blocks.AIR.defaultBlockState();
        this.level = null;
        this.blockModelRenderState = new BlockModelRenderState();
    }

    public void update(BlockModelResolver resolver, BlockGetter level, BlockState blockState, BlockPos pos) {
        this.level = level;
        this.blockState = blockState;
        this.pos = pos;
        resolver.update(blockModelRenderState, blockState, BLOCK_DISPLAY_CONTEXT);
        blockModelRenderState.renderType = ECRenderTypes.GHOST;
    }

    public void clear() {
        this.blockState = Blocks.AIR.defaultBlockState();
        this.blockModelRenderState.clear();
    }

    public boolean isOccupied() {
        if (level == null) {
            return true;
        }
        return !level.getBlockState(pos).isAir();
    }

    public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, int lightCoords) {
        if (isOccupied() || blockState.isAir() || blockModelRenderState.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        blockModelRenderState.submit(poseStack, nodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
