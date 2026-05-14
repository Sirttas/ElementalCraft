package sirttas.elementalcraft.spell.tick;

import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.Spells;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellTickManager implements ISpellTickManager, ValueIOSerializable {

    private long tick;
    private final List<SpellInstance> spellInstances;
    private final Map<Holder<Spell>, SpellCooldown> spellCooldowns;

    public SpellTickManager() {
        spellInstances = new ArrayList<>();
        spellCooldowns = new HashMap<>();
    }

    @Override
    public List<SpellInstance> getSpellInstances() {
        return List.copyOf(spellInstances);
    }

    @Override
    public void addSpellInstance(SpellInstance instance) {
        spellInstances.add(instance);
    }

    @Override
    public void startCooldown(Holder<Spell> spell) {
        spellCooldowns.put(spell, new SpellCooldown(tick, tick + spell.value().getCooldown()));
    }

    @Override
    public float getCooldown(Holder<Spell> spell, float partialTick) {
        if (spell.value().isValid() && spellCooldowns.containsKey(spell)) {
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
        spellInstances.removeIf(SpellInstance::isFinished);
        spellInstances.forEach(i -> {
            i.tick();
            i.decTick();
        });
        spellInstances.removeIf(SpellInstance::isFinished);
    }

    @Override
    public void serialize(ValueOutput output) {
        spellCooldowns.forEach((spell, cooldown) -> output.putLong(spell.getKey().toString(), cooldown.expireTicks() - tick));
    }

    @Override
    public void deserialize(ValueInput input) {
        input.keySet().forEach(key -> Spells.REGISTRY.get(ElementalCraftApi.identifier(key)).ifPresent(spell -> spellCooldowns.put(spell, new SpellCooldown(tick, tick + input.getLongOr(key, 0)))));
    }
}
