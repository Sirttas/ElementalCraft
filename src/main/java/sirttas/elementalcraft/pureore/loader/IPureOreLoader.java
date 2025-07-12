package sirttas.elementalcraft.pureore.loader;

import com.mojang.serialization.Codec;
import net.minecraft.core.RegistryAccess;

import java.util.List;

public interface IPureOreLoader {

    Codec<IPureOreLoader> CODEC = PureOreLoaderTypes.REGISTRY.byNameCodec().dispatch(IPureOreLoader::type, PureOreLoaderType::codec);

    int getOrder();
    List<LoadedPureOre> generate(RegistryAccess registry);
    PureOreLoaderType<? extends IPureOreLoader> type();

}
