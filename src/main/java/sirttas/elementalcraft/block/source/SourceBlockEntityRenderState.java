package sirttas.elementalcraft.block.source;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import sirttas.elementalcraft.client.renderer.state.SourceRenderState;

public class SourceBlockEntityRenderState extends BlockEntityRenderState {
    public final SourceRenderState source = new SourceRenderState();
    public boolean stabilized;
    public float animationTime;
    public int color;
}
