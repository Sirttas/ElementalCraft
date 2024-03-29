package sirttas.elementalcraft.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import org.apache.commons.lang3.StringUtils;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.diffuser.DiffuserRenderer;
import sirttas.elementalcraft.block.instrument.io.mill.MillRenderer;
import sirttas.elementalcraft.block.pipe.ElementPipeRenderer;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgradeModelShaper;
import sirttas.elementalcraft.block.pipe.upgrade.beam.ElementBeamPipeUpgradeRenderer;
import sirttas.elementalcraft.block.pipe.upgrade.pump.ElementPumpPipeUpgradeRenderer;
import sirttas.elementalcraft.block.pipe.upgrade.valve.ElementValvePipeUpgradeRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.AccelerationShrineUpgradeRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.vortex.VortexShrineUpgradeRenderer;
import sirttas.elementalcraft.block.source.SourceRenderer;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerRenderer;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.rune.Runes;

import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECModelHandler {

    private ECModelHandler() { }

    @SubscribeEvent
    public static void initShapers(RenderLevelStageEvent.RegisterStageEvent event) { // We use this event because it is the first one fired after the ModelManager get initialized
        ECModelShapers.register(PipeUpgradeModelShaper.NAME, PipeUpgradeModelShaper::new);

        ECModelShapers.init(Minecraft.getInstance().getModelManager());
    }


    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterAdditional event) {
        var addModel = addModel(event::register);

        Runes.registerModels(addModel);
        Jewels.registerModels(addModel);
        ECModelShapers.getAll().forEach(shaper -> shaper.registerModels(addModel));
        event.register(ElementPipeRenderer.SIDE_LOCATION);
        event.register(ElementPipeRenderer.EXTRACT_LOCATION);
        event.register(SolarSynthesizerRenderer.LENSE_LOCATION);
        event.register(MillRenderer.WATER_MILL_GRINDSTONE_SHAFT_LOCATION);
        event.register(MillRenderer.AIR_MILL_GRINDSTONE_SHAFT_LOCATION);
        event.register(MillRenderer.WATER_MILL_WOOD_SAW_SHAFT_LOCATION);
        event.register(MillRenderer.AIR_MILL_WOOD_SAW_SHAFT_LOCATION);
        event.register(DiffuserRenderer.CUBE_LOCATION);
        event.register(AccelerationShrineUpgradeRenderer.CLOCK_LOCATION);
        event.register(VortexShrineUpgradeRenderer.RING_LOCATION);
        event.register(TranslocationShrineUpgradeRenderer.RING_LOCATION);
        event.register(SourceRenderer.STABILIZER_LOCATION);
        event.register(ElementValvePipeUpgradeRenderer.OPEN_LOCATION);
        event.register(ElementValvePipeUpgradeRenderer.CLOSE_LOCATION);
        event.register(ElementBeamPipeUpgradeRenderer.RING_1_LOCATION);
        event.register(ElementBeamPipeUpgradeRenderer.RING_2_LOCATION);
        event.register(ElementBeamPipeUpgradeRenderer.RING_3_LOCATION);
        event.register(ElementPumpPipeUpgradeRenderer.PUMP_LOCATION);
    }

    private static Consumer<ResourceLocation> addModel(Consumer<ResourceLocation> consumer) {
        return m -> {
            var path = StringUtils.removeStart(StringUtils.removeEnd(m.getPath(), ".json"), "models/");

            if (path.startsWith("item/")) {
                consumer.accept(new ModelResourceLocation(new ResourceLocation(m.getNamespace(), StringUtils.removeStart(path, "item/")), "inventory"));
            } else {
                consumer.accept(new ResourceLocation(m.getNamespace(), path));
            }
        };
    }

    @SubscribeEvent
    public static void onBakingComplete(ModelEvent.BakingCompleted event) {
        ECModelShapers.getAll().forEach(AbstractECModelShaper::rebuildCache);
    }
}
