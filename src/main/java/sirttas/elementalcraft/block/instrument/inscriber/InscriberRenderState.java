package sirttas.elementalcraft.block.instrument.inscriber;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import sirttas.elementalcraft.renderer.state.RunesRenderState;

import java.util.ArrayList;
import java.util.List;

public class InscriberRenderState extends BlockEntityRenderState {
    public float partialTick;
    public Direction facing;
    public final RunesRenderState runes = new RunesRenderState();
    public final ItemStackRenderState runeSlate = new ItemStackRenderState();
    public final List<ItemStackRenderState> items = new ArrayList<>();
}

