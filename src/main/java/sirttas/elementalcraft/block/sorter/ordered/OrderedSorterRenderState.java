package sirttas.elementalcraft.block.sorter.ordered;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import sirttas.elementalcraft.client.renderer.state.RunesRenderState;

import java.util.ArrayList;
import java.util.List;

public class OrderedSorterRenderState extends BlockEntityRenderState {
    public final RunesRenderState runes = new RunesRenderState();
    public Quaternionf runeRotation;

    public final List<ItemStackRenderState> items = new ArrayList<>();
    public int index;
    public boolean useAlternativeDirection;
    public Quaternionf rotation;
    public Vector3f facePosition;
}
