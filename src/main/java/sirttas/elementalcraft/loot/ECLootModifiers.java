package sirttas.elementalcraft.loot;

import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECLootModifiers {

    private static final DeferredRegister<Codec<? extends IGlobalLootModifier>> DEFERRED_REGISTER = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ElementalCraftApi.MODID);

    public static final DeferredHolder<Codec<? extends IGlobalLootModifier>, Codec<FireInfusionLootModifier>> FIRE_INFUSION = register(FireInfusionLootModifier.DIRECT_CODEC, "fire_infusion");

    private ECLootModifiers() {}

    private static <T extends IGlobalLootModifier> DeferredHolder<Codec<? extends IGlobalLootModifier>, Codec<T>> register(Codec<T> codec, String name) {
        return DEFERRED_REGISTER.register(name, () -> codec);
    }

    public static void register(IEventBus modBus) {
        DEFERRED_REGISTER.register(modBus);
    }

}
