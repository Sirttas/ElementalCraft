package sirttas.elementalcraft.client.model;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.diffuser.DiffuserRenderer;
import sirttas.elementalcraft.block.instrument.io.mill.MillRenderer;
import sirttas.elementalcraft.block.pipe.section.ElementPipeSectionRenderer;
import sirttas.elementalcraft.block.pipe.upgrade.beam.ElementBeamPipeUpgradeRenderer;
import sirttas.elementalcraft.block.pipe.upgrade.pump.ElementPumpPipeUpgradeRenderer;
import sirttas.elementalcraft.block.pipe.upgrade.render.model.PipeUpgradeModelResolver;
import sirttas.elementalcraft.block.pipe.upgrade.valve.ElementValvePipeUpgradeRenderer;
import sirttas.elementalcraft.block.shrine.budding.BuddingShrinePlateModelResolver;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.AccelerationShrineUpgradeRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.vortex.VortexShrineUpgradeRenderer;
import sirttas.elementalcraft.block.source.SourceRenderer;
import sirttas.elementalcraft.block.synthesizer.cracking.CrackingSynthesizerRenderer;
import sirttas.elementalcraft.block.synthesizer.mill.AirMillSynthesizerRenderer;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerRenderer;
import sirttas.elementalcraft.rune.RuneModelResolver;
import sirttas.elementalcraft.rune.RuneSpecialRenderer;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class ECModelHandler {

    private ECModelHandler() { }

    @SubscribeEvent
    public static void registerSpecialModelRenderer(RegisterSpecialModelRendererEvent event) {
        event.register(RuneSpecialRenderer.IDENTIFIER, RuneSpecialRenderer.Unbaked.MAP_CODEC);
    }

    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterStandalone event) {
        var modelManager = Minecraft.getInstance().getModelManager();

        ECModelResolver.register(RuneModelResolver.IDENTIFIER, new RuneModelResolver(modelManager));
        ECModelResolver.register(PipeUpgradeModelResolver.IDENTIFIER, new PipeUpgradeModelResolver(modelManager));
        ECModelResolver.register(BuddingShrinePlateModelResolver.IDENTIFIER, new BuddingShrinePlateModelResolver(modelManager));
        ECModelResolver.getAll().forEach(resolver -> resolver.registerModels(event::register));

        register(event, ElementPipeSectionRenderer.SIDE);
        register(event, ElementPipeSectionRenderer.EXTRACT);
        register(event, CrackingSynthesizerRenderer.HEAD_LOCATION);
        register(event, SolarSynthesizerRenderer.LENS);
        register(event, MillRenderer.WATER_MILL_GRINDSTONE_SHAFT);
        register(event, MillRenderer.AIR_MILL_GRINDSTONE_SHAFT);
        register(event, MillRenderer.WATER_MILL_WOOD_SAW_SHAFT);
        register(event, MillRenderer.AIR_MILL_WOOD_SAW_SHAFT);
        register(event, AirMillSynthesizerRenderer.SHAFT);
        register(event, DiffuserRenderer.CUBE);
        register(event, AccelerationShrineUpgradeRenderer.CLOCK);
        register(event, VortexShrineUpgradeRenderer.RING);
        register(event, TranslocationShrineUpgradeRenderer.RING);
        register(event, SourceRenderer.STABILIZER);
        register(event, ElementValvePipeUpgradeRenderer.OPEN);
        register(event, ElementValvePipeUpgradeRenderer.CLOSE);
        register(event, ElementBeamPipeUpgradeRenderer.RING_1);
        register(event, ElementBeamPipeUpgradeRenderer.RING_2);
        register(event, ElementBeamPipeUpgradeRenderer.RING_3);
        register(event, ElementPumpPipeUpgradeRenderer.PUMP);
    }

    private static void register(ModelEvent.RegisterStandalone event, SimpleStandaloneModelSupplier supplier) {
       event.register(supplier.key(), SimpleUnbakedStandaloneModel.simpleModelWrapper(supplier.identifier()));
    }
}
