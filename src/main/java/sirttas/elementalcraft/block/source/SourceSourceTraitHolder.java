package sirttas.elementalcraft.block.source;

import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.block.source.trait.SourceTraits;
import sirttas.elementalcraft.block.source.trait.holder.SourceTraitHolder;

import javax.annotation.Nonnull;

public class SourceSourceTraitHolder extends SourceTraitHolder {

    public final SourceBlockEntity source;

    public SourceSourceTraitHolder(@Nonnull SourceBlockEntity source) {
        this.source = source;
    }

    @Override
    public float getPreservationModifier() {
        return super.getPreservationModifier() * (source.isStabilized() ? 1.05F : 1F); // TODO config
    }

    @Override
    public float getTraits(SourceTrait.Type type) {
        return (float) getTraits().entrySet().stream()
                .mapToDouble(e -> {
                    var value = e.getValue().getValue(type);

                    if (e.getKey().is(SourceTraits.DIURNAL_NOCTURNAL_KEY)) {
                        var level = source.getLevel();

                        if (level == null || value == 0 || value == 1) {
                            return 1;
                        } else if (level.isDay()) {
                            return value;
                        } else if (level.isNight()) {
                            return 1 / value;
                        }
                        return 1;
                    }
                    return value;
                })
                .reduce(1, (a, b) -> a * b);
    }

}
