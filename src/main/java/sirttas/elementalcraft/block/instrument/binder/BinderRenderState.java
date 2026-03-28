package sirttas.elementalcraft.block.instrument.binder;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import sirttas.elementalcraft.block.entity.renderer.ECBlockEntityRenderState;
import sirttas.elementalcraft.client.renderer.state.RunesRenderState;

import java.util.ArrayList;
import java.util.List;

public class BinderRenderState extends ECBlockEntityRenderState {
    public final RunesRenderState runes = new RunesRenderState();
    public final List<ItemStackRenderState> items = new ArrayList<>(BinderBlockEntity.MAX_INVENTORY_SIZE);
}
