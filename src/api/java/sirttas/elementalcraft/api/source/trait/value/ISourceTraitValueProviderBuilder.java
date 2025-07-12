package sirttas.elementalcraft.api.source.trait.value;

import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;

@FunctionalInterface
public interface ISourceTraitValueProviderBuilder {

    default ISourceTraitValueProviderBuilder chance(float chance) {
        return chance(chance, 0.25F);
    }

    default ISourceTraitValueProviderBuilder chance(float chance, float luckRatio) {
        return () -> new ChanceSourceTraitValueProvider(this.build(), chance, -1F, luckRatio, -1F);
    }


    default ISourceTraitValueProviderBuilder chance(float chance, float chanceOnBred, float luckRatio, float luckRatioOnBred) {
        return () -> new ChanceSourceTraitValueProvider(this.build(), chance, chanceOnBred, luckRatio, luckRatioOnBred);
    }

    default ISourceTraitValueProviderBuilder predicate(IBlockPosPredicate predicate) {
        return () -> new PredicateSourceTraitValueProvider(this.build(), predicate);
    }

    ISourceTraitValueProvider build();

}
