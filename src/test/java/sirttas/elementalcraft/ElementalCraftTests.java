package sirttas.elementalcraft;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.testframework.Test;
import net.neoforged.testframework.conf.ClientConfiguration;
import net.neoforged.testframework.conf.Feature;
import net.neoforged.testframework.conf.FrameworkConfiguration;
import net.neoforged.testframework.conf.MissingDescriptionAction;
import net.neoforged.testframework.impl.MutableTestFramework;
import net.neoforged.testframework.summary.GitHubActionsStepSummaryDumper;
import net.neoforged.testframework.summary.JUnitSummaryDumper;
import org.lwjgl.glfw.GLFW;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.ElementalCraftInteraction;
import sirttas.elementalcraft.block.ECContainerBlockGameTests;
import sirttas.elementalcraft.block.airmill.AirMillGameTests;
import sirttas.elementalcraft.block.container.ContainerGameTests;
import sirttas.elementalcraft.block.container.reservoir.ReservoirGameTests;
import sirttas.elementalcraft.block.diffuser.DiffuserGameTests;
import sirttas.elementalcraft.block.doublehalf.DoubleHalfBlockGameTests;
import sirttas.elementalcraft.block.extractor.ElementExtractorGameTests;
import sirttas.elementalcraft.block.instrument.crystallizer.CrystallizerGameTests;
import sirttas.elementalcraft.block.instrument.infuser.InfuserGameTests;
import sirttas.elementalcraft.block.instrument.io.firefurnace.FireFurnaceGameTests;
import sirttas.elementalcraft.block.instrument.io.firefurnace.blast.FireBlastFurnaceGameTests;
import sirttas.elementalcraft.block.instrument.io.mill.MillGameTests;
import sirttas.elementalcraft.block.pipe.ElementPipeGameTests;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserGameTests;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineGameUpgradeTests;
import sirttas.elementalcraft.item.chisel.ChiselGameTests;
import sirttas.elementalcraft.item.cover.CoverFrameGameTests;
import sirttas.elementalcraft.item.holder.ElementHolderGameTests;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleGameTests;
import sirttas.elementalcraft.range.RangeGameTests;
import sirttas.elementalcraft.rune.RuneGameTests;

import java.nio.file.Path;
import java.util.function.Consumer;

public class ElementalCraftTests implements ElementalCraftInteraction {

    static MinecraftServer server;

    @Override
    public boolean isActive() {
        return ModList.get().isLoaded("testframework");
    }

    @Override
    public void registerTestFramework(IEventBus modBus, ModContainer container) {
        try {
            final MutableTestFramework framework = FrameworkConfiguration.builder(ElementalCraftApi.createRL("tests"))
                    .clientConfiguration(() -> ClientConfiguration.builder()
                            .toggleOverlayKey(GLFW.GLFW_KEY_J)
                            .openManagerKey(GLFW.GLFW_KEY_N)
                            .build())
                    .dumpers(new JUnitSummaryDumper(Path.of("tests/")), new GitHubActionsStepSummaryDumper())
                    .enable(Feature.CLIENT_SYNC, Feature.CLIENT_MODIFICATIONS, Feature.TEST_STORE)
                    .onMissingDescription(MissingDescriptionAction.ERROR)
                    .build().create();

            registerAdditionalTests(framework.tests()::register);
            framework.init(modBus, container);

            NeoForge.EVENT_BUS.addListener(EventPriority.HIGH, (final ServerStartedEvent event) -> {
                server = event.getServer();
            });
            NeoForge.EVENT_BUS.addListener((final RegisterCommandsEvent event) -> {
                final LiteralArgumentBuilder<CommandSourceStack> node = Commands.literal("tests");
                framework.registerCommands(node);
                event.getDispatcher().register(node);
            });
        } catch (Throwable t) {
            ElementalCraftApi.LOGGER.error("Failed to register test framework", t);
            throw t;
        }
    }

    private static void registerAdditionalTests(Consumer<Test> registrar) {
        ECContainerBlockGameTests.collectTests().forEach(registrar);
        ElementHolderGameTests.collectTests().forEach(registrar);
        ElementExtractorGameTests.collectTests().forEach(registrar);
        InfuserGameTests.collectTests().forEach(registrar);
        CrystallizerGameTests.collectTests().forEach(registrar);
        MillGameTests.collectTests().forEach(registrar);
        FireFurnaceGameTests.should_smelt().forEach(registrar);
        FireBlastFurnaceGameTests.collectTests().forEach(registrar);
        ChiselGameTests.collectTests().forEach(registrar);
        RuneGameTests.should_dropRunes().forEach(registrar);
        ReceptacleGameTests.collectTests().forEach(registrar);
        DiffuserGameTests.should_fillHolder().forEach(registrar);
        PureInfuserGameTests.shouldNot_craftWhenAPedestalIsBroken().forEach(registrar);
        ElementPipeGameTests.should_disconnectWhenBroken().forEach(registrar);
        RangeGameTests.should_haveRange().forEach(registrar);
        DoubleHalfBlockGameTests.should_breakBothParts().forEach(registrar);
        AirMillGameTests.collectTests().forEach(registrar);
        ContainerGameTests.should_supportARudimentaryExtractor().forEach(registrar);
        ReservoirGameTests.should_insertElementFromBothParts().forEach(registrar);
        ShrineGameUpgradeTests.should_breakUpgradesWhenBroken().forEach(registrar);
        CoverFrameGameTests.collectTests().forEach(registrar);
    }
}
