package sirttas.elementalcraft.block.entity.renderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import sirttas.elementalcraft.client.renderer.state.RunesRenderState;

public class RuneBlockEntityRenderState extends BlockEntityRenderState {
    public final RunesRenderState runes = new RunesRenderState();
}
