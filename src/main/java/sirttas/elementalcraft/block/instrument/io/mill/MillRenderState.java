package sirttas.elementalcraft.block.instrument.io.mill;

import sirttas.elementalcraft.block.instrument.io.IOInstrumentRenderState;
import sirttas.elementalcraft.client.renderer.state.MillModelRenderState;

public class MillRenderState extends IOInstrumentRenderState {
    public final MillModelRenderState shaft = new MillModelRenderState();
    public float animationTime;
}
