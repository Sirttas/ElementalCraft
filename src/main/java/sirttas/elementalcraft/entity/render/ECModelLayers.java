package sirttas.elementalcraft.entity.render;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.entity.spectral.SpectralTool;
import sirttas.elementalcraft.entity.spectral.SpectralToolModel;

@EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ECModelLayers {

    public static final ModelLayerLocation SPECTRAL_TOOL = new ModelLayerLocation(ElementalCraftApi.createRL(SpectralTool.NAME), "main");

    private ECModelLayers() {}

    @SubscribeEvent
    public static void registerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        event.registerLayerDefinition(SPECTRAL_TOOL, () -> LayerDefinition.create(SpectralToolModel.createMesh(), 64, 32));
    }
}
