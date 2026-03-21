package sirttas.elementalcraft.block.instrument.crystallizer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import sirttas.elementalcraft.renderer.state.RunesRenderState;

public class CrystallizerRenderState extends BlockEntityRenderState {
    public float partialTick;
    public final RunesRenderState runes = new RunesRenderState();
    public final ItemStackRenderState gem = new ItemStackRenderState();
    public final ItemStackRenderState crystal = new ItemStackRenderState();
}
