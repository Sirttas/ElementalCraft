package sirttas.elementalcraft.spell.tick;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.Spells;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellTickManager implements ISpellTickManager, ValueIOSerializable {

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
    public void serialize(@NotNull ValueOutput output) {
        spellCooldowns.forEach((spell, cooldown) -> output.putLong(spell.getKey().toString(), cooldown.expireTicks() - tick));
    }

    @Override
    public void deserialize(@NotNull ValueInput input) {
        input.keySet().forEach(key -> Spells.REGISTRY.get(ElementalCraftApi.identifier(key)).ifPresent(spell -> spellCooldowns.put(spell.value(), new SpellCooldown(tick, tick + input.getLongOr(key, 0)))));
    }
}
