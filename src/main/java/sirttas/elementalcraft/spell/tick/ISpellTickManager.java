package sirttas.elementalcraft.spell.tick;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import sirttas.elementalcraft.spell.Spell;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@AutoRegisterCapability
public interface ISpellTickManager {

    Capability<ISpellTickManager> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    @Nullable
    default AbstractSpellInstance getSpellInstance(Spell spell) {
        return getSpellInstances().stream()
                .filter(spellInstance -> spellInstance.getSpell().equals(spell))
                .findFirst()
                .orElse(null);
    }

    @Nonnull
    List<AbstractSpellInstance> getSpellInstances();

    void addSpellInstance(AbstractSpellInstance instance);

    void startCooldown(Spell spell);

    default boolean hasCooldown(Spell spell) {
        return getCooldown(spell) > 0;
    }

    default float getCooldown(Spell spell) {
        return getCooldown(spell, 0);
    }

    float getCooldown(Spell spell, float partialTick);

    void tick();

}
