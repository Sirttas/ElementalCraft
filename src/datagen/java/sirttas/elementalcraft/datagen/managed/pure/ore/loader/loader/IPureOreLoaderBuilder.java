package sirttas.elementalcraft.datagen.managed.pure.ore.loader.loader;

import com.mojang.serialization.Encoder;
import sirttas.elementalcraft.pureore.loader.IPureOreLoader;

@FunctionalInterface
public interface IPureOreLoaderBuilder {

    Encoder<IPureOreLoaderBuilder> ENCODER = IPureOreLoader.CODEC.comap(IPureOreLoaderBuilder::build);

    IPureOreLoader build();

}
