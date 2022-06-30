package sirttas.elementalcraft.loot;

import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.api.ElementalCraftApi;

import java.util.function.Supplier;

public class ECLootModifiers {

    private static final DeferredRegister<GlobalLootModifierSerializer<?>> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, ElementalCraftApi.MODID);

    public static final RegistryObject<GlobalLootModifierSerializer<FireInfusionLootModifier>> FIRE_INFUSION = register(FireInfusionLootModifier.Serializer::new, "fire_infusion");

    private ECLootModifiers() {}

    private static <L extends LootModifier, T extends GlobalLootModifierSerializer<? extends L>> RegistryObject<T> register(Supplier<? extends T> builder, String name) {
        return DEFERRED_REGISTER.register(name, builder);
    }

    public static void register(IEventBus modBus) {
        DEFERRED_REGISTER.register(modBus);
    }

}
