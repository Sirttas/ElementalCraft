package sirttas.elementalcraft.spell.tick;

import net.minecraft.core.Holder;
import net.neoforged.neoforge.capabilities.EntityCapability;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.spell.Spell;

import java.util.List;

public interface ISpellTickManager {

    EntityCapability<ISpellTickManager, Void> CAPABILITY = EntityCapability.createVoid(ElementalCraftApi.identifier("spell_tick_manager"), ISpellTickManager.class);

    @Nullable
    default SpellInstance getSpellInstance(Spell spell) {
        return getSpellInstances().stream()
                .filter(spellInstance -> spellInstance.getSpell().equals(spell))
                .findFirst()
                .orElse(null);
    }

    List<SpellInstance> getSpellInstances();

    void addSpellInstance(SpellInstance instance);

    void startCooldown(Holder<Spell> spell);

    default boolean hasCooldown(Holder<Spell> spell) {
        return getCooldown(spell) > 0;
    }

    default float getCooldown(Holder<Spell> spell) {
        return getCooldown(spell, 0);
    }

    float getCooldown(Holder<Spell> spell, float partialTick);

    void tick();

}
