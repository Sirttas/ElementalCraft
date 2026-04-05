package sirttas.elementalcraft.block.pipe.upgrade.beam;

import sirttas.elementalcraft.block.pipe.upgrade.render.PipeUpgradeRenderState;
import sirttas.elementalcraft.client.renderer.state.RunesRenderState;

public class ElementBeamPipeUpgradeRenderState extends PipeUpgradeRenderState {
    public float animationTime;
    public boolean linked;
    public final RunesRenderState runes = new RunesRenderState();
}

