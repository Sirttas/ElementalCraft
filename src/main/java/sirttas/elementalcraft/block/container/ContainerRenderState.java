package sirttas.elementalcraft.block.container;

import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import sirttas.elementalcraft.renderer.state.GhostBlockRenderState;

public class ContainerRenderState extends BlockEntityRenderState {
    public final GhostBlockRenderState ghostBlockRenderState;

    public ContainerRenderState(BlockRenderDispatcher blockRenderDispatcher) {
        this.ghostBlockRenderState = new GhostBlockRenderState(blockRenderDispatcher);
    }
}
