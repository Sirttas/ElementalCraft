package sirttas.elementalcraft.block.shrine;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import sirttas.elementalcraft.renderer.state.GhostBlockRenderState;
import sirttas.elementalcraft.renderer.state.RangeRenderState;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class ShrineRenderState extends BlockEntityRenderState {

    public final RangeRenderState range;
    public final Map<Direction, GhostBlockRenderState> ghostUpgrades;

    protected ShrineRenderState() {
        this.range = new RangeRenderState();
        ghostUpgrades = new EnumMap<>(Direction.class);

        Arrays.stream(Direction.values()).forEach(direction -> ghostUpgrades.put(direction, new GhostBlockRenderState()));
    }
}
