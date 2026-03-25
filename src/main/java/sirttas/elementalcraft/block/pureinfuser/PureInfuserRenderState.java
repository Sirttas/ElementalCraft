package sirttas.elementalcraft.block.pureinfuser;

import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import sirttas.elementalcraft.block.entity.renderer.SingleItemBlockEntityRenderState;
import sirttas.elementalcraft.renderer.state.GhostBlockRenderState;

public class PureInfuserRenderState extends SingleItemBlockEntityRenderState {
    public final GhostBlockRenderState ghostBlockRenderState;

    public PureInfuserRenderState(BlockRenderDispatcher blockRenderDispatcher) {
        this.ghostBlockRenderState = new GhostBlockRenderState(blockRenderDispatcher);
    }
}
