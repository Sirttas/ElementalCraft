package sirttas.elementalcraft.spell.tick;

import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.spell.Spell;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class SpellTickHelper {

    private SpellTickHelper() {}

    @Nonnull
    public static LazyOptional<ISpellTickManager> get(ICapabilityProvider provider) {
        return ISpellTickManager.CAPABILITY != null && provider != null ? provider.getCapability(ISpellTickManager.CAPABILITY, null) : LazyOptional.empty();
    }

    @Nonnull
    public static List<AbstractSpellInstance> getSpellInstances(ICapabilityProvider provider) {
        return get(provider).map(ISpellTickManager::getSpellInstances).orElse(Collections.emptyList());
    }

    public static void startCooldown(ICapabilityProvider provider, Spell spell) {
        get(provider).ifPresent(m -> m.startCooldown(spell));
    }

    public static boolean hasCooldown(ICapabilityProvider provider, Spell spell) {
        return get(provider)
                .map(m -> m.hasCooldown(spell))
                .orElse(false);
    }


    public static float getCooldown(ICapabilityProvider provider, Spell spell, float frameTime) {
        return get(provider)
                .map(m -> m.getCooldown(spell, frameTime))
                .orElse(0f);
    }

    @Nullable
    public static AbstractSpellInstance getSpellInstance(ICapabilityProvider provider, Spell spell) {
        return get(provider)
                .map(m -> m.getSpellInstance(spell))
                .orElse(null);
    }
}
