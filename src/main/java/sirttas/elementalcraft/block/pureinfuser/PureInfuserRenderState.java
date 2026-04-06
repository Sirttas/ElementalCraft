package sirttas.elementalcraft.block.pureinfuser;

import sirttas.elementalcraft.block.entity.renderer.SingleItemBlockEntityRenderState;
import sirttas.elementalcraft.client.renderer.state.GhostBlockRenderState;

import java.util.ArrayList;
import java.util.List;

public class PureInfuserRenderState extends SingleItemBlockEntityRenderState {
    public final List<GhostBlockRenderState> ghostPedestals = new ArrayList<>(4);

}
