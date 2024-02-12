package sirttas.elementalcraft.spell.tick;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.Spells;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellTickManager implements ISpellTickManager, INBTSerializable<CompoundTag> {

    private long tick;
    private final List<AbstractSpellInstance> spellInstances;
    private final Map<Spell, SpellCooldown> spellCooldowns;

    public SpellTickManager() {
        spellInstances = new ArrayList<>();
        spellCooldowns = new HashMap<>();
    }

    @Override
    @Nonnull
    public List<AbstractSpellInstance> getSpellInstances() {
        return List.copyOf(spellInstances);
    }

    @Override
    public void addSpellInstance(AbstractSpellInstance instance) {
        spellInstances.add(instance);
    }

    @Override
    public void startCooldown(Spell spell) {
        spellCooldowns.put(spell, new SpellCooldown(tick, tick + spell.getCooldown()));
    }

    @Override
    public float getCooldown(Spell spell, float partialTick) {
        if (spell.isValid() && spellCooldowns.containsKey(spell)) {
            var cooldown = spellCooldowns.get(spell);
            var current = cooldown.expireTicks() - (tick + partialTick);
            var total = cooldown.expireTicks() - cooldown.createTicks();

            return current / total;
        }
        return 0;
    }

    @Override
    public void tick() {
        tick++;
        spellCooldowns.values().removeIf(c -> c.expireTicks() <= tick);
        if (spellCooldowns.isEmpty()) {
            tick = 0;
        }
        spellInstances.removeIf(AbstractSpellInstance::isFinished);
        spellInstances.forEach(i -> {
            i.tick();
            i.decTick();
        });
        spellInstances.removeIf(AbstractSpellInstance::isFinished);
    }

    @Override
    public @NotNull CompoundTag serializeNBT() {
        var tag = new CompoundTag();

        spellCooldowns.forEach((spell, cooldown) -> tag.putLong(spell.getKey().toString(), cooldown.expireTicks() - tick));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        spellCooldowns.clear();
        nbt.getAllKeys().forEach(key -> spellCooldowns.put(Spells.REGISTRY.get(ElementalCraftApi.createRL(key)), new SpellCooldown(tick, tick + nbt.getLong(key))));
    }
}
