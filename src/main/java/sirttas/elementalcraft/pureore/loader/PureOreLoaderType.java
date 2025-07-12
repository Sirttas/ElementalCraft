package sirttas.elementalcraft.pureore.loader;

import com.mojang.serialization.MapCodec;

public record PureOreLoaderType<T extends IPureOreLoader>(MapCodec<T> codec) {}
