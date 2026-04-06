package sirttas.elementalcraft.block.source.breeder;

import net.minecraft.core.Direction;
import sirttas.elementalcraft.block.entity.renderer.SingleItemBlockEntityRenderState;
import sirttas.elementalcraft.client.renderer.state.GhostBlockRenderState;
import sirttas.elementalcraft.client.renderer.state.SourceRenderState;

import java.util.EnumMap;
import java.util.Map;

public class SourceBreederRenderState extends SingleItemBlockEntityRenderState {
    public final Map<Direction, GhostBlockRenderState> ghostPedestals = new EnumMap<>(Direction.class);
    public final SourceRenderState source = new SourceRenderState();
}
