package sirttas.elementalcraft.loot;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECLootModifiers {

    private static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> DEFERRED_REGISTER = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ElementalCraftApi.MODID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<FireInfusionLootModifier>> FIRE_INFUSION = register("fire_infusion", FireInfusionLootModifier.DIRECT_CODEC);

    private ECLootModifiers() {}

    private static <T extends IGlobalLootModifier> DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<T>> register(String name, MapCodec<T> codec) {
        return DEFERRED_REGISTER.register(name, () -> codec);
    }

    public static void register(IEventBus modBus) {
        DEFERRED_REGISTER.register(modBus);
    }

}
