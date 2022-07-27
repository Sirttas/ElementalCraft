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
    public int getRecoverRate() {
        return super.getRecoverRate() + (source.isStabilized() ? 20 : 0);
    }

    @Override
    public float getTraits(SourceTrait.Type type) {
        return (float) getTraits().entrySet().stream()
                .mapToDouble(e -> {
                    var value = e.getValue().getValue(type);

                    if (SourceTraits.DIURNAL_NOCTURNAL.equals(e.getKey())) {
                        var l = source.getLevel();

                        if (l == null || value == 0) {
                            return 1;
                        }
                        return (l.isDay() ? value : l.isNight() ? 1 / value : 1);
                    }
                    return value;
                })
                .reduce(1, (a, b) -> a * b);
    }

}
