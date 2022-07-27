package sirttas.elementalcraft.api.source.trait.holder;

import net.minecraft.resources.ResourceKey;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;

import java.util.Map;

public interface ISourceTraitHolder {

    Map<ResourceKey<SourceTrait>, ISourceTraitValue> getTraits();
    void setTraits(Map<ResourceKey<SourceTrait>, ISourceTraitValue> traits);
    boolean isFleeting();

    default int getRecoverRate() {
        return Math.round(getTraits(SourceTrait.Type.RECOVER_RATE));
    }

    default int getCapacity() {
        return Math.round(getTraits(SourceTrait.Type.CAPACITY));
    }

    default float getSpeedModifier() {
        return getTraits(SourceTrait.Type.EXTRACTION_SPEED);
    }

    default float getPreservationModifier() {
        return getTraits(SourceTrait.Type.PRESERVATION);
    }

    default float getTraits(SourceTrait.Type type) {
        return (float) getTraits().values().stream()
                .mapToDouble(traitValue -> traitValue.getValue(type))
                .reduce(1, (a, b) -> a * b);

    }

    default boolean isEmpty() {
        return getTraits().isEmpty();
    }
}
