package sirttas.elementalcraft.pureore.loader;

import com.mojang.serialization.Codec;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.ExtraCodecs;
import sirttas.elementalcraft.pureore.PureOre;

import java.util.List;
import java.util.function.Function;

public interface IPureOreLoader {


    Codec<IPureOreLoader> CODEC = ExtraCodecs.lazyInitializedCodec(PureOreLoaderTypes.REGISTRY::byNameCodec).dispatch(IPureOreLoader::codec, Function.identity());

    int getOrder();
    List<PureOre> generate(RegistryAccess registry);
    Codec<? extends IPureOreLoader> codec();

}
