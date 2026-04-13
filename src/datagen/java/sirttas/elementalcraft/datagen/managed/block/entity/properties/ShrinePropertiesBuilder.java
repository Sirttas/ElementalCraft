package sirttas.elementalcraft.datagen.managed.block.entity.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.range.Range;
import sirttas.elementalcraft.api.range.RangeVariants;
import sirttas.elementalcraft.block.entity.properties.ConfigurableBlockEntityPropertiesType;

import java.util.Collections;
import java.util.List;
import java.util.stream.DoubleStream;

public class ShrinePropertiesBuilder implements IConfigurableBlockEntityPropertiesBuilder {

    public static final MapCodec<ShrinePropertiesBuilder> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ElementType.MAP_CODEC.forGetter(b -> b.elementType),
            Codec.DOUBLE.optionalFieldOf(ECNames.PERIOD, 0D).forGetter(b -> b.period),
            Codec.INT.optionalFieldOf(ECNames.ELEMENT_CONSUMPTION, 0).forGetter(b -> b.consumption),
            Codec.INT.optionalFieldOf(ECNames.ELEMENT_CAPACITY, 0).forGetter(b -> b.capacity),
            RangeVariants.Builder.CODEC.fieldOf(ECNames.RANGES).forGetter(b -> b.ranges),
            Codec.DOUBLE.listOf().optionalFieldOf(ECNames.STRENGTH, Collections.emptyList()).forGetter(b -> b.strength)
    ).apply(builder, (a1, a2, a3, a4, a5, a6) -> {
        throw new UnsupportedOperationException("Builder deserialization is not supported.");
    }));

    private double period;
    private int consumption;
    private int capacity;
    private List<Double> strength;
    private final ElementType elementType;
    private final RangeVariants.Builder ranges;

    ShrinePropertiesBuilder(ElementType elementType) {
        this.elementType = elementType;
        this.ranges = new RangeVariants.Builder();
        consumption = 0;
        period = 1;
        capacity = 10000;
        strength = Collections.emptyList();
    }

    public ShrinePropertiesBuilder consumption(int consumeAmount) {
        this.consumption = consumeAmount;
        return this;
    }

    public ShrinePropertiesBuilder period(double period) {
        if (period <= 0) {
            throw new IllegalArgumentException("Shrine period should be greater than 0");
        }
        this.period = period;
        return this;
    }

    public ShrinePropertiesBuilder capacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public ShrinePropertiesBuilder range(Range.Builder builder) {
        return range(RangeVariants.DEFAULT_KEY, builder);
    }

    public ShrinePropertiesBuilder range(ResourceKey<Range> key) {
        return range(RangeVariants.DEFAULT_KEY, key);
    }

    public ShrinePropertiesBuilder range(Direction direction, Range.Builder builder) {
        return range(direction.getSerializedName(), builder);
    }

    public ShrinePropertiesBuilder range(Direction direction, ResourceKey<Range> key) {
        return range(direction.getSerializedName(), key);
    }


    public ShrinePropertiesBuilder range(String name, Range.Builder builder) {
        this.ranges.put(name, builder);
        return this;
    }

    public ShrinePropertiesBuilder range(String name, ResourceKey<Range> key) {
        this.ranges.put(name, key.identifier());
        return this;
    }

    public ShrinePropertiesBuilder strength(double... strength) {
        this.strength = DoubleStream.of(strength).boxed().toList();
        return this;
    }

    @Override
    public ConfigurableBlockEntityPropertiesType<?> getType() {
        return ConfigurableBlockEntityPropertiesType.SHRINE.get();
    }
}
