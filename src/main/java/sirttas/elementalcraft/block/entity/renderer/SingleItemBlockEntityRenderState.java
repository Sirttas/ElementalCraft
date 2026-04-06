package sirttas.elementalcraft.block.entity.renderer;

import net.minecraft.client.renderer.item.ItemStackRenderState;

public class SingleItemBlockEntityRenderState extends RuneBlockEntityRenderState {
    public float itemRotationAnimationTime;
    public final ItemStackRenderState item = new ItemStackRenderState();
}
