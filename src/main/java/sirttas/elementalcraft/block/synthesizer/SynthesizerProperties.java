package sirttas.elementalcraft.block.synthesizer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.range.Range;
import sirttas.elementalcraft.block.entity.properties.ConfigurableBlockEntityPropertiesType;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;

public record SynthesizerProperties(
        ElementType elementType,
        int transferSpeed,
        int bufferCapacity,
        int maxRunes,
        float synthesisMultiplier,
        Holder<Range> range
) implements IConfigurableBlockEntityProperties {

    public static final SynthesizerProperties DEFAULT = new SynthesizerProperties(ElementType.NONE, 0, 0, 0, 1, Holder.direct(Range.DEFAULT));
    public static final MapCodec<SynthesizerProperties> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ElementType.forGetter(SynthesizerProperties::elementType),
            Codec.INT.fieldOf(ECNames.TRANSFER_SPEED).forGetter(SynthesizerProperties::transferSpeed),
            Codec.INT.fieldOf(ECNames.ELEMENT_CAPACITY).forGetter(SynthesizerProperties::bufferCapacity),
            Codec.INT.fieldOf(ECNames.MAX_RUNES).forGetter(SynthesizerProperties::maxRunes),
            Codec.FLOAT.optionalFieldOf("synthesis_multiplier", 1F).forGetter(SynthesizerProperties::synthesisMultiplier),
            Range.HOLDER_CODEC.optionalFieldOf(ECNames.RANGE, Holder.direct(Range.DEFAULT)).forGetter(SynthesizerProperties::range)
    ).apply(builder, SynthesizerProperties::new));

    @Override
    public ConfigurableBlockEntityPropertiesType<?> getType() {
        return ConfigurableBlockEntityPropertiesType.SYNTHESIZER.get();
    }
}
