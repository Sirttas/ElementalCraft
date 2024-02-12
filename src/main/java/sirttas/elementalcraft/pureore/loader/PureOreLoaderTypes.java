package sirttas.elementalcraft.pureore.loader;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.function.Consumers;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;

public class PureOreLoaderTypes {

    public static final ResourceKey<Registry<Codec<? extends IPureOreLoader>>> KEY = ResourceKey.createRegistryKey(ElementalCraftApi.createRL(ECNames.PURE_ORE_LOADER_SERIALIZER));

    private static final DeferredRegister<Codec<? extends IPureOreLoader>> DEFERRED_REGISTER = DeferredRegister.create(KEY, ElementalCraftApi.MODID);

    public static final Registry<Codec<? extends IPureOreLoader>> REGISTRY = DEFERRED_REGISTER.makeRegistry(Consumers.nop());

    public static final DeferredHolder<Codec<? extends IPureOreLoader>, Codec<PatternPureOreLoader>> PATTERN = register("pattern", PatternPureOreLoader.CODEC);
    public static final DeferredHolder<Codec<? extends IPureOreLoader>, Codec<FixedNamePureOreLoader>> FIXED_NAME = register("fixed_name", FixedNamePureOreLoader.CODEC);

    private static <T extends IPureOreLoader> DeferredHolder<Codec<? extends IPureOreLoader>, Codec<T>> register(String name, Codec<T> codec) {
        return DEFERRED_REGISTER.register(name, () -> codec);
    }

    private PureOreLoaderTypes() {
    }

    public static void register(IEventBus modBus) {
        DEFERRED_REGISTER.register(modBus);
    }

}
