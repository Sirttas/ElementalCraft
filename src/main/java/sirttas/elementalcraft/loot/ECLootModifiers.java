package sirttas.elementalcraft.loot;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECLootModifiers {

    private static final DeferredRegister<Codec<? extends IGlobalLootModifier>> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ElementalCraftApi.MODID);

    public static final RegistryObject<Codec<FireInfusionLootModifier>> FIRE_INFUSION = register(FireInfusionLootModifier.DIRECT_CODEC, "fire_infusion");

    private ECLootModifiers() {}

    private static <T extends IGlobalLootModifier> RegistryObject<Codec<T>> register(Codec<T> codec, String name) {
        return DEFERRED_REGISTER.register(name, () -> codec);
    }

    public static void register(IEventBus modBus) {
        DEFERRED_REGISTER.register(modBus);
    }

}
