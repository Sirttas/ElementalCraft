package sirttas.elementalcraft.spell.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.Spells;
import sirttas.elementalcraft.spell.airshield.AirShieldSpellRenderer;
import sirttas.elementalcraft.spell.flamecleave.FlameCleaveSpellRenderer;

import java.util.HashMap;
import java.util.Map;

public class SpellRenderers {

    private static final Map<ResourceLocation, ISpellRenderer> SPELL_RENDERERS = new HashMap<>();

    static {
        register(Spells.FLAME_CLEAVE, new FlameCleaveSpellRenderer());
        register(Spells.AIR_SHIELD, new AirShieldSpellRenderer());
    }

    private SpellRenderers() {}

    public static ISpellRenderer get(Spell spell) {
        return SPELL_RENDERERS.get(spell.getRegistryName());
    }

    public static void register(RegistryObject<? extends Spell> spell, ISpellRenderer renderer) {
        SPELL_RENDERERS.put(spell.getId(), renderer);
    }
}
