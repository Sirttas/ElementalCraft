package sirttas.elementalcraft.block.source.flux;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.config.ECConfig;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SourceFluxModHandler {

    private SourceFluxModHandler() { }

    private static SourceFluxConfig config = new SourceFluxConfig(1200, 1, 1, 1);

    public static SourceFluxConfig getConfig() {
        return config;
    }

    @SubscribeEvent
    public static void reloadConfig(ModConfigEvent.Loading event) {
        doReload(event);
    }

    @SubscribeEvent
    public static void reloadConfig(ModConfigEvent.Reloading event) {
        doReload(event);
    }

    private static void doReload(ModConfigEvent event) {
        if (event.getConfig().getSpec() == ECConfig.SERVER_SPEC) {
            config = new SourceFluxConfig(
                    ECConfig.SERVER.sourceFluxCapacity.get().floatValue(),
                    ECConfig.SERVER.sourceFluxRecovery.get().floatValue(),
                    ECConfig.SERVER.sourceFluxConsumption.get().floatValue(),
                    ECConfig.SERVER.sourceFluxTransfer.get().floatValue()
            );
        }
    }
}
