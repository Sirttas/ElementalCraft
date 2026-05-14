package sirttas.elementalcraft.spell.tick;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.spell.Spell;
import java.util.Collections;
import java.util.List;

public class SpellTickHelper {

    private SpellTickHelper() {}

    @Nullable
    public static ISpellTickManager get(@Nullable Entity entity) {
        return entity != null ? entity.getCapability(ISpellTickManager.CAPABILITY) : null;
    }

    public static List<SpellInstance> getSpellInstances(@Nullable Entity entity) {
        var manager = get(entity);

        if (manager == null) {
            return Collections.emptyList();
        }

        return manager.getSpellInstances();
    }

    public static void startCooldown(@Nullable Entity entity, Holder<Spell> spell) {
        var manager = get(entity);

        if (manager != null) {
            manager.startCooldown(spell);
        }
    }

    public static boolean hasCooldown(@Nullable Entity entity, Holder<Spell> spell) {
        var manager = get(entity);

        return manager != null && manager.hasCooldown(spell);
    }


    public static float getCooldown(@Nullable Entity entity, Holder<Spell> spell, float frameTime) {
        var manager = get(entity);

        return manager != null ? manager.getCooldown(spell, frameTime) : 0;
    }

    @Nullable
    public static SpellInstance getSpellInstance(@Nullable Entity entity, Spell spell) {
        var manager = get(entity);

        return manager != null ? manager.getSpellInstance(spell) : null;
    }
}
