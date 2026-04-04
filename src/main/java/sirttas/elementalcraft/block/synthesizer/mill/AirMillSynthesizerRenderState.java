package sirttas.elementalcraft.block.synthesizer.mill;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import sirttas.elementalcraft.client.renderer.state.MillModelRenderState;
import sirttas.elementalcraft.client.renderer.state.RunesRenderState;

public class AirMillSynthesizerRenderState extends BlockEntityRenderState {
    public final RunesRenderState runes = new RunesRenderState();
    public final MillModelRenderState shaft = new MillModelRenderState();
}
