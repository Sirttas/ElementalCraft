package sirttas.elementalcraft.spell.flamecleave;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import sirttas.elementalcraft.spell.renderer.SpellRenderState;

public class FlameCleaveSpellRenderState extends SpellRenderState {
    public final ItemStackRenderState weapon = new ItemStackRenderState();
    public float angle;
}
