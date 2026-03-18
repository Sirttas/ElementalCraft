package sirttas.elementalcraft.block.shrine;

import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.Direction;
import sirttas.elementalcraft.renderer.state.GhostBlockRenderState;
import sirttas.elementalcraft.renderer.state.RangeRenderState;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class ShrineRenderState {

    private final RangeRenderState range;
    private final Map<Direction, GhostBlockRenderState> ghostUpgrades;

    protected ShrineRenderState(BlockRenderDispatcher blockRenderDispatcher) {
        this.range = new RangeRenderState();
        ghostUpgrades = new EnumMap<>(Direction.class);

        Arrays.stream(Direction.values()).forEach(direction -> ghostUpgrades.put(direction, new GhostBlockRenderState(blockRenderDispatcher)));
    }
}
