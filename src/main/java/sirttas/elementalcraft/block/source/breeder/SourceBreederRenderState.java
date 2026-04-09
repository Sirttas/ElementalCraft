package sirttas.elementalcraft.block.source.breeder;

import sirttas.elementalcraft.block.entity.renderer.SingleItemBlockEntityRenderState;
import sirttas.elementalcraft.client.renderer.state.GhostBlockRenderState;
import sirttas.elementalcraft.client.renderer.state.SourceRenderState;

import java.util.ArrayList;
import java.util.List;

public class SourceBreederRenderState extends SingleItemBlockEntityRenderState {
    public final List<GhostBlockRenderState> ghostPedestals = new ArrayList<>(4);
    public final SourceRenderState source = new SourceRenderState();
}
