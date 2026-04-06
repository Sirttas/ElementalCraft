package sirttas.elementalcraft.block.shrine;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import sirttas.elementalcraft.client.renderer.state.GhostBlockRenderState;
import sirttas.elementalcraft.client.renderer.state.RangeRenderState;

import java.util.EnumMap;
import java.util.Map;

public class ShrineRenderState extends BlockEntityRenderState {

    public final RangeRenderState range = new RangeRenderState();
    public final Map<Direction, GhostBlockRenderState> ghostUpgrades = new EnumMap<>(Direction.class);
}
