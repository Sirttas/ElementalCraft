package sirttas.elementalcraft.pureore.loader;

import com.mojang.serialization.Codec;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.crafting.Recipe;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.pureore.PureOre;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public interface IPureOreLoader {


    Codec<IPureOreLoader> CODEC = ExtraCodecs.lazyInitializedCodec(() -> PureOreLoaderTypes.REGISTRY.get().getCodec()).dispatch(IPureOreLoader::codec, Function.identity());

    int getOrder();
    List<PureOre> generate(RegistryAccess registry, Collection<AbstractPureOreRecipeInjector<?, ? extends Recipe<?>>> injectors);
    Codec<? extends IPureOreLoader> codec();

}
