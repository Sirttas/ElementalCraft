package sirttas.elementalcraft.block.instrument.crystallizer;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import sirttas.elementalcraft.block.entity.renderer.ECBlockEntityRenderState;
import sirttas.elementalcraft.client.renderer.state.RunesRenderState;

public class CrystallizerRenderState extends ECBlockEntityRenderState {
    public final RunesRenderState runes = new RunesRenderState();
    public final ItemStackRenderState gem = new ItemStackRenderState();
    public final ItemStackRenderState crystal = new ItemStackRenderState();
}
