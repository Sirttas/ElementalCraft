package sirttas.elementalcraft.element;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import sirttas.elementalcraft.api.element.ElementType;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class ElementAmounts {

    public static final ElementAmounts EMPTY = new ElementAmounts(Map.of());

    public static final Codec<ElementAmounts> CODEC = Codec.unboundedMap(ElementType.CODEC, Codec.INT).xmap(ElementAmounts::new, e -> e.amounts);
    public static final StreamCodec<RegistryFriendlyByteBuf, ElementAmounts> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(s -> new EnumMap<>(ElementType.class), ElementType.STREAM_CODEC, ByteBufCodecs.VAR_INT),
            e -> e.amounts,
            ElementAmounts::new
    );

    private final EnumMap<ElementType, Integer> amounts;

    private ElementAmounts(Map<ElementType, Integer> amounts) {
        this.amounts = new EnumMap<>(ElementType.class);
        this.amounts.putAll(amounts);
    }

    public int get(ElementType type) {
        return amounts.getOrDefault(type, 0);
    }

    public ElementAmounts with(ElementType type, int newCount) {
        var map = new EnumMap<>(amounts);

        map.put(type, newCount);
        return new ElementAmounts(map);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return Objects.equals(amounts, ((ElementAmounts) o).amounts);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amounts);
    }
}
