package sirttas.elementalcraft.block.synthesizer.cracking;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import sirttas.elementalcraft.client.renderer.state.RangeRenderState;
import sirttas.elementalcraft.client.renderer.state.RunesRenderState;

public class CrackingSynthesizerRenderState extends BlockEntityRenderState {
    public RangeRenderState range = new RangeRenderState();
    public final RunesRenderState runes = new RunesRenderState();
    public float animationTime;
}
