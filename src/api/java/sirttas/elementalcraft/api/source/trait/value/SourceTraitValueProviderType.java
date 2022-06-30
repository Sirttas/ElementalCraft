package sirttas.elementalcraft.api.source.trait.value;

import com.mojang.serialization.Codec;
import sirttas.dpanvil.api.codec.ICodecProvider;

public record SourceTraitValueProviderType<T extends ISourceTraitValueProvider>(Codec<T> codec) implements ICodecProvider<T> {

}
