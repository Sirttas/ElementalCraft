package sirttas.elementalcraft.block.pipe;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import sirttas.elementalcraft.block.cover.CoverRenderState;
import sirttas.elementalcraft.block.pipe.section.ElementPipeSectionRenderState;

import java.util.ArrayList;
import java.util.List;

public class ElementPipeRenderState extends BlockEntityRenderState {
    public final List<ElementPipeSectionRenderState> sections = new ArrayList<>(6);
    public final CoverRenderState cover = new CoverRenderState();
}
