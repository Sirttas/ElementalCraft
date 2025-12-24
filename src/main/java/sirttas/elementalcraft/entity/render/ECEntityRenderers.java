package sirttas.elementalcraft.entity.render;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.entity.ECEntities;
import sirttas.elementalcraft.entity.projectile.FeatherSpikeRenderer;
import sirttas.elementalcraft.entity.spectral.SpectralToolRenderer;

@EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ECEntityRenderers {

    private ECEntityRenderers() {}

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ECEntities.FEATHER_SPIKE.get(), FeatherSpikeRenderer::new);
        event.registerEntityRenderer(ECEntities.SPECTRAL_TOOL.get(), SpectralToolRenderer::new);
    }
}
