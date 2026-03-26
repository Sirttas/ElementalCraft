package sirttas.elementalcraft.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.block.shrine.budding.BuddingShrineBudType;
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
import sirttas.elementalcraft.block.synthesizer.cracking.CrackingSynthesizerRenderer;
import sirttas.elementalcraft.block.synthesizer.mill.AirMillSynthesizerRenderer;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerRenderer;

import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class ECModelHandler {

    private ECModelHandler() { }

    @SubscribeEvent
    public static void initShapers(RenderLevelStageEvent.RegisterStageEvent event) { // We use this event because it is the first one fired after the ModelManager get initialized
        ECModelShapers.register(PipeUpgradeModelShaper.NAME, PipeUpgradeModelShaper::new);

        ECModelShapers.init(Minecraft.getInstance().getModelManager());
    }


    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterStandalone event) {

        registerRuneModels(addModel);
        registerBuddingShrinePlatesModels(addModel);
        ECModelShapers.getAll().forEach(shaper -> shaper.registerModels(event::register));
        event.register(ElementPipeRenderer.SIDE_LOCATION);
        event.register(ElementPipeRenderer.EXTRACT_LOCATION);
        event.register(CrackingSynthesizerRenderer.HEAD_LOCATION);
        event.register(SolarSynthesizerRenderer.LENS_LOCATION);
        event.register(MillRenderer.WATER_MILL_GRINDSTONE_SHAFT_LOCATION);
        event.register(MillRenderer.AIR_MILL_GRINDSTONE_SHAFT_LOCATION);
        event.register(MillRenderer.WATER_MILL_WOOD_SAW_SHAFT_LOCATION);
        event.register(MillRenderer.AIR_MILL_WOOD_SAW_SHAFT_LOCATION);
        event.register(AirMillSynthesizerRenderer.SHAFT_LOCATION);
        register(event, DiffuserRenderer.CUBE);
        register(event, AccelerationShrineUpgradeRenderer.CLOCK);
        event.register(VortexShrineUpgradeRenderer.RING_LOCATION);
        event.register(TranslocationShrineUpgradeRenderer.RING_LOCATION);
        event.register(SourceRenderer.STABILIZER_LOCATION);
        event.register(ElementValvePipeUpgradeRenderer.OPEN_LOCATION);
        event.register(ElementValvePipeUpgradeRenderer.CLOSE_LOCATION);
        event.register(ElementBeamPipeUpgradeRenderer.RING_1_LOCATION);
        event.register(ElementBeamPipeUpgradeRenderer.RING_2_LOCATION);
        event.register(ElementBeamPipeUpgradeRenderer.RING_3_LOCATION);
        event.register(ElementPumpPipeUpgradeRenderer.PUMP_LOCATION);
        event.register(ECModelHelper.createStandaloneKey("item/air_mill_synthesizer_broken"));
        event.register(ECModelHelper.createStandaloneKey("item/air_mill_grindstone_broken"));
        event.register(ECModelHelper.createStandaloneKey("item/air_mill_wood_saw_broken"));
    }

    private static void registerRuneModels(Consumer<Identifier> addModel) {
        ElementalCraftApi.RUNE_MANAGER.getData().values().forEach(rune -> addModel.accept(rune.getModelName()));
        addAllModelsInManagerFolder(ElementalCraftApi.RUNE_MANAGER, addModel);
    }

    private static void registerBuddingShrinePlatesModels(Consumer<Identifier> addModel) {
        ElementalCraftApi.BUD_TYPE_MANAGER.getData().values().forEach(budType -> addModel.accept(budType.plateModel()));
        addModel.accept(BuddingShrineBudType.AMETHYST.plateModel());
        addAllModelsFolder(BuddingShrineBudType.PLATE_MODEL_FOLDER, addModel);
    }

    private static void addAllModelsInManagerFolder(IDataManager<?> manager, Consumer<Identifier> addModel) {
        addAllModelsFolder(manager.getFolder(), addModel);
    }

    private static void addAllModelsFolder(String folder, Consumer<Identifier> addModel) {
        Minecraft.getInstance().getResourceManager().listResources("models/" + folder, fileName -> fileName.getPath().endsWith(".json")).keySet().forEach(addModel);
    }

    private static void register(ModelEvent.RegisterStandalone event, SimpleStandaloneModelSupplier supplier) {
       event.register(supplier.key(), SimpleUnbakedStandaloneModel.blockStateModel(supplier.identifier()));

    }

    @SubscribeEvent
    public static void onBakingComplete(ModelEvent.BakingCompleted event) {
        ECModelShapers.getAll().forEach(AbstractECModelShaper::rebuildCache);
    }
}
