package sirttas.elementalcraft.block.source.trait.value;

import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValueProvider;

@FunctionalInterface
public interface ISourceTraitValueProviderBuilder {

    default ISourceTraitValueProviderBuilder chance(float chance) {
        return chance(chance, chance);
    }

    default ISourceTraitValueProviderBuilder chance(float chance, float chanceOnBred) {
        return () -> new ChanceSourceTraitValueProvider(this.build(), chance, chanceOnBred);
    }

    default ISourceTraitValueProviderBuilder predicate(IBlockPosPredicate predicate) {
        return () -> new PredicateSourceTraitValueProvider(this.build(), predicate);
    }

    ISourceTraitValueProvider build();

}
