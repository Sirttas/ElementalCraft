package sirttas.elementalcraft.api.source.trait;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import sirttas.dpanvil.api.data.DataManagerCodecs;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValueProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class SourceTrait {

	public static final Codec<SourceTrait> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Codec.INT.fieldOf(ECNames.ORDER).forGetter(SourceTrait::getOrder),
			ISourceTraitValueProvider.CODEC.fieldOf(ECNames.VALUE).forGetter(t -> t.valueProvider)
	).apply(builder, SourceTrait::new));
	public static final Codec<Holder<@NotNull SourceTrait>> HOLDER_CODEC = DataManagerCodecs.holderCodec(ElementalCraftApi.SOURCE_TRAIT_MANAGER_KEY, SourceTrait.CODEC, false);
    public static final Codec<Map<Holder<@NotNull SourceTrait>, ISourceTraitValue>> VALUE_MAP_CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<Map<Holder<@NotNull SourceTrait>, ISourceTraitValue>, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getMap(input).setLifecycle(Lifecycle.stable()).flatMap(map -> {
                final Object2ObjectMap<Holder<@NotNull SourceTrait>, ISourceTraitValue> read = new Object2ObjectArrayMap<>();
                final Stream.Builder<Pair<T, T>> failed = Stream.builder();

                final DataResult<Unit> result = map.entries().reduce(
                        DataResult.success(Unit.INSTANCE, Lifecycle.stable()),
                        (r, pair) -> {
                            final var key = HOLDER_CODEC.parse(ops, pair.getFirst());
                            final DataResult<Pair<Holder<@NotNull SourceTrait>, ISourceTraitValue>> entryResult = key.flatMap(k -> k.value().valueCodec().parse(ops, pair.getSecond()).map(v -> Pair.of(k, v)));
                            final Optional<Pair<Holder<@NotNull SourceTrait>, ISourceTraitValue>> entry = entryResult.resultOrPartial();

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

                final Map<Holder<@NotNull SourceTrait>, ISourceTraitValue> elements = ImmutableMap.copyOf(read);
                final T errors = ops.createMap(failed.build());

                return result.map(unit -> elements).setPartial(elements).mapError(e -> e + " missed input: " + errors);
            }).map(result -> {
                final Object2ObjectMap<Holder<@NotNull SourceTrait>, ISourceTraitValue> traits = new Object2ObjectArrayMap<>();

                traits.putAll(result);
                return Pair.of(traits, input);
            });
        }

        @Override
        public <T> DataResult<T> encode(Map<Holder<@NotNull SourceTrait>, ISourceTraitValue> input, DynamicOps<T> ops, T prefix) {
            var builder = ops.mapBuilder();

            for (final Map.Entry<Holder<@NotNull SourceTrait>, ISourceTraitValue> entry : input.entrySet()) {
                builder.add(HOLDER_CODEC.encodeStart(ops, entry.getKey()), entry.getKey().value().valueCodec().encodeStart(ops, entry.getValue()));
            }
            return builder.build(prefix);
        }
    };
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull Map<Holder<@NotNull SourceTrait>, ISourceTraitValue>> VALUE_MAP_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull Map<Holder<@NotNull SourceTrait>, ISourceTraitValue> decode(@NotNull RegistryFriendlyByteBuf buf) {
            int i = ByteBufCodecs.readCount(buf, Integer.MAX_VALUE);
            Map<Holder<@NotNull SourceTrait>, ISourceTraitValue> traits = new HashMap<Holder<@NotNull SourceTrait>, ISourceTraitValue>();

            for (int j = 0; j < i; j++) {
                var k = ElementalCraftApi.SOURCE_TRAIT_MANAGER.getOrCreateHolder(Identifier.STREAM_CODEC.decode(buf));
                var v = k.value().valueStreamCodec().decode(buf);

                traits.put(k, v);
            }
            return traits;
        }

        @Override
        public void encode(@NotNull RegistryFriendlyByteBuf buf, Map<Holder<@NotNull SourceTrait>, ISourceTraitValue> traits) {
            ByteBufCodecs.writeCount(buf, traits.size(), Integer.MAX_VALUE);
            traits.forEach((k, v) -> {
                Identifier.STREAM_CODEC.encode(buf, ElementalCraftApi.SOURCE_TRAIT_MANAGER.getId(k));
                k.value().valueStreamCodec().encode(buf, v);
            });
        }
    };
    
	private Identifier id;
	private final int order;
	private final ISourceTraitValueProvider valueProvider;
	
	public static Builder builder() {
		return new Builder();
	}
	
	public SourceTrait(int order, ISourceTraitValueProvider valueProvider) {
		this.order = order;
		this.valueProvider = valueProvider;
	}
	
	public Identifier getId() {
		return id;
	}

	public void setId(Identifier id) {
		this.id = id;
	}
	
	public int getOrder() {
		return order;
	}
	
	@Nullable
	public ISourceTraitValue roll(ServerLevelAccessor level, BlockPos pos, float luck) {
		return roll(level, level.getRandom(), pos, luck);
	}

	@Nullable
	public ISourceTraitValue roll(ServerLevelAccessor level, RandomSource random, BlockPos pos, float luck) {
		return valueProvider.roll(new SourceTraitRollContext(this, random, luck), level.getLevel(), pos);
	}


	@Nullable
	public ISourceTraitValue breed(RandomSource random, float luck, ISourceTraitValue value1, ISourceTraitValue value2) {
		return valueProvider.breed(new SourceTraitRollContext(this, random, luck),  value1, value2);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		} else if (other == this) {
			return true;
		}
		return other instanceof SourceTrait trait && this.id != null && this.id.equals(trait.id);
	}
	
	@Override
	public int hashCode() {
		return this.id != null ? this.id.hashCode() : 0;
	}

	public Codec<ISourceTraitValue> valueCodec() {
		return valueProvider.valueCodec();
	}

	public StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull ISourceTraitValue> valueStreamCodec() {
		return valueProvider.valueStreamCodec();
	}

	public enum Type implements StringRepresentable {
		NONE(ECNames.NONE),
		CAPACITY(ECNames.ELEMENT_CAPACITY),
		EXTRACTION_SPEED(ECNames.EXTRACTION_SPEED),
		PRESERVATION(ECNames.ELEMENT_PRESERVATION),
		BREEDING_COST(ECNames.BREEDING_COST);

		public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
		public static final Codec<Map<Type, Float>> VALUE_CODEC = valueCodec(Codec.FLOAT);

		public static <T> Codec<Map<Type, T>> valueCodec(Codec<T> valueCodec) {
			return Codec.unboundedMap(Type.CODEC, valueCodec);
		}

		private final String name;

		Type(String name) {
			this.name = name;
		}

		@Nonnull
		@Override
		public String getSerializedName() {
			return this.name;
		}
	}

	public static class Builder {

		private static final AtomicInteger ORDER_INCREMENT = new AtomicInteger(0);

		public static final Encoder<Builder> ENCODER = SourceTrait.CODEC.comap(builder -> new SourceTrait(builder.order, builder.valueProvider));

		private int order;
		private ISourceTraitValueProvider valueProvider;
		
		private Builder() {
			order = ORDER_INCREMENT.getAndIncrement();
		}
		
		public Builder order(int order) {
			this.order = order;
			if (order >= ORDER_INCREMENT.get()) {
				ORDER_INCREMENT.set(order + 1);
			}
			return this;
		}
		
		public Builder value(ISourceTraitValueProvider valueProvider) {
			this.valueProvider = valueProvider;
			return this;
		}
	}
}
