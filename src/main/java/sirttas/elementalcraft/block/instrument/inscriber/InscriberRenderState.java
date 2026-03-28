package sirttas.elementalcraft.block.instrument.inscriber;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import sirttas.elementalcraft.block.entity.renderer.ECBlockEntityRenderState;
import sirttas.elementalcraft.client.renderer.state.RunesRenderState;

import java.util.ArrayList;
import java.util.List;

public class InscriberRenderState extends ECBlockEntityRenderState {
    public Direction facing;
    public final RunesRenderState runes = new RunesRenderState();
    public final ItemStackRenderState runeSlate = new ItemStackRenderState();
    public final List<ItemStackRenderState> items = new ArrayList<>();
}

