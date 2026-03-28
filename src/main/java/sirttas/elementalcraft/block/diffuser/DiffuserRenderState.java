package sirttas.elementalcraft.block.diffuser;

import sirttas.elementalcraft.block.entity.renderer.ECBlockEntityRenderState;
import sirttas.elementalcraft.client.renderer.state.RangeRenderState;
import sirttas.elementalcraft.client.renderer.state.RunesRenderState;

public class DiffuserRenderState extends ECBlockEntityRenderState {
    public final RunesRenderState runes = new RunesRenderState();
    public final RangeRenderState range = new RangeRenderState();
}
