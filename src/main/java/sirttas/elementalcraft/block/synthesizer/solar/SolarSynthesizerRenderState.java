package sirttas.elementalcraft.block.synthesizer.solar;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import org.joml.Quaternionf;
import sirttas.elementalcraft.client.renderer.state.RunesRenderState;

public class SolarSynthesizerRenderState extends BlockEntityRenderState {
    public final RunesRenderState runes = new RunesRenderState();
    public Quaternionf lensRotation;
    public boolean running;
}
