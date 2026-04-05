package sirttas.elementalcraft.api.source.trait.holder;

import net.minecraft.core.Holder;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;

import java.util.Map;

public interface ISourceTraitHolder {

    Map<Holder<@NotNull SourceTrait>, ISourceTraitValue> getTraits();

    default int getCapacity() {
        return Math.round(getTraitValue(SourceTrait.Type.CAPACITY));
    }

    default float getSpeedModifier() {
        return getTraitValue(SourceTrait.Type.EXTRACTION_SPEED);
    }

    default float getPreservationModifier() {
        return getTraitValue(SourceTrait.Type.PRESERVATION);
    }

    default float getBreedingCost() {
        return getTraitValue(SourceTrait.Type.BREEDING_COST);
    }

    default float getTraitValue(SourceTrait.Type type) {
        return (float) getTraits().values().stream()
                .mapToDouble(traitValue -> traitValue.getValue(type))
                .reduce(1, (a, b) -> a * b);
    }

    default boolean isEmpty() {
        return getTraits().isEmpty();
    }
}
