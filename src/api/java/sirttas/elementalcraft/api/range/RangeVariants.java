package sirttas.elementalcraft.api.range;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public record RangeVariants(Map<String, Holder<Range>> delegate) implements Map<String, Holder<Range>> {

    public static final Codec<RangeVariants> CODEC = Codec.unboundedMap(Codec.STRING, Range.HOLDER_CODEC).xmap(RangeVariants::new, RangeVariants::delegate);
    public static final String DEFAULT_KEY = "default";
    public static final String TRANSLOCATION_KEY = "translocation";
    public static final RangeVariants DEFAULT = new RangeVariants(Map.of(DEFAULT_KEY, Range.DEFAULT_HOLDER));

    public RangeVariants(Map<String, Holder<Range>> delegate) {
        this.delegate = Map.copyOf(delegate);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public Holder<Range> get(Object key) {
        if (key instanceof Direction direction) {
            return delegate.get(direction.getSerializedName());
        }
        return delegate.get(key);
    }

    @Nullable
    @Override
    public Holder<Range> put(String key, Holder<Range> value) {
        throw uoe();
    }

    @Override
    public Holder<Range> remove(Object key) {
        throw uoe();
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ? extends Holder<Range>> m) {
        throw uoe();
    }

    @Override
    public void clear() {
        throw uoe();
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return delegate.keySet();
    }

    @NotNull
    @Override
    public Collection<Holder<Range>> values() {
        return delegate.values();
    }

    @NotNull
    @Override
    public Set<Entry<String, Holder<Range>>> entrySet() {
        return delegate.entrySet();
    }

    private static UnsupportedOperationException uoe() {
        return new UnsupportedOperationException();
    }

    public static class Builder {

        public static final Codec<Builder> CODEC = Codec.unboundedMap(Codec.STRING, Codec.either(Identifier.CODEC, Range.Builder.CODEC)).xmap(Builder::new, b -> b.ranges);

        private final Map<String, Either<Identifier, Range.Builder>> ranges;

        public Builder(Map<String, Either<Identifier, Range.Builder>> ranges) {
            this.ranges = new HashMap<>(ranges);
        }

        public Builder() {
            this.ranges = new HashMap<>();
        }

        public Builder put(String name, Range.Builder builder) {
            ranges.put(name, Either.right(builder));
            return this;
        }

        public Builder put(String name, Identifier loc) {
            ranges.put(name, Either.left(loc));
            return this;
        }

    }
}
