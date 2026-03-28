package sirttas.elementalcraft.block.pureinfuser;

import sirttas.elementalcraft.block.entity.renderer.SingleItemBlockEntityRenderState;
import sirttas.elementalcraft.client.renderer.state.GhostBlockRenderState;

public class PureInfuserRenderState extends SingleItemBlockEntityRenderState {
    public final GhostBlockRenderState ghostBlockRenderState;

    public PureInfuserRenderState() {
        this.ghostBlockRenderState = new GhostBlockRenderState();
    }
}
