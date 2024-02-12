package sirttas.elementalcraft.entity;


import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.entity.projectile.FeatherSpikeRenderer;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECEntityRenderers {

    private ECEntityRenderers() {}

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ECEntities.FEATHER_SPIKE.get(), FeatherSpikeRenderer::new);
        event.registerEntityRenderer(ECEntities.THROWN_ELEMENT_CRYSTAL.get(), ThrownItemRenderer::new);
    }
}
