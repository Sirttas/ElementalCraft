package sirttas.elementalcraft.block.diffuser;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import org.joml.Quaternionf;
import sirttas.elementalcraft.client.renderer.state.RangeRenderState;
import sirttas.elementalcraft.client.renderer.state.RunesRenderState;

public class DiffuserRenderState extends BlockEntityRenderState {
    public final RunesRenderState runes = new RunesRenderState();
    public final RangeRenderState range = new RangeRenderState();
    public Quaternionf cubeRotation;
}
