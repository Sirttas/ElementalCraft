package sirttas.elementalcraft.block.diffuser;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import sirttas.elementalcraft.renderer.state.RangeRenderState;
import sirttas.elementalcraft.renderer.state.RunesRenderState;

public class DiffuserRenderState extends BlockEntityRenderState {
    public float partialTick;
    public final RunesRenderState runes = new RunesRenderState();
    public final RangeRenderState range = new RangeRenderState();
}
