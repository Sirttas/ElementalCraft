package sirttas.elementalcraft.block.shrine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.range.RangeVariants;
import sirttas.elementalcraft.block.entity.properties.ConfigurableBlockEntityPropertiesType;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;

import java.util.Collections;
import java.util.List;

public record ShrineProperties(
        ElementType elementType,
        double period,
        int consumption,
        int capacity,
        RangeVariants ranges,
        List<Double> strength
) implements IConfigurableBlockEntityProperties, IElementTypeProvider {

    public static final ShrineProperties DEFAULT = new ShrineProperties(ElementType.NONE, 1.0, 0, 1000000, RangeVariants.DEFAULT, Collections.emptyList());

    public static final MapCodec<ShrineProperties> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ElementType.forGetter(ShrineProperties::getElementType),
            Codec.DOUBLE.optionalFieldOf(ECNames.PERIOD, 0D).forGetter(ShrineProperties::period),
            Codec.INT.optionalFieldOf(ECNames.ELEMENT_CONSUMPTION, 0).forGetter(ShrineProperties::consumption),
            Codec.INT.optionalFieldOf(ECNames.ELEMENT_CAPACITY, 0).forGetter(ShrineProperties::capacity),
            RangeVariants.CODEC.optionalFieldOf(ECNames.RANGES, RangeVariants.DEFAULT).forGetter(ShrineProperties::ranges),
            Codec.DOUBLE.listOf().optionalFieldOf(ECNames.STRENGTH, Collections.emptyList()).forGetter(ShrineProperties::strength)
    ).apply(builder, ShrineProperties::new));

    @Override
    public @NotNull ElementType getElementType() {
        return elementType;
    }

    @Override
    public ConfigurableBlockEntityPropertiesType<ShrineProperties> getType() {
        return ConfigurableBlockEntityPropertiesType.SHRINE.get();
    }

}
