package sirttas.elementalcraft.datagen.interaction.patchouli;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.registries.DeferredHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgradeBlock;
import sirttas.elementalcraft.datagen.interaction.patchouli.builder.BookBuilder;
import sirttas.elementalcraft.datagen.interaction.patchouli.builder.CategoryBuilder;
import sirttas.elementalcraft.datagen.interaction.patchouli.builder.EntryBuilder;
import sirttas.elementalcraft.datagen.interaction.patchouli.builder.PatchouliFile;
import sirttas.elementalcraft.datagen.interaction.patchouli.builder.page.PageBuilder;
import sirttas.elementalcraft.datagen.interaction.patchouli.builder.page.custom.CheckPageBuilder;
import sirttas.elementalcraft.datagen.language.TranslationKeyValidator;
import sirttas.elementalcraft.item.ECCreativeModeTabs;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.rune.Runes;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.Spells;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BookDataProvider implements DataProvider {

    private final PackOutput packOutput;
    private final CompletableFuture<HolderLookup.Provider> registries;
    private final TranslationKeyValidator translationKeyValidator;

    public BookDataProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, TranslationKeyValidator translationKeyValidator) {
        this.packOutput = packOutput;
        this.registries = registries;
        this.translationKeyValidator = translationKeyValidator;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return registries.thenCompose(r -> CompletableFuture.allOf(generate().stream()
                .map(book -> save(cache, r, book))
                .toArray(CompletableFuture[]::new)));
    }

    private List<BookBuilder> generate() {
        var book = new BookBuilder(ElementalCraftApi.identifier("element_book"), translationKeyValidator)
                .landingText("elementalcraft.landing")
                .creativeTab(ECCreativeModeTabs.ELEMENTAL_CRAFT_CREATIVE_TAB)
                .i18n()
                .version("24")
                .macro("$(ec:source)", "$(l:sources/source)")
                .macro("$(ec:container)", "$(l:instruments/container)")
                .macro("$(ec:small_container)", "$(l:instruments/small_container)")
                .macro("$(ec:pipe)", "$(l:pipes/elementpipe)")
                .macro("$(ec:infuser)", "$(l:instruments/infuser)")
                .macro("$(ec:extractor)", "$(l:instruments/extractor)")
                .macro("$(ec:binder)", "$(l:instruments/binder)")
                .macro("$(ec:firefurnace)", "$(l:instruments/firefurnace)")
                .macro("$(ec:holder)", "$(l:advanced/element_holder)")
                .macro("$(ec:source_traits)", "$(l:source/source_traits)")
                .macro("$(ec:rune)", "$(l:advanced/rune)");

        generateBasics(book);
        generateSources(book);
        generateElementProduction(book);
        generatePipes(book);
        generateInstruments(book);
        generateAdvanced(book);
        generateShrines(book);
        generateShrineUpgrades(book);
        generateSpells(book);
        generateJewels(book);

        return List.of(book);
    }

    private static void generateBasics(BookBuilder book) {
        var basics = book.category("basics")
                .icon(ECItems.INERT_CRYSTAL.get());

        basics.entry(ECItems.INERT_CRYSTAL.get())
                .priority()
                .turnIn(ElementalCraftApi.identifier("main/inert_crystal"))
                .page(PageBuilder.text("elementalcraft.page.inert_crystal0"))
                .page(PageBuilder.spotlight(ECItems.INERT_CRYSTAL.get(), true));
        basics.entry(ECItems.CONTAINED_CRYSTAL.get())
                .priority()
                .advancement(ElementalCraftApi.identifier("main/inert_crystal"))
                .turnIn(ElementalCraftApi.identifier("main/contained_crystal"))
                .page(PageBuilder.text("elementalcraft.page.contained_crystal0"))
                .page(PageBuilder.crafting(ECItems.CONTAINED_CRYSTAL.get()));
        basics.entry(ECBlocks.SMALL_CONTAINER.get())
                .priority()
                .advancement(ElementalCraftApi.identifier("main/contained_crystal"))
                .turnIn(ElementalCraftApi.identifier("main/small_container"))
                .page(PageBuilder.text("elementalcraft.page.small_container0"))
                .page(PageBuilder.crafting(ECBlocks.SMALL_CONTAINER.get()));
        basics.entry(ECBlocks.WHITE_ROCK.get())
                .priority()
                .advancement(ElementalCraftApi.identifier("main/infuser"))
                .turnIn(ElementalCraftApi.identifier("pickup/whiterock"))
                .page(PageBuilder.text("elementalcraft.page.whiterock0"))
                .page(PageBuilder.crafting(ECBlocks.WHITE_ROCK_STAIRS.get(), ECBlocks.WHITE_ROCK_SLAB.get()))
                .page(PageBuilder.crafting(ECBlocks.WHITE_ROCK_WALL.get()));
        basics.entry("gems")
                .icon(ECItems.PRISTINE_FIRE_GEM.get())
                .priority()
                .advancement(ElementalCraftApi.identifier("main/infuser"))
                .turnIn(ElementalCraftApi.identifier("main/pristine_gems"))
                .page(PageBuilder.text("elementalcraft.page.gems0"));
        basics.entry(ECBlocks.CONTAINER.get())
                .priority()
                .advancement(ElementalCraftApi.identifier("main/small_container"))
                .turnIn(ElementalCraftApi.identifier("main/container"))
                .page(PageBuilder.text("elementalcraft.page.container0"))
                .page(PageBuilder.crafting(ECBlocks.CONTAINER.get()));
        basics.entry(ECItems.SPRINGALINE_SHARD.get())
                .priority()
                .advancement(ElementalCraftApi.identifier("main/binder"))
                .turnIn(ElementalCraftApi.identifier("main/springaline_shard"))
                .page(PageBuilder.text("elementalcraft.page.springaline0"));
        basics.entry(ECItems.FIRE_LENS.get())
                .advancement(ElementalCraftApi.identifier("main/springaline_shard"))
                .turnIn(ElementalCraftApi.identifier("pickup/fire_lens"))
                .page(PageBuilder.text("elementalcraft.page.fire_lens0"));
        basics.entry(ECBlocks.PURE_INFUSER.get())
                .advancement(ElementalCraftApi.identifier("main/crystallizer"))
                .turnIn(ElementalCraftApi.identifier("pickup/pure_infuser"))
                .page(PageBuilder.text("elementalcraft.page.pure_infuser0"))
                .page(PageBuilder.crafting(ECBlocks.PURE_INFUSER.get()))
                .page(PageBuilder.multiblock()
                        .define('w', ECBlocks.WATER_PEDESTAL.get())
                        .define('f', ECBlocks.FIRE_PEDESTAL.get())
                        .define('e', ECBlocks.EARTH_PEDESTAL.get())
                        .define('a', ECBlocks.AIR_PEDESTAL.get())
                        .define('0', ECBlocks.PURE_INFUSER.get())
                        .patternLayer(
                                "   w   ",
                                "       ",
                                "       ",
                                "f  0  e",
                                "       ",
                                "       ",
                                "   a   "));
    }

    private static void generateSources(BookBuilder book) {
        var sources = book.category("sources")
                .icon(ECBlocks.FIRE_SOURCE.get());

        sources.entry("sources")
                .icon(ECBlocks.FIRE_SOURCE.get())
                .priority()
                .turnIn(ElementalCraftApi.identifier("main/sources"))
                .page(PageBuilder.text("elementalcraft.page.sources0"))
                .page(PageBuilder.text("elementalcraft.page.sources1"))
                .page(PageBuilder.image("elementalcraft.page.sources2", true, ElementalCraftApi.identifier("textures/gui/entries/source0.png")));
        sources.entry("source_traits")
                .icon(ECItems.SPRINGALINE_SHARD.get())
                .priority()
                .page(PageBuilder.text("elementalcraft.page.source_traits0"))
                .page(PageBuilder.text("elementalcraft.page.source_traits1"))
                .page(PageBuilder.text("elementalcraft.page.source_traits2"));
        sources.entry(ECItems.EMPTY_RECEPTACLE.get())
                .advancement(ElementalCraftApi.identifier("main/sources"))
                .turnIn(ElementalCraftApi.identifier("main/empty_receptacle"))
                .page(PageBuilder.text("elementalcraft.page.empty_receptacle0"))
                .page(PageBuilder.crafting(ECItems.EMPTY_RECEPTACLE.get()));
        sources.entry(ECItems.SOURCE_ANALYSIS_GLASS.get())
                .advancement(ElementalCraftApi.identifier("main/empty_receptacle"))
                .turnIn(ElementalCraftApi.identifier("main/source_analysis_glass"))
                .page(PageBuilder.text("elementalcraft.page.source_analysis_glass0"))
                .page(PageBuilder.crafting(ECItems.SOURCE_ANALYSIS_GLASS.get()));
        sources.entry(ECItems.SOURCE_STABILIZER.get())
                .advancement(ElementalCraftApi.identifier("main/empty_receptacle"))
                .turnIn(ElementalCraftApi.identifier("main/source_stabilizer"))
                .page(PageBuilder.text("elementalcraft.page.source_stabilizer0"))
                .page(PageBuilder.crafting(ECItems.SOURCE_STABILIZER.get()));
        sources.entry(ECBlocks.SOURCE_BREEDER.get())
                .advancement(ElementalCraftApi.identifier("main/empty_receptacle"))
                .turnIn(ElementalCraftApi.identifier("pickup/source_breeder"))
                .page(PageBuilder.text("elementalcraft.page.source_breeder0"))
                .page(PageBuilder.crafting(ECBlocks.SOURCE_BREEDER.get(), ECBlocks.SOURCE_BREEDER_PEDESTAL.get()))
                .page(PageBuilder.multiblock()
                        .define('p', ECBlocks.SOURCE_BREEDER_PEDESTAL.get())
                        .define('0', ECBlocks.SOURCE_BREEDER.get().defaultBlockState()
                                .setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER))
                        .define('1', ECBlocks.SOURCE_BREEDER.get().defaultBlockState()
                                .setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER))
                        .patternLayer("  0  ")
                        .patternLayer("p 1 p"));
        sources.entry("source_seeds")
                .icon(ECItems.FIRE_SOURCE_SEED.get())
                .advancement(ElementalCraftApi.identifier("main/empty_receptacle"))
                .page(PageBuilder.text("elementalcraft.page.source_seeds0"))
                .page(PageBuilder.crafting(ECItems.FIRE_SOURCE_SEED.get(), ECItems.WATER_SOURCE_SEED.get()))
                .page(PageBuilder.crafting(ECItems.EARTH_SOURCE_SEED.get(), ECItems.AIR_SOURCE_SEED.get()));
    }

    private static void generateElementProduction(BookBuilder book) {
        var elementProduction = book.category("element_production")
                .icon(ECBlocks.RUDIMENTARY_EXTRACTOR.get());

        elementProduction.entry(ECBlocks.RUDIMENTARY_EXTRACTOR.get())
                .priority()
                .advancement(ElementalCraftApi.identifier("main/small_container"))
                .turnIn(ElementalCraftApi.identifier("main/rudimentary_extractor"))
                .page(PageBuilder.text("elementalcraft.page.rudimentary_extractor0"))
                .page(PageBuilder.crafting(ECBlocks.RUDIMENTARY_EXTRACTOR.get()))
                .page(PageBuilder.multiblock()
                        .define('I', ECBlocks.RUDIMENTARY_EXTRACTOR.get())
                        .define('0', ECBlocks.SMALL_CONTAINER.get())
                        .patternLayer("I")
                        .patternLayer("0"));
        elementProduction.entry(ECBlocks.EXTRACTOR.get())
                .advancement(ElementalCraftApi.identifier("main/rudimentary_extractor"))
                .turnIn(ElementalCraftApi.identifier("main/extractor"))
                .page(PageBuilder.text("elementalcraft.page.extractor0"))
                .page(PageBuilder.crafting(ECBlocks.EXTRACTOR.get()))
                .page(PageBuilder.multiblock()
                        .define('I', ECBlocks.EXTRACTOR.get())
                        .define('0', ECBlocks.CONTAINER.get())
                        .patternLayer("I")
                        .patternLayer("0"));
        elementProduction.entry(ECBlocks.IMPROVED_EXTRACTOR.get())
                .advancement(ElementalCraftApi.identifier("main/extractor"))
                .turnIn(ElementalCraftApi.identifier("main/improved_extractor"))
                .page(PageBuilder.text("elementalcraft.page.improved_extractor0"))
                .page(PageBuilder.crafting(ECBlocks.IMPROVED_EXTRACTOR.get()))
                .page(PageBuilder.multiblock()
                        .define('I', ECBlocks.IMPROVED_EXTRACTOR.get())
                        .define('0', ECBlocks.CONTAINER.get())
                        .patternLayer("I")
                        .patternLayer("0"));
        elementProduction.entry(ECBlocks.CRACKING_SYNTHESIZER.get())
                .advancement(ElementalCraftApi.identifier("main/small_container"))
                .turnIn(ElementalCraftApi.identifier("pickup/cracking_earth_synthesizer"))
                .page(PageBuilder.text("elementalcraft.page.cracking_earth_synthesizer0"))
                .page(PageBuilder.crafting(ECBlocks.CRACKING_SYNTHESIZER.get()))
                .page(PageBuilder.multiblock()
                        .define('I', ECBlocks.CRACKING_SYNTHESIZER.get())
                        .define('0', ECBlocks.SMALL_CONTAINER.get())
                        .patternLayer("I")
                        .patternLayer("0"));
        elementProduction.entry(ECBlocks.COMBUSTION_SYNTHESIZER.get())
                .advancement(ElementalCraftApi.identifier("main/small_container"))
                .turnIn(ElementalCraftApi.identifier("pickup/combustion_fire_synthesizer"))
                .page(PageBuilder.text("elementalcraft.page.combustion_fire_synthesizer0"))
                .page(PageBuilder.crafting(ECBlocks.COMBUSTION_SYNTHESIZER.get()))
                .page(PageBuilder.multiblock()
                        .define('I', ECBlocks.COMBUSTION_SYNTHESIZER.get())
                        .define('0', ECBlocks.SMALL_CONTAINER.get())
                        .patternLayer("I")
                        .patternLayer("0"));
        elementProduction.entry(ECBlocks.DRAINING_SYNTHESIZER.get())
                .advancement(ElementalCraftApi.identifier("main/small_container"))
                .turnIn(ElementalCraftApi.identifier("pickup/draining_water_synthesizer"))
                .page(PageBuilder.text("elementalcraft.page.draining_water_synthesizer0"))
                .page(PageBuilder.crafting(ECBlocks.DRAINING_SYNTHESIZER.get()))
                .page(PageBuilder.multiblock()
                        .define('I', ECBlocks.DRAINING_SYNTHESIZER.get())
                        .define('0', ECBlocks.SMALL_CONTAINER.get())
                        .patternLayer("I")
                        .patternLayer("0"));
        elementProduction.entry(ECBlocks.VIBRATION_SYNTHESIZER.get())
                .advancement(ElementalCraftApi.identifier("main/small_container"))
                .turnIn(ElementalCraftApi.identifier("pickup/vibration_air_synthesizer"))
                .page(PageBuilder.text("elementalcraft.page.vibration_air_synthesizer0"))
                .page(PageBuilder.crafting(ECBlocks.VIBRATION_SYNTHESIZER.get()))
                .page(PageBuilder.multiblock()
                        .define('I', ECBlocks.VIBRATION_SYNTHESIZER.get())
                        .define('0', ECBlocks.SMALL_CONTAINER.get())
                        .patternLayer("I")
                        .patternLayer("0"));
        elementProduction.entry(ECBlocks.SOLAR_SYNTHESIZER.get())
                .advancement(ElementalCraftApi.identifier("main/container"))
                .turnIn(ElementalCraftApi.identifier("pickup/solar_fire_synthesizer"))
                .page(PageBuilder.text("elementalcraft.page.solar_fire_synthesizer0"))
                .page(PageBuilder.crafting(ECBlocks.SOLAR_SYNTHESIZER.get()))
                .page(PageBuilder.multiblock()
                        .define('I', ECBlocks.SOLAR_SYNTHESIZER.get())
                        .define('0', ECBlocks.CONTAINER.get())
                        .patternLayer("I")
                        .patternLayer("0"));
        elementProduction.entry(ECBlocks.CULINARY_SYNTHESIZER.get())
                .advancement(ElementalCraftApi.identifier("main/container"))
                .turnIn(ElementalCraftApi.identifier("pickup/culinary_water_synthesizer"))
                .page(PageBuilder.text("elementalcraft.page.culinary_water_synthesizer0"))
                .page(PageBuilder.crafting(ECBlocks.CULINARY_SYNTHESIZER.get()))
                .page(PageBuilder.multiblock()
                        .define('I', ECBlocks.CULINARY_SYNTHESIZER.get())
                        .define('0', ECBlocks.CONTAINER.get())
                        .patternLayer("I")
                        .patternLayer("0"));
        elementProduction.entry(ECBlocks.SCULK_CRACKING_SYNTHESIZER.get())
                .advancement(ElementalCraftApi.identifier("main/container"))
                .turnIn(ElementalCraftApi.identifier("pickup/sculk_cracking_earth_synthesizer"))
                .page(PageBuilder.text("elementalcraft.page.sculk_cracking_earth_synthesizer0"))
                .page(PageBuilder.crafting(ECBlocks.SCULK_CRACKING_SYNTHESIZER.get()))
                .page(PageBuilder.multiblock()
                        .define('I', ECBlocks.SCULK_CRACKING_SYNTHESIZER.get())
                        .define('0', ECBlocks.CONTAINER.get())
                        .patternLayer("I")
                        .patternLayer("0"));
        elementProduction.entry(ECBlocks.AIR_MILL_SYNTHESIZER.get())
                .advancement(ElementalCraftApi.identifier("main/container"))
                .turnIn(ElementalCraftApi.identifier("pickup/air_mill_synthesizer"))
                .page(PageBuilder.text("elementalcraft.page.air_mill_synthesizer0"))
                .page(PageBuilder.crafting(ECBlocks.AIR_MILL_SYNTHESIZER.get()))
                .page(PageBuilder.multiblock()
                        .define('M', ECBlocks.AIR_MILL_SYNTHESIZER.get().defaultBlockState()
                                .setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER))
                        .define('I', ECBlocks.AIR_MILL_SYNTHESIZER.get().defaultBlockState()
                                .setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER))
                        .define('0', ECBlocks.CONTAINER.get())
                        .patternLayer("M")
                        .patternLayer("I")
                        .patternLayer("0"));
    }

    private static void generatePipes(BookBuilder book) {
        var pipes = book.category("pipes")
                .icon(ECBlocks.PIPE.get());

        pipes.entry(ECBlocks.PIPE.get())
                .priority()
                .advancement(ElementalCraftApi.identifier("main/contained_crystal"))
                .turnIn(ElementalCraftApi.identifier("main/rudimentary_pipe"))
                .page(PageBuilder.text("elementalcraft.page.element_pipe0"))
                .page(PageBuilder.crafting(ECBlocks.PIPE_RUDIMENTARY.get(), ECBlocks.PIPE.get()))
                .page(PageBuilder.crafting(ECBlocks.PIPE_IMPROVED.get()));
        pipes.entry(ECItems.COVER_FRAME.get())
                .advancement(ElementalCraftApi.identifier("main/drenched_iron"))
                .turnIn(ElementalCraftApi.identifier("pickup/cover_frame"))
                .page(PageBuilder.text("elementalcraft.page.cover_frame0"))
                .page(PageBuilder.crafting(ECItems.COVER_FRAME.get()));
        pipes.entry(PipeUpgradeTypes.PIPE_PRIORITY_RINGS.get())
                .advancement(ElementalCraftApi.identifier("pickup/cover_frame"))
                .turnIn(ElementalCraftApi.identifier("pickup/pipe_priority_rings"))
                .page(PageBuilder.text("elementalcraft.page.pipe_priority_rings0"))
                .page(PageBuilder.crafting(PipeUpgradeTypes.PIPE_PRIORITY_RINGS.get()));
        pipes.entry(PipeUpgradeTypes.ELEMENT_VALVE.get())
                .advancement(ElementalCraftApi.identifier("pickup/cover_frame"))
                .turnIn(ElementalCraftApi.identifier("pickup/element_valve"))
                .page(PageBuilder.text("elementalcraft.page.element_valve0"))
                .page(PageBuilder.crafting(PipeUpgradeTypes.ELEMENT_VALVE.get()));
        pipes.entry(PipeUpgradeTypes.ELEMENT_BEAM.get())
                .advancement(ElementalCraftApi.identifier("pickup/cover_frame"))
                .turnIn(ElementalCraftApi.identifier("pickup/element_beam"))
                .page(PageBuilder.text("elementalcraft.page.element_beam0"))
                .page(PageBuilder.crafting(PipeUpgradeTypes.ELEMENT_BEAM.get()));
        pipes.entry(PipeUpgradeTypes.ELEMENT_PUMP.get())
                .advancement(ElementalCraftApi.identifier("pickup/cover_frame"))
                .turnIn(ElementalCraftApi.identifier("pickup/element_pump"))
                .page(PageBuilder.text("elementalcraft.page.element_pump0"))
                .page(PageBuilder.crafting(PipeUpgradeTypes.ELEMENT_PUMP.get()));
    }

    private static void generateInstruments(BookBuilder book) {
        var instruments = book.category("instruments")
                .icon(ECBlocks.INFUSER.get());

        instruments.entry(ECBlocks.INFUSER.get())
                .priority()
                .advancement(ElementalCraftApi.identifier("main/small_container"))
                .turnIn(ElementalCraftApi.identifier("main/infuser"))
                .page(PageBuilder.text("elementalcraft.page.infuser0"))
                .page(PageBuilder.crafting(ECBlocks.INFUSER.get()))
                .page(PageBuilder.multiblock()
                        .define('I', ECBlocks.INFUSER.get())
                        .define('0', ECBlocks.CONTAINER.get())
                        .patternLayer("I")
                        .patternLayer("0"));
        instruments.entry(ECBlocks.BINDER.get())
                .priority()
                .advancement(ElementalCraftApi.identifier("main/container"))
                .turnIn(ElementalCraftApi.identifier("main/binder"))
                .page(PageBuilder.text("elementalcraft.page.binder0"))
                .page(PageBuilder.crafting(ECBlocks.BINDER.get()))
                .page(PageBuilder.multiblock()
                        .define('I', ECBlocks.BINDER.get())
                        .define('0', ECBlocks.CONTAINER.get())
                        .patternLayer("I")
                        .patternLayer("0"));
        instruments.entry(ECBlocks.BINDER_IMPROVED.get())
                .advancement(ElementalCraftApi.identifier("main/binder"))
                .turnIn(ElementalCraftApi.identifier("pickup/binder_improved"))
                .page(PageBuilder.text("elementalcraft.page.binder_improved0"))
                .page(PageBuilder.crafting(ECBlocks.BINDER_IMPROVED.get()))
                .page(PageBuilder.multiblock()
                        .define('I', ECBlocks.BINDER_IMPROVED.get())
                        .define('0', ECBlocks.CONTAINER.get())
                        .patternLayer("I")
                        .patternLayer("0"));
        instruments.entry(ECBlocks.CRYSTALLIZER.get())
                .priority()
                .advancement(ElementalCraftApi.identifier("main/binder"))
                .turnIn(ElementalCraftApi.identifier("main/crystallizer"))
                .page(PageBuilder.text("elementalcraft.page.crystallizer0"))
                .page(PageBuilder.crafting(ECBlocks.CRYSTALLIZER.get()))
                .page(PageBuilder.multiblock()
                        .define('I', ECBlocks.CRYSTALLIZER.get())
                        .define('0', ECBlocks.CONTAINER.get())
                        .patternLayer("I")
                        .patternLayer("0"));
        instruments.entry(ECBlocks.INSCRIBER.get())
                .advancement(ElementalCraftApi.identifier("main/container"))
                .turnIn(ElementalCraftApi.identifier("main/inscriber"))
                .page(PageBuilder.text("elementalcraft.page.inscriber0"))
                .page(PageBuilder.crafting(ECBlocks.INSCRIBER.get()))
                .page(PageBuilder.multiblock()
                        .define('I', ECBlocks.INSCRIBER.get())
                        .define('0', ECBlocks.CONTAINER.get())
                        .patternLayer("I")
                        .patternLayer("0"));
        instruments.entry(ECBlocks.FIRE_FURNACE.get())
                .advancement(ElementalCraftApi.identifier("main/small_container"))
                .turnIn(ElementalCraftApi.identifier("pickup/firefurnace"))
                .page(PageBuilder.text("elementalcraft.page.firefurnace0"))
                .page(PageBuilder.crafting(ECBlocks.FIRE_FURNACE.get()))
                .page(PageBuilder.multiblock()
                        .define('I', ECBlocks.FIRE_FURNACE.get())
                        .define('0', ECBlocks.CONTAINER.get())
                        .patternLayer("I")
                        .patternLayer("0"));
        instruments.entry(ECBlocks.FIRE_BLAST_FURNACE.get())
                .advancement(ElementalCraftApi.identifier("main/container"))
                .turnIn(ElementalCraftApi.identifier("pickup/fireblastfurnace"))
                .page(PageBuilder.text("elementalcraft.page.fireblastfurnace0"))
                .page(PageBuilder.crafting(ECBlocks.FIRE_BLAST_FURNACE.get()))
                .page(PageBuilder.multiblock()
                        .define('I', ECBlocks.FIRE_BLAST_FURNACE.get())
                        .define('0', ECBlocks.CONTAINER.get())
                        .patternLayer("I")
                        .patternLayer("0"));
        instruments.entry(ECBlocks.WATER_MILL_WOOD_SAW.get())
                .advancement(ElementalCraftApi.identifier("main/small_container"))
                .turnIn(ElementalCraftApi.identifier("main/water_mill_wood_saw"))
                .page(PageBuilder.text("elementalcraft.page.water_mill_wood_saw0"))
                .page(PageBuilder.crafting(ECBlocks.WATER_MILL_WOOD_SAW.get()))
                .page(PageBuilder.multiblock()
                        .define('I', ECBlocks.WATER_MILL_WOOD_SAW.get())
                        .define('0', ECBlocks.CONTAINER.get())
                        .patternLayer("I")
                        .patternLayer("0"));
        instruments.entry(ECBlocks.WATER_MILL_GRINDSTONE.get())
                .advancement(ElementalCraftApi.identifier("main/small_container"))
                .turnIn(ElementalCraftApi.identifier("main/water_mill_grindstone"))
                .page(PageBuilder.text("elementalcraft.page.water_mill_grindstone0"))
                .page(PageBuilder.crafting(ECBlocks.WATER_MILL_GRINDSTONE.get()))
                .page(PageBuilder.multiblock()
                        .define('I', ECBlocks.WATER_MILL_GRINDSTONE.get())
                        .define('0', ECBlocks.CONTAINER.get())
                        .patternLayer("I")
                        .patternLayer("0"));
        instruments.entry(ECBlocks.AIR_MILL_WOOD_SAW.get())
                .advancement(ElementalCraftApi.identifier("main/container"))
                .turnIn(ElementalCraftApi.identifier("main/air_mill_wood_saw"))
                .page(PageBuilder.text("elementalcraft.page.air_mill_wood_saw0"))
                .page(PageBuilder.crafting(ECBlocks.AIR_MILL_WOOD_SAW.get()))
                .page(PageBuilder.multiblock()
                        .define('M', ECBlocks.AIR_MILL_WOOD_SAW.get().defaultBlockState()
                                .setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER))
                        .define('I', ECBlocks.AIR_MILL_WOOD_SAW.get().defaultBlockState()
                                .setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER))
                        .define('0', ECBlocks.CONTAINER.get())
                        .patternLayer("M")
                        .patternLayer("I")
                        .patternLayer("0"));
        instruments.entry(ECBlocks.AIR_MILL_GRINDSTONE.get())
                .advancement(ElementalCraftApi.identifier("main/container"))
                .turnIn(ElementalCraftApi.identifier("main/air_mill_grindstone"))
                .page(PageBuilder.text("elementalcraft.page.air_mill_grindstone0"))
                .page(PageBuilder.crafting(ECBlocks.AIR_MILL_GRINDSTONE.get()))
                .page(PageBuilder.multiblock()
                        .define('M', ECBlocks.AIR_MILL_GRINDSTONE.get().defaultBlockState()
                                .setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER))
                        .define('I', ECBlocks.AIR_MILL_GRINDSTONE.get().defaultBlockState()
                                .setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER))
                        .define('0', ECBlocks.CONTAINER.get())
                        .patternLayer("M")
                        .patternLayer("I")
                        .patternLayer("0"));
        instruments.entry(ECBlocks.ENCHANTMENT_LIQUEFIER.get())
                .advancement(ElementalCraftApi.identifier("main/container"))
                .turnIn(ElementalCraftApi.identifier("pickup/enchantment_liquefier"))
                .page(PageBuilder.text("elementalcraft.page.enchantment_liquefier0"))
                .page(PageBuilder.crafting(ECBlocks.ENCHANTMENT_LIQUEFIER.get()))
                .page(PageBuilder.multiblock()
                        .define('U', ECBlocks.ENCHANTMENT_LIQUEFIER.get().defaultBlockState()
                                .setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER))
                        .define('I', ECBlocks.ENCHANTMENT_LIQUEFIER.get().defaultBlockState()
                                .setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER))
                        .define('0', ECBlocks.CONTAINER.get())
                        .patternLayer("U")
                        .patternLayer("I")
                        .patternLayer("0"));
        instruments.entry(ECBlocks.PURIFIER.get())
                .advancement(ElementalCraftApi.identifier("main/container"))
                .turnIn(ElementalCraftApi.identifier("main/purifier"))
                .page(PageBuilder.text("elementalcraft.page.purifier0"))
                .page(PageBuilder.crafting(ECBlocks.PURIFIER.get()))
                .page(PageBuilder.multiblock()
                        .define('I', ECBlocks.PURIFIER.get())
                        .define('0', ECBlocks.CONTAINER.get())
                        .patternLayer("I")
                        .patternLayer("0"));
        instruments.entry(ECBlocks.DIFFUSER.get())
                .advancement(ElementalCraftApi.identifier("main/container"))
                .turnIn(ElementalCraftApi.identifier("main/diffuser"))
                .page(PageBuilder.text("elementalcraft.page.diffuser0"))
                .page(PageBuilder.crafting(ECBlocks.DIFFUSER.get()))
                .page(PageBuilder.multiblock()
                        .define('I', ECBlocks.DIFFUSER.get())
                        .define('0', ECBlocks.CONTAINER.get())
                        .patternLayer("I")
                        .patternLayer("0"));
    }

    private static void generateAdvanced(BookBuilder book) {
        var advanced = book.category("advanced")
                .icon(ECBlocks.PURE_INFUSER.get());

        advanced.entry("element_holders")
                .icon(ECItems.FIRE_HOLDER.get())
                .advancement(ElementalCraftApi.identifier("main/small_container"))
                .turnIn(ElementalCraftApi.identifier("main/element_holders"))
                .page(PageBuilder.text("elementalcraft.page.element_holders0"))
                .page(PageBuilder.crafting(ECItems.FIRE_HOLDER.get(), ECItems.WATER_HOLDER.get()))
                .page(PageBuilder.crafting(ECItems.EARTH_HOLDER.get(), ECItems.AIR_HOLDER.get()));
        advanced.entry(ECItems.PURE_HOLDER.get())
                .advancement(ElementalCraftApi.identifier("main/element_holders"))
                .turnIn(ElementalCraftApi.identifier("main/pure_element_holder"))
                .page(PageBuilder.text("elementalcraft.page.pure_element_holder0"));
        advanced.entry("runes")
                .icon(ECItems.RUNE.get().getRuneStackTemplate(Runes.TANO))
                .advancement(ElementalCraftApi.identifier("main/inscriber"))
                .turnIn(ElementalCraftApi.identifier("pickup/rune"))
                .page(PageBuilder.text("elementalcraft.page.runes0"))
                .page(PageBuilder.crafting(ECItems.MINOR_RUNE_SLATE.get(), ECItems.RUNE_SLATE.get()))
                .page(PageBuilder.crafting(ECItems.MAJOR_RUNE_SLATE.get()));
        advanced.entry(ECBlocks.RETRIEVER.get())
                .advancement(ElementalCraftApi.identifier("main/infuser"))
                .turnIn(ElementalCraftApi.identifier("pickup/instrument_retriever"))
                .page(PageBuilder.text("elementalcraft.page.instrument_retriever0"))
                .page(PageBuilder.crafting(ECBlocks.RETRIEVER.get()));
        advanced.entry(ECBlocks.ORDERED_SORTER.get())
                .advancement(ElementalCraftApi.identifier("main/binder"))
                .turnIn(ElementalCraftApi.identifier("pickup/ordered_sorter"))
                .page(PageBuilder.text("elementalcraft.page.ordered_sorter0"))
                .page(PageBuilder.crafting(ECBlocks.ORDERED_SORTER.get()));
        advanced.entry(ECItems.ELEMENTAL_FIREFUEL.get())
                .advancement(ElementalCraftApi.identifier("main/binder"))
                .turnIn(ElementalCraftApi.identifier("pickup/elemental_firefuel"))
                .page(PageBuilder.text("elementalcraft.page.elemental_firefuel0"));
        advanced.entry("reservoirs")
                .icon(ECBlocks.FIRE_RESERVOIR.get())
                .advancement(ElementalCraftApi.identifier("main/container"))
                .turnIn(ElementalCraftApi.identifier("main/reservoirs"))
                .page(PageBuilder.text("elementalcraft.page.reservoirs0"));
        advanced.entry(ECBlocks.PURE_ROCK.get())
                .advancement(ElementalCraftApi.identifier("main/pure_crystal"))
                .turnIn(ElementalCraftApi.identifier("pickup/purerock"))
                .page(PageBuilder.text("elementalcraft.page.purerock0"))
                .page(PageBuilder.crafting(ECBlocks.PURE_ROCK_STAIRS.get(), ECBlocks.PURE_ROCK_SLAB.get()))
                .page(PageBuilder.crafting(ECBlocks.PURE_ROCK_WALL.get()));
    }

    private static void generateShrines(BookBuilder book) {
        var shrines = book.category("shrines")
                .icon(ECItems.SHRINE_BASE.get());

        shrines.entry("using_shrines")
                .priority()
                .icon(ECItems.SHRINE_BASE.get())
                .advancement(ElementalCraftApi.identifier("main/binder"))
                .turnIn(ElementalCraftApi.identifier("pickup/shrinebase"))
                .page(PageBuilder.text("elementalcraft.page.using_shrines0"))
                .page(new CheckPageBuilder(
                        "elementalcraft.page.using_shrines1",
                        "elementalcraft.page.using_shrines2",
                        "elementalcraft.page.using_shrines3",
                        "elementalcraft.page.using_shrines4"));
        shrine(shrines, ECBlocks.FIRE_PYLON);
        shrine(shrines, ECBlocks.VACUUM_SHRINE);
        shrine(shrines, ECBlocks.ORE_SHRINE);
        shrine(shrines, ECBlocks.BREEDING_SHRINE);
        shrine(shrines, ECBlocks.BUDDING_SHRINE);
        shrine(shrines, ECBlocks.ENDER_LOCK_SHRINE);
        shrine(shrines, ECBlocks.GROVE_SHRINE);
        shrine(shrines, ECBlocks.GROWTH_SHRINE);
        shrine(shrines, ECBlocks.HARVEST_SHRINE);
        shrine(shrines, ECBlocks.MELTING_SHRINE);
        shrine(shrines, ECBlocks.LUMBER_SHRINE);
        shrine(shrines, ECBlocks.OVERLOAD_SHRINE);
        shrine(shrines, ECBlocks.SPAWNING_SHRINE);
        shrine(shrines, ECBlocks.SPRING_SHRINE);
        shrine(shrines, ECBlocks.SWEET_SHRINE);
    }

    private static void generateShrineUpgrades(BookBuilder book) {
        var shrineUpgrades = book.category("shrine_upgrades")
                .icon(ECBlocks.OPTIMIZATION_SHRINE_UPGRADE.get());

        shrineUpgrade(shrineUpgrades, ECBlocks.ACCELERATION_SHRINE_UPGRADE);
        shrineUpgrade(shrineUpgrades, ECBlocks.BONELESS_GROWTH_SHRINE_UPGRADE);
        shrineUpgrade(shrineUpgrades, ECBlocks.CAPACITY_SHRINE_UPGRADE);
        shrineUpgrade(shrineUpgrades, ECBlocks.CRYSTAL_GROWTH_SHRINE_UPGRADE);
        shrineUpgrade(shrineUpgrades, ECBlocks.CRYSTAL_HARVEST_SHRINE_UPGRADE);
        shrineUpgrade(shrineUpgrades, ECBlocks.EFFICIENCY_SHRINE_UPGRADE);
        shrineUpgrade(shrineUpgrades, ECBlocks.FILLING_SHRINE_UPGRADE);
        shrineUpgrade(shrineUpgrades, ECBlocks.FORTUNE_SHRINE_UPGRADE);
        shrineUpgrade(shrineUpgrades, ECBlocks.MYSTICAL_GROVE_SHRINE_UPGRADE)
                .ignoreValidation();
        shrineUpgrade(shrineUpgrades, ECBlocks.NECTAR_SHRINE_UPGRADE);
        shrineUpgrade(shrineUpgrades, ECBlocks.OPTIMIZATION_SHRINE_UPGRADE);
        shrineUpgrade(shrineUpgrades, ECBlocks.PICKUP_SHRINE_UPGRADE);
        shrineUpgrade(shrineUpgrades, ECBlocks.PLANTING_SHRINE_UPGRADE);
        shrineUpgrade(shrineUpgrades, ECBlocks.PROTECTION_SHRINE_UPGRADE);
        shrineUpgrade(shrineUpgrades, ECBlocks.RANGE_SHRINE_UPGRADE);
        shrineUpgrade(shrineUpgrades, ECBlocks.SILK_TOUCH_SHRINE_UPGRADE);
        shrineUpgrade(shrineUpgrades, ECBlocks.SPRINGALINE_SHRINE_UPGRADE);
        shrineUpgrade(shrineUpgrades, ECBlocks.CERTUS_QUARTZ_SHRINE_UPGRADE)
                .ignoreValidation();
        shrineUpgrade(shrineUpgrades, ECBlocks.STEM_POLLINATION_SHRINE_UPGRADE);
        shrineUpgrade(shrineUpgrades, ECBlocks.STRENGTH_SHRINE_UPGRADE);
        shrineUpgrade(shrineUpgrades, ECBlocks.VORTEX_SHRINE_UPGRADE);

        shrineUpgrade(shrineUpgrades, ECBlocks.GREATER_FORTUNE_SHRINE_UPGRADE);
        shrineUpgrade(shrineUpgrades, ECBlocks.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE);
        shrineUpgrade(shrineUpgrades, ECBlocks.OVERWHELMING_STRENGTH_SHRINE_UPGRADE);
        shrineUpgrade(shrineUpgrades, ECBlocks.TRANSLOCATION_SHRINE_UPGRADE);
    }

    private static void generateSpells(BookBuilder book) {
        var spells = book.category("spells")
                .icon("textures/gui/entries/spells.png");

        spells.entry(ECBlocks.SPELL_DESK.get())
                .priority()
                .advancement(ElementalCraftApi.identifier("main/infuser"))
                .turnIn(ElementalCraftApi.identifier("pickup/spell_desk"))
                .page(PageBuilder.text("elementalcraft.page.spell_desk0"))
                .page(PageBuilder.crafting(ECBlocks.SPELL_DESK.get()));
        spells.entry("casting_spells")
                .priority()
                .icon("textures/gui/entries/spells.png")
                .advancement(ElementalCraftApi.identifier("pickup/spell_desk"))
                .turnIn(ElementalCraftApi.identifier("pickup/spell_desk"))
                .page(PageBuilder.text("elementalcraft.page.casting_spells0"))
                .page(new CheckPageBuilder(
                        "elementalcraft.page.casting_spells1",
                        "elementalcraft.page.casting_spells2",
                        "elementalcraft.page.casting_spells3",
                        "elementalcraft.page.casting_spells4"));
        spells.entry(ECItems.FOCUS.get())
                .advancement(ElementalCraftApi.identifier("pickup/spell_desk"))
                .turnIn(ElementalCraftApi.identifier("pickup/focus"))
                .page(PageBuilder.text("elementalcraft.page.focus0"))
                .page(PageBuilder.crafting(ECItems.FOCUS.get()));
        spells.entry(ECItems.STAFF.get())
                .advancement(ElementalCraftApi.identifier("pickup/focus"))
                .turnIn(ElementalCraftApi.identifier("pickup/staff"))
                .page(PageBuilder.text("elementalcraft.page.staff0"))
                .page(PageBuilder.crafting(ECItems.STAFF.get()));
        spells.entry(ECItems.SPELL_BOOK.get())
                .advancement(ElementalCraftApi.identifier("pickup/focus"))
                .turnIn(ElementalCraftApi.identifier("pickup/spell_book"))
                .page(PageBuilder.text("elementalcraft.page.spell_book0"))
                .page(PageBuilder.crafting(ECItems.SPELL_BOOK.get()));
        spell(spells, Spells.LIGHT);
        spell(spells, Spells.AIR_SHIELD);
        spell(spells, Spells.ANIMAL_GROWTH);
        spell(spells, Spells.DASH);
        spell(spells, Spells.ENDER_STRIKE);
        spell(spells, Spells.FEATHER_SPIKES);
        spell(spells, Spells.FIRE_BALL);
        spell(spells, Spells.FLAME_CLEAVE);
        spell(spells, Spells.GRAVEL_FALL);
        spell(spells, Spells.HEAL);
        spell(spells, Spells.INFERNO);
        spell(spells, Spells.ITEM_PULL);
        spell(spells, Spells.PURIFICATION);
        spell(spells, Spells.REPAIR);
        spell(spells, Spells.RIPENING);
        spell(spells, Spells.SHOCKWAVE);
        spell(spells, Spells.SILK_VEIN);
        spell(spells, Spells.SPEED);
        spell(spells, Spells.STONE_WALL);
        spell(spells, Spells.TREE_FALL);
        spell(spells, Spells.TRANSLOCATION);
        spells.entry(ECBlocks.TRANSLOCATION_ANCHOR.get())
                .advancement(ElementalCraftApi.identifier("pickup/fireite_ingot"))
                .turnIn(ElementalCraftApi.identifier("pickup/translocation_anchor"))
                .page(PageBuilder.text("elementalcraft.page.translocation_anchor0"))
                .page(PageBuilder.crafting(ECBlocks.TRANSLOCATION_ANCHOR.get()));
    }

    private static void generateJewels(BookBuilder book) {
        var jewels = book.category("jewels")
                .icon(Jewels.PHOENIX.get());

        jewel(jewels, Jewels.ARCTIC_HARE);
        jewel(jewels, Jewels.BASILISK);
        jewel(jewels, Jewels.BEAR);
        jewel(jewels, Jewels.DEMIGOD);
        jewel(jewels, Jewels.DOLPHIN);
        jewel(jewels, Jewels.HAWK);
        jewel(jewels, Jewels.KIRIN);
        jewel(jewels, Jewels.LEOPARD);
        jewel(jewels, Jewels.MOLE);
        jewel(jewels, Jewels.PHOENIX);
        jewel(jewels, Jewels.PIGLIN);
        jewel(jewels, Jewels.SALMON);
        jewel(jewels, Jewels.WATER_STRIDER);
        jewel(jewels, Jewels.STRIDER);
        jewel(jewels, Jewels.TIGER);
        jewel(jewels, Jewels.TORTOISE);
        jewel(jewels, Jewels.VIPER);
    }


    private static void shrine(CategoryBuilder category, DeferredHolder<Block, ? extends AbstractShrineBlock<?>> shrine) {
        var name = shrine.getId().getPath();

        category.entry(shrine.get())
                .advancement(ElementalCraftApi.identifier("pickup/shrinebase"))
                .turnIn(ElementalCraftApi.identifier("pickup/" + name))
                .page(PageBuilder.text("elementalcraft.page." + name + "0"));
    }

    private static EntryBuilder shrineUpgrade(CategoryBuilder category, DeferredHolder<Block, ? extends ShrineUpgradeBlock> shrineUpgrade) {
        var name = shrineUpgrade.getId().getPath();

        return category.entry(shrineUpgrade.get())
                .advancement(ElementalCraftApi.identifier("pickup/shrinebase"))
                .turnIn(ElementalCraftApi.identifier("pickup/" + name))
                .page(PageBuilder.text("elementalcraft.page." + name + "0"))
                .page(PageBuilder.crafting(shrineUpgrade.get()));
    }

    private static void spell(CategoryBuilder category, DeferredHolder<Spell, ? extends Spell> spell) {
        var name = spell.getId().getPath();

        category.entry(name)
                .name(spell.get().getDescriptionId())
                .icon(spell.get().createItemStackTemplate())
                .advancement(ElementalCraftApi.identifier("pickup/spell_desk"))
                .turnIn(ElementalCraftApi.identifier("pickup/scroll"))
                .page(PageBuilder.text("elementalcraft.page." + name + "0"));
    }

    private static void jewel(CategoryBuilder category, DeferredHolder<Jewel, ? extends Jewel> jewel) {
        var name = jewel.getId().getPath();

        category.entry(jewel.get())
                .advancement(ElementalCraftApi.identifier("pickup/unset_jewel"))
                .turnIn(ElementalCraftApi.identifier("pickup/" + name))
                .page(PageBuilder.text("elementalcraft.page." + name + "0"));
    }

    private CompletableFuture<?> save(CachedOutput cache, HolderLookup.Provider registries, BookBuilder book) {
        book.validate();

        var categoryCodec = CategoryBuilder.codec(registries);
        var entryCodec = EntryBuilder.codec(registries);

        return DataProvider.saveStable(cache, registries, BookBuilder.CODEC, book, getPath(book))
                .thenCompose(_ -> CompletableFuture.allOf(book.getCategories().stream()
                        .map(category -> DataProvider.saveStable(cache, registries, categoryCodec, category, getPath(category))
                                .thenCompose(_ -> CompletableFuture.allOf(category.getEntries().stream()
                                        .map(entry -> DataProvider.saveStable(cache, registries, entryCodec, entry, getPath(entry)))
                                        .toArray(CompletableFuture[]::new))))
                        .toArray(CompletableFuture[]::new)));
    }

    private Path getPath(PatchouliFile file) {
        return this.packOutput.getOutputFolder().resolve(file.getPath());
    }

    @Override
    public String getName() {
        return "Elemental Craft Patchouli Book";
    }
}
