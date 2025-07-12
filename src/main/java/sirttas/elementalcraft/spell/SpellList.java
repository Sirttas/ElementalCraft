package sirttas.elementalcraft.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Map;
import java.util.Objects;
import java.util.function.ObjIntConsumer;

public class SpellList {

    public static final SpellList EMPTY = new SpellList(Map.of(), 0);

    public static final Codec<SpellList> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(Spells.REGISTRY.holderByNameCodec(), Codec.INT).fieldOf("spells").forGetter(l -> l.spells),
            Codec.INT.fieldOf("max_spells").forGetter(l -> l.maxSpells)
    ).apply(instance, SpellList::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpellList> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(Object2IntOpenHashMap::new, ByteBufCodecs.holderRegistry(Spells.REGISTRY_KEY), ByteBufCodecs.VAR_INT),
            l -> l.spells,
            ByteBufCodecs.VAR_INT,
            l -> l.maxSpells,
            SpellList::new
    );

    private final Object2IntOpenHashMap<Holder<Spell>> spells;
    private final int maxSpells;

    private SpellList(Map<Holder<Spell>, Integer> spells, int maxSpells) {
        this.spells = new Object2IntOpenHashMap<>(spells);
        this.maxSpells = maxSpells;
    }

    public void forEachSpell(ObjIntConsumer<Holder<Spell>> consumer) {
        spells.forEach(consumer::accept);
    }

    public Mutable mutable() {
        return new Mutable(this);
    }

    public boolean isEmpty() {
        return spells.isEmpty();
    }

    public boolean isFull() {
        return count() >= maxSpells;
    }

    public int count() {
        return spells.values().intStream().sum();
    }

    public Map<Holder<Spell>, Integer> getSpells() {
        return Map.copyOf(spells);
    }

    public int getIndex(Holder<Spell> spell) {
        var index = 0;

        for (Holder<Spell> spellHolder : spells.keySet()) {
            if (spellHolder == spell) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public Holder<Spell> getSpellAt(int i) {
        var index = 0;

        for (Holder<Spell> spell : spells.keySet()) {
            if (index == i) {
                return spell;
            }
            index++;
        }
        return Spells.NONE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var spellList = (SpellList) o;

        return maxSpells == spellList.maxSpells && Objects.equals(spells, spellList.spells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spells, maxSpells);
    }

    public static class Mutable {
        private final Object2IntOpenHashMap<Holder<Spell>> spells;
        private int maxSpells;

        private Mutable(SpellList list) {
            this.spells = new Object2IntOpenHashMap<>(list.spells);
            this.maxSpells = list.maxSpells;
        }

        public Mutable maxSpells(int maxSpells) {
            this.maxSpells = maxSpells;
            return this;
        }

        public Mutable add(Holder<Spell> spell) {
            spells.compute(spell, (s, i) -> i == null ? 1 : i + 1);
            return this;
        }

        public Mutable remove(Holder<Spell> spell) {
            spells.compute(spell, (s, i) -> i == null ? 0 : i - 1);
            if (spells.getInt(spell) == 0) {
                spells.removeInt(spell);
            }
            return this;
        }

        public Mutable removeAll(Holder<Spell> spell) {
            spells.removeInt(spell);
            return this;
        }

        public Mutable clear() {
            spells.clear();
            return this;
        }

        public SpellList immutable() {
            return new SpellList(spells, maxSpells);
        }
    }

}
