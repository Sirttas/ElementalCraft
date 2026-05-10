package sirttas.elementalcraft.spell.renderer;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.Spells;
import sirttas.elementalcraft.spell.airshield.AirShieldSpellRenderer;
import sirttas.elementalcraft.spell.flamecleave.FlameCleaveSpellRenderer;
import sirttas.elementalcraft.spell.repair.RepairSpellRenderer;

import java.util.HashMap;
import java.util.Map;

public class SpellRenderers {

    private static final Map<Identifier, SpellRenderer<?>> SPELL_RENDERERS = new HashMap<>(); // TODO add provider

    static {
        register(Spells.FLAME_CLEAVE, new FlameCleaveSpellRenderer());
        register(Spells.AIR_SHIELD, new AirShieldSpellRenderer());
        register(Spells.REPAIR, new RepairSpellRenderer());
    }

    private SpellRenderers() {}

    @SuppressWarnings("unchecked")
    public static <S extends SpellRenderState> SpellRenderer<S> get(Holder<Spell> spell) {
        return (SpellRenderer<S>) SPELL_RENDERERS.get(spell.getKey());
    }

    public static void register(DeferredHolder<Spell, ? extends Spell> spell, SpellRenderer<?> renderer) {
        SPELL_RENDERERS.put(spell.getId(), renderer);
    }
}
