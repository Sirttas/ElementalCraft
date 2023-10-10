package sirttas.elementalcraft.pureore.loader;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;

import java.util.function.Supplier;

public class PureOreLoaderTypes {

    public static final ResourceKey<Registry<Codec<? extends IPureOreLoader>>> KEY = ResourceKey.createRegistryKey(ElementalCraft.createRL(ECNames.PURE_ORE_LOADER_SERIALIZER));

    private static final DeferredRegister<Codec<? extends IPureOreLoader>> DEFERRED_REGISTER = DeferredRegister.create(KEY, ElementalCraftApi.MODID);

    public static final Supplier<IForgeRegistry<Codec<? extends IPureOreLoader>>> REGISTRY = DEFERRED_REGISTER.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<Codec<PatternPureOreLoader>> PATTERN = register("pattern", PatternPureOreLoader.CODEC);
    public static final RegistryObject<Codec<FixedNamePureOreLoader>> FIXED_NAME = register("fixed_name", FixedNamePureOreLoader.CODEC);

    private static <T extends IPureOreLoader> RegistryObject<Codec<T>> register(String name, Codec<T> codec) {
        return DEFERRED_REGISTER.register(name, () -> codec);
    }

    private PureOreLoaderTypes() {
    }

    public static void register(IEventBus modBus) {
        DEFERRED_REGISTER.register(modBus);
    }

}
