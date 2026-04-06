package sirttas.elementalcraft.block.source.breeder.pedestal;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import sirttas.elementalcraft.client.renderer.state.RunesRenderState;
import sirttas.elementalcraft.client.renderer.state.SourceRenderState;

public class SourceBreederPedestalRenderState extends BlockEntityRenderState {
    public final SourceRenderState source = new SourceRenderState();
    public final RunesRenderState runes = new RunesRenderState();
}
