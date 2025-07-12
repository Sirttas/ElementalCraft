package sirttas.elementalcraft.api.source.trait.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.SourceTraitRollContext;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

public final class FixedSourceTraitValueProvider implements ISourceTraitValueProvider {

    public static final String NAME = "fixed";
    public static final MapCodec<FixedSourceTraitValueProvider> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.STRING.fieldOf(ECNames.NAME).forGetter(FixedSourceTraitValueProvider::getTranslationKey),
            SourceTrait.Type.VALUE_CODEC.fieldOf(ECNames.VALUES).forGetter(FixedSourceTraitValueProvider::getValues)
    ).apply(builder, FixedSourceTraitValueProvider::new));

    private final String translationKey;
    private final Map<SourceTrait.Type, Float> values;
	private final SourceTraitValue value;

    public FixedSourceTraitValueProvider(String translationKey, Map<SourceTrait.Type, Float> values) {
        this.translationKey = translationKey;
        this.values = values;
		this.value = new SourceTraitValue();
    }

    @Override
    public ISourceTraitValue roll(SourceTraitRollContext context, Level level, BlockPos pos) {
        return value;
    }

    @Override
    public ISourceTraitValue breed(SourceTraitRollContext context, @Nullable ISourceTraitValue value1, @Nullable ISourceTraitValue value2) {
        return value;
    }

    @Override
    public @NotNull SourceTraitValueProviderType<FixedSourceTraitValueProvider> getType() {
        return SourceTraitValueProviderTypes.FIXED.get();
    }

    @Override
    public ISourceTraitValue load(Tag tag) {
        return value;
    }

    @Override
    public Tag save(ISourceTraitValue value) {
        return ByteTag.ONE;
    }

    @Override
    public Codec<ISourceTraitValue> valueCodec() {
        return Codec.unit(value);
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ISourceTraitValue> valueStreamCodec() {
        return StreamCodec.unit(value);
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public Map<SourceTrait.Type, Float> getValues() {
        return values;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FixedSourceTraitValueProvider) obj;
        return Objects.equals(this.translationKey, that.translationKey) &&
                Objects.equals(this.values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(translationKey, values);
    }

    @Override
    public String toString() {
        return "FixedSourceTraitValueProvider[" +
                "translationKey=" + translationKey + ", " +
                "values=" + values + ']';
    }

    private class SourceTraitValue implements ISourceTraitValue {

        @Override
        public float getValue(SourceTrait.Type type) {
            return values.getOrDefault(type, 1f);
        }

        @Override
        public Component getDescription() {
            return Component.translatable(translationKey);
        }
    }
}
