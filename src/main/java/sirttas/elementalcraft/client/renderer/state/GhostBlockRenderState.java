package sirttas.elementalcraft.client.renderer.state;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import sirttas.elementalcraft.client.renderer.ECRenderTypes;

public class GhostBlockRenderState {
    public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();

    private final BlockModelRenderState blockModelRenderState;
    private BlockState blockState;
    private Vector3f offset;

    public GhostBlockRenderState() {
        this.blockState = Blocks.AIR.defaultBlockState();
        this.blockModelRenderState = new BlockModelRenderState();
        this.offset = new Vector3f();
    }

    public void update(BlockModelResolver resolver, BlockState blockState, Vector3f offset) {
        this.blockState = blockState;
        resolver.update(blockModelRenderState, blockState, BLOCK_DISPLAY_CONTEXT);
        blockModelRenderState.renderType = ECRenderTypes.GHOST;
    }

    public void clear() {
        this.blockState = Blocks.AIR.defaultBlockState();
        this.blockModelRenderState.clear();
    }

    public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, int lightCoords) {
        if (blockState.isAir() || blockModelRenderState.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(offset.x(), offset.y(), offset.z());
        blockModelRenderState.submit(poseStack, nodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
