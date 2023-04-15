package sirttas.elementalcraft.api.source.trait;

import net.minecraft.util.RandomSource;

public record SourceTraitRollContext(
        SourceTrait trait,
        RandomSource random,
        float luck
) {
}
