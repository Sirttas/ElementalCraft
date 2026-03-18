package sirttas.elementalcraft.block.entity.renderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import sirttas.elementalcraft.renderer.state.RunesRenderState;

public class SingleItemRenderState extends BlockEntityRenderState {
    public float partialTick;
    public final RunesRenderState runes = new RunesRenderState();
    public final ItemStackRenderState item = new ItemStackRenderState();
}
