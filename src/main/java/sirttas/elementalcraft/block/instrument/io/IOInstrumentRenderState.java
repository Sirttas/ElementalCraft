package sirttas.elementalcraft.block.instrument.io;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import sirttas.elementalcraft.block.entity.renderer.ECBlockEntityRenderState;
import sirttas.elementalcraft.client.renderer.state.RunesRenderState;

public class IOInstrumentRenderState extends ECBlockEntityRenderState {
    public Direction facing;
    public final RunesRenderState runes = new RunesRenderState();
    public final ItemStackRenderState material = new ItemStackRenderState();
    public final ItemStackRenderState result = new ItemStackRenderState();
}
