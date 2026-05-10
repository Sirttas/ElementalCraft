package sirttas.elementalcraft.spell.renderer;

import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import sirttas.elementalcraft.spell.Spell;

public class SpellRenderState {
    public Holder<Spell> spell;
    public int lightCoords;
    public InteractionHand hand;

    public static void extractBase(SpellRenderState state, Holder<Spell> spell, InteractionHand hand, int lightCoords) {
        state.spell = spell;
        state.hand = hand;
        state.lightCoords = lightCoords;
    }

}
