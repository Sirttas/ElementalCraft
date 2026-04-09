package sirttas.elementalcraft.block.shrine;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import sirttas.elementalcraft.client.renderer.state.GhostBlockRenderState;
import sirttas.elementalcraft.client.renderer.state.RangeRenderState;

import java.util.ArrayList;
import java.util.List;

public class ShrineRenderState extends BlockEntityRenderState {
    public final List<GhostBlockRenderState> ghostUpgrades = new ArrayList<>(6);
    public final RangeRenderState range = new RangeRenderState();
}
