package sirttas.elementalcraft.registry;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.registry.ElementalCraftRegistries;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECRegistryHandler {

    private ECRegistryHandler() { }

    @SubscribeEvent
    private static void registerRegistries(NewRegistryEvent event) {
        event.register(ElementalCraftRegistries.SOURCE_TRAIT_VALUE_PROVIDER_TYPE);
        event.register(ElementalCraftRegistries.TOOL_INFUSION_EFFECT_TYPE);
    }

}
