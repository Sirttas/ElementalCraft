package sirttas.elementalcraft.datagen.managed.block.entity.properties;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.range.Range;
import sirttas.elementalcraft.block.entity.properties.ConfigurableBlockEntityPropertiesType;

import java.util.Optional;

public class SynthesizerPropertiesBuilder implements IConfigurableBlockEntityPropertiesBuilder {

    public static final MapCodec<SynthesizerPropertiesBuilder> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ElementType.forGetter(b -> b.elementType),
            Codec.INT.fieldOf(ECNames.TRANSFER_SPEED).forGetter(b -> b.transferSpeed),
            Codec.INT.fieldOf(ECNames.ELEMENT_CAPACITY).forGetter(b -> b.bufferCapacity),
            Codec.INT.fieldOf(ECNames.MAX_RUNES).forGetter(b -> b.maxRunes),
            Codec.FLOAT.optionalFieldOf("synthesis_multiplier", 1F).forGetter(b -> b.synthesisMultiplier),
            Codec.either(ResourceLocation.CODEC, Range.Builder.CODEC).optionalFieldOf(ECNames.RANGE).forGetter(b -> Optional.ofNullable(b.range))
    ).apply(builder, (a1, a2, a3, a4, a5, a6) -> {
        throw new UnsupportedOperationException("Builder deserialization is not supported.");
    }));

    private ElementType elementType;
    private int transferSpeed;
    private int bufferCapacity;
    private int maxRunes;
    private float synthesisMultiplier;
    private Either<ResourceLocation, Range.Builder> range;

    public SynthesizerPropertiesBuilder() {
        this.elementType = ElementType.NONE;
        this.transferSpeed = 0;
        this.bufferCapacity = 0;
        this.maxRunes = 0;
        this.synthesisMultiplier = 1;
        this.range = null;
    }

    @Override
    public ConfigurableBlockEntityPropertiesType<?> getType() {
        return ConfigurableBlockEntityPropertiesType.SYNTHESIZER.get();
    }

    public SynthesizerPropertiesBuilder elementType(ElementType elementType) {
        this.elementType = elementType;
        return this;
    }

    public SynthesizerPropertiesBuilder transferSpeed(int transferSpeed) {
        this.transferSpeed = transferSpeed;
        return this;
    }

    public SynthesizerPropertiesBuilder bufferCapacity(int bufferCapacity) {
        this.bufferCapacity = bufferCapacity;
        return this;
    }

    public SynthesizerPropertiesBuilder maxRunes(int maxRunes) {
        this.maxRunes = maxRunes;
        return this;
    }

    public SynthesizerPropertiesBuilder synthesisMultiplier(float synthesisMultiplier) {
        this.synthesisMultiplier = synthesisMultiplier;
        return this;
    }

    public SynthesizerPropertiesBuilder range(ResourceKey<Range> key) {
        this.range = Either.left(key.location());
        return this;
    }

    public SynthesizerPropertiesBuilder range(Range.Builder range) {
        this.range = Either.right(range);
        return this;
    }
}
