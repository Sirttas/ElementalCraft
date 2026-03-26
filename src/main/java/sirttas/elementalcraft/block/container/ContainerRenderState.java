package sirttas.elementalcraft.block.container;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import sirttas.elementalcraft.renderer.state.GhostBlockRenderState;

public class ContainerRenderState extends BlockEntityRenderState {
    public final GhostBlockRenderState ghostBlockRenderState = new GhostBlockRenderState();

}
