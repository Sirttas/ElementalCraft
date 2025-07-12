package sirttas.elementalcraft.pureore.loader;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;

public class PureOreLoaderTypes {

    public static final ResourceKey<Registry<PureOreLoaderType<? extends IPureOreLoader>>> KEY = ResourceKey.createRegistryKey(ElementalCraftApi.createRL(ECNames.PURE_ORE_LOADER_SERIALIZER));

    private static final DeferredRegister<PureOreLoaderType<? extends IPureOreLoader>> DEFERRED_REGISTER = DeferredRegister.create(KEY, ElementalCraftApi.MODID);

    public static final Registry<PureOreLoaderType<? extends IPureOreLoader>> REGISTRY = DEFERRED_REGISTER.makeRegistry(b -> b.sync(true));

    public static final DeferredHolder<PureOreLoaderType<? extends IPureOreLoader>, PureOreLoaderType<PatternPureOreLoader>> PATTERN = register("pattern", PatternPureOreLoader.CODEC);
    public static final DeferredHolder<PureOreLoaderType<? extends IPureOreLoader>, PureOreLoaderType<FixedNamePureOreLoader>> FIXED_NAME = register("fixed_name", FixedNamePureOreLoader.CODEC);

    private static <T extends IPureOreLoader> DeferredHolder<PureOreLoaderType<? extends IPureOreLoader>, PureOreLoaderType<T>> register(String name, MapCodec<T> codec) {
        return DEFERRED_REGISTER.register(name, () -> new PureOreLoaderType<>(codec));
    }

    private PureOreLoaderTypes() {
    }

    public static void register(IEventBus modBus) {
        DEFERRED_REGISTER.register(modBus);
    }

}
