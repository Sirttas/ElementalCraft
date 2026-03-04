package sirttas.elementalcraft.block.source.trait.holder;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import sirttas.dpanvil.api.data.DataManagerCodecs;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.holder.ISourceTraitHolder;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.block.source.trait.SourceTraits;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class ItemSourceTraitHolder implements ISourceTraitHolder {

    private static final Codec<Holder<SourceTrait>> HOLDER_CODEC = DataManagerCodecs.holderCodec(ElementalCraftApi.SOURCE_TRAIT_MANAGER_KEY, SourceTrait.CODEC, false);
    public static final Codec<ItemSourceTraitHolder> CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<ItemSourceTraitHolder, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getMap(input).setLifecycle(Lifecycle.stable()).flatMap(map -> {
                final Object2ObjectMap<Holder<SourceTrait>, ISourceTraitValue> read = new Object2ObjectArrayMap<>();
                final Stream.Builder<Pair<T, T>> failed = Stream.builder();

                final DataResult<Unit> result = map.entries().reduce(
                        DataResult.success(Unit.INSTANCE, Lifecycle.stable()),
                        (r, pair) -> {
                            final var key = HOLDER_CODEC.parse(ops, pair.getFirst());
                            final DataResult<Pair<Holder<SourceTrait>, ISourceTraitValue>> entryResult = key.flatMap(k -> k.value().valueCodec().parse(ops, pair.getSecond()).map(v -> Pair.of(k, v)));
                            final Optional<Pair<Holder<SourceTrait>, ISourceTraitValue>> entry = entryResult.resultOrPartial();

                            if (entry.isPresent()) {
                                final ISourceTraitValue existingValue = read.putIfAbsent(entry.get().getFirst(), entry.get().getSecond());

                                if (existingValue != null) {
                                    failed.add(pair);
                                    return r.apply2stable((u, p) -> u, DataResult.error(() -> "Duplicate entry for key: '" + entry.get().getFirst() + "'"));
                                }
                            }
                            if (entryResult.isError()) {
                                failed.add(pair);
                            }

                            return r.apply2stable((u, p) -> u, entryResult);
                        },
                        (r1, r2) -> r1.apply2stable((u1, u2) -> u1, r2)
                );

                final Map<Holder<SourceTrait>, ISourceTraitValue> elements = ImmutableMap.copyOf(read);
                final T errors = ops.createMap(failed.build());

                return result.map(unit -> elements).setPartial(elements).mapError(e -> e + " missed input: " + errors);
            }).map(result -> {
                var holder = new ItemSourceTraitHolder();

                holder.traits.putAll(result);
                return Pair.of(holder, input);
            });
        }

        @Override
        public <T> DataResult<T> encode(ItemSourceTraitHolder input, DynamicOps<T> ops, T prefix) {
            var builder = ops.mapBuilder();

            for (final Map.Entry<Holder<SourceTrait>, ISourceTraitValue> entry : input.traits.entrySet()) {
                builder.add(HOLDER_CODEC.encodeStart(ops, entry.getKey()), entry.getKey().value().valueCodec().encodeStart(ops, entry.getValue()));
            }
            return builder.build(prefix);
        }
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemSourceTraitHolder> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ItemSourceTraitHolder decode(@NotNull RegistryFriendlyByteBuf buf) {
            int i = ByteBufCodecs.readCount(buf, Integer.MAX_VALUE);
            ItemSourceTraitHolder holder = new ItemSourceTraitHolder();

            for (int j = 0; j < i; j++) {
                var k = ElementalCraftApi.SOURCE_TRAIT_MANAGER.getOrCreateHolder(Identifier.STREAM_CODEC.decode(buf));
                var v = k.value().valueStreamCodec().decode(buf);

                holder.traits.put(k, v);
            }
            return holder;
        }

        @Override
        public void encode(@NotNull RegistryFriendlyByteBuf buf, ItemSourceTraitHolder holder) {
            ByteBufCodecs.writeCount(buf, holder.traits.size(), Integer.MAX_VALUE);
            holder.traits.forEach((k, v) -> {
                Identifier.STREAM_CODEC.encode(buf, ElementalCraftApi.SOURCE_TRAIT_MANAGER.getId(k));
                k.value().valueStreamCodec().encode(buf, v);
            });
        }
    };

    public static final ItemSourceTraitHolder EMPTY = new ItemSourceTraitHolder();

    private final Map<Holder<SourceTrait>, ISourceTraitValue> traits;

    private ItemSourceTraitHolder() {
        traits = SourceTraits.createTraitMap();
    }

    public static ItemSourceTraitHolder from(ISourceTraitHolder traitHolder) {
        return from(traitHolder.getTraits());
    }

    public static ItemSourceTraitHolder from(Map<Holder<SourceTrait>, ISourceTraitValue> traits) {
        var holder = new ItemSourceTraitHolder();

        holder.traits.putAll(traits);
        return holder;
    }

    @Override
    public Map<Holder<SourceTrait>, ISourceTraitValue> getTraits() {
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
