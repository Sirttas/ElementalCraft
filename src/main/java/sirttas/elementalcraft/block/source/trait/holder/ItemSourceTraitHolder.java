package sirttas.elementalcraft.block.source.trait.holder;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.holder.ISourceTraitHolder;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.block.source.trait.SourceTraits;

import java.util.Map;
import java.util.Objects;

public class ItemSourceTraitHolder implements ISourceTraitHolder {

    public static final Codec<ItemSourceTraitHolder> CODEC = SourceTrait.VALUE_MAP_CODEC.xmap(ItemSourceTraitHolder::from, ItemSourceTraitHolder::getTraits);
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull ItemSourceTraitHolder> STREAM_CODEC = SourceTrait.VALUE_MAP_STREAM_CODEC.map(ItemSourceTraitHolder::from, ItemSourceTraitHolder::getTraits);
    public static final ItemSourceTraitHolder EMPTY = new ItemSourceTraitHolder();

    private final Map<Holder<@NotNull SourceTrait>, ISourceTraitValue> traits;

    private ItemSourceTraitHolder() {
        traits = SourceTraits.createTraitMap();
    }

    public static ItemSourceTraitHolder from(ISourceTraitHolder traitHolder) {
        return from(traitHolder.getTraits());
    }

    public static ItemSourceTraitHolder from(Map<Holder<@NotNull SourceTrait>, ISourceTraitValue> traits) {
        var holder = new ItemSourceTraitHolder();

        holder.traits.putAll(traits);
        return holder;
    }

    @Override
    public Map<Holder<@NotNull SourceTrait>, ISourceTraitValue> getTraits() {
        return Map.copyOf(traits);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return Objects.equals(traits, ((ItemSourceTraitHolder) o).traits);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(traits);
    }
}
