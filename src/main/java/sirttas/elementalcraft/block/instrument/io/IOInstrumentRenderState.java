package sirttas.elementalcraft.block.instrument.io;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import sirttas.elementalcraft.renderer.state.RunesRenderState;

public class IOInstrumentRenderState extends BlockEntityRenderState {
    public float partialTick;
    public Direction facing;
    public final RunesRenderState runes = new RunesRenderState();
    public final ItemStackRenderState material = new ItemStackRenderState();
    public final ItemStackRenderState result = new ItemStackRenderState();
}
