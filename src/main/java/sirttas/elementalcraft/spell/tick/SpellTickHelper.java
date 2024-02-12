package sirttas.elementalcraft.spell.tick;

import net.minecraft.world.entity.Entity;
import sirttas.elementalcraft.spell.Spell;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class SpellTickHelper {

    private SpellTickHelper() {}

    @Nullable
    public static ISpellTickManager get(@Nullable Entity entity) {
        return entity != null ? entity.getCapability(ISpellTickManager.CAPABILITY) : null;
    }

    @Nonnull
    public static List<AbstractSpellInstance> getSpellInstances(@Nullable Entity entity) {
        var manager = get(entity);

        if (manager == null) {
            return Collections.emptyList();
        }

        return manager.getSpellInstances();
    }

    public static void startCooldown(@Nullable Entity entity, Spell spell) {
        var manager = get(entity);

        if (manager != null) {
            manager.startCooldown(spell);
        }
    }

    public static boolean hasCooldown(@Nullable Entity entity, Spell spell) {
        var manager = get(entity);

        return manager != null && manager.hasCooldown(spell);
    }


    public static float getCooldown(@Nullable Entity entity, Spell spell, float frameTime) {
        var manager = get(entity);

        return manager != null ? manager.getCooldown(spell, frameTime) : 0;
    }

    @Nullable
    public static AbstractSpellInstance getSpellInstance(@Nullable Entity entity, Spell spell) {
        var manager = get(entity);

        return manager != null ? manager.getSpellInstance(spell) : null;
    }
}
