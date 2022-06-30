package sirttas.elementalcraft.block.shrine.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;

import java.util.Collections;
import java.util.List;
import java.util.stream.DoubleStream;

public record ShrineProperties(
        ElementType elementType,
        double period,
        int consumption,
        int capacity,
        float range,
        List<Double> strength
) implements IElementTypeProvider {

    public static final String NAME = "shrine_properties";
    public static final String FOLDER = ElementalCraftApi.MODID + '/' + NAME;
    public static final ShrineProperties DEFAULT = new ShrineProperties(ElementType.NONE, 1.0, 0, 1000000, 1, Collections.emptyList());

    public static final Codec<ShrineProperties> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ElementType.forGetter(ShrineProperties::getElementType),
            Codec.DOUBLE.optionalFieldOf(ECNames.PERIODE, 0D).forGetter(ShrineProperties::period),
            Codec.INT.optionalFieldOf(ECNames.ELEMENT_CONSUMPTION, 0).forGetter(ShrineProperties::consumption),
            Codec.INT.optionalFieldOf(ECNames.ELEMENT_CAPACITY, 0).forGetter(ShrineProperties::capacity),
            Codec.FLOAT.optionalFieldOf(ECNames.RANGE, 0F).forGetter(ShrineProperties::range),
            Codec.DOUBLE.listOf().optionalFieldOf(ECNames.STRENGTH, Collections.emptyList()).forGetter(ShrineProperties::strength)
    ).apply(builder, ShrineProperties::new));

    public static ShrineProperties.Builder builder(ElementType elementType) {
        return new ShrineProperties.Builder(elementType);
    }

    public static ShrineProperties.Builder builder(AbstractShrineBlock<?> block) {
        return builder(block.getElementType());
    }


    @Override
    public ElementType getElementType() {
        return elementType;
    }

    public static class Builder {

        public static final Encoder<ShrineProperties.Builder> ENCODER = ShrineProperties.CODEC.comap(builder -> new ShrineProperties(builder.elementType, builder.period, builder.consumption, builder.capacity, builder.range, builder.strength));

        private double period;
        private int consumption;
        private int capacity;
        private float range;

        private List<Double> strength;
        private final ElementType elementType;

        private Builder(ElementType elementType) {
            this.elementType = elementType;
            consumption = 0;
            period = 1;
            range = 1;
            capacity = 10000;
            strength = Collections.emptyList();
        }

        public ShrineProperties.Builder consumption(int consumeAmount) {
            this.consumption = consumeAmount;
            return this;
        }

        public ShrineProperties.Builder period(double period) {
            if (period <= 0) {
                throw new IllegalArgumentException("Shrine period should be greater than 0");
            }
            this.period = period;
            return this;
        }

        public ShrineProperties.Builder capacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public ShrineProperties.Builder range(float range) {
            this.range = range;
            return this;
        }

        public ShrineProperties.Builder strength(double... strength) {
            this.strength = DoubleStream.of(strength).boxed().toList();
            return this;
        }
    }
}
