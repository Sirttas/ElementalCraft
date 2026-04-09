package sirttas.elementalcraft.block.synthesizer.vibration;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import sirttas.elementalcraft.client.renderer.state.RangeRenderState;
import sirttas.elementalcraft.client.renderer.state.RunesRenderState;

public class VibrationSynthesizerRenderState extends BlockEntityRenderState {
    public final RangeRenderState range = new RangeRenderState();
    public final RunesRenderState runes = new RunesRenderState();
}
