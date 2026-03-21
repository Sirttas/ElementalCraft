package sirttas.elementalcraft.block.instrument.binder;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import sirttas.elementalcraft.renderer.state.RunesRenderState;

import java.util.ArrayList;
import java.util.List;

public class BinderRenderState extends BlockEntityRenderState {
    public float partialTick;
    public final RunesRenderState runes = new RunesRenderState();
    public final List<ItemStackRenderState> items = new ArrayList<>(BinderBlockEntity.MAX_INVENTORY_SIZE);
}
