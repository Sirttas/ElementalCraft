package sirttas.elementalcraft.block.synthesizer.vibration;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import sirttas.elementalcraft.renderer.state.RangeRenderState;
import sirttas.elementalcraft.renderer.state.RunesRenderState;

public class VibrationSynthesizerRenderState extends BlockEntityRenderState {
    public RangeRenderState range = new RangeRenderState();
    public RunesRenderState runes = new RunesRenderState();
}
