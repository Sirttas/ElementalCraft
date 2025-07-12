package sirttas.elementalcraft.api.source.trait.value;

import com.mojang.serialization.MapCodec;

public record SourceTraitValueProviderType<T extends ISourceTraitValueProvider>(MapCodec<T> codec) {

}
