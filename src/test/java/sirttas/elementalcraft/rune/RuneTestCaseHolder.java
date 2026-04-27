package sirttas.elementalcraft.rune;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.neoforged.testframework.Test;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.block.extractor.ElementExtractorGameTests;
import sirttas.elementalcraft.block.instrument.InstrumentTestTemplates;
import sirttas.elementalcraft.block.instrument.io.mill.MillTestCaseHolder;
import sirttas.elementalcraft.block.synthesizer.cracking.CrackingSynthesizerGameTests;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public record RuneTestCaseHolder(
        String template,
        BlockPos pos,
        Direction side,
        List<ResourceKey<Rune>> runes
) {

    public static final List<RuneTestCaseHolder> HOLDERS = Stream.concat(Stream.of(
            of(ElementExtractorGameTests.RUDIMENTARY_EXTRACTOR_WITH_RUNES_TEMPLATE_NAME, Runes.ZOD),
            of(ElementExtractorGameTests.EXTRACTOR_WITH_RUNES_TEMPLATE_NAME, Runes.ZOD, Runes.ZOD),
            of(ElementExtractorGameTests.IMPROVED_EXTRACTOR_WITH_RUNES_TEMPLATE_NAME, Runes.ZOD, Runes.ZOD, Runes.ZOD),
            of(CrackingSynthesizerGameTests.CRACKING_SYNTHESIZER_WITH_RUNE_TEMPLATE_NAME, new BlockPos(6, 2, 6), Runes.TYRIA),
            of(InstrumentTestTemplates.INSCRIBER_TEMPLATE_NAME, Runes.MEWTWO),
            of(InstrumentTestTemplates.INFUSER_TEMPLATE_NAME, Runes.CREATIVE),
            of(InstrumentTestTemplates.BINDER_TEMPLATE_NAME, Runes.CREATIVE),
            of(InstrumentTestTemplates.CRYSTALLIZER_TEMPLATE_NAME, Runes.CREATIVE),
            of(InstrumentTestTemplates.ORE_PURIFIER_TEMPLATE_NAME, Runes.CREATIVE),
            of(InstrumentTestTemplates.FIRE_FURNACE_TEMPLATE_NAME, Runes.CREATIVE),
            of(InstrumentTestTemplates.FIRE_BLAST_FURNACE_TEMPLATE_NAME, Runes.CREATIVE),
            of("chiselgametests.sorter_with_rune", new BlockPos(0, 0, 0), Runes.ZOD),
            of(InstrumentTestTemplates.ENCHANTMENT_LIQUEFIER_TEMPLATE_NAME, Runes.CREATIVE),
            of(InstrumentTestTemplates.ENCHANTMENT_LIQUEFIER_TEMPLATE_NAME, new BlockPos(0, 2, 0), Runes.CREATIVE),
            of("sourcebreedergametests.source_breeder", new BlockPos(0, 0, 2), Runes.CREATIVE),
            of("pureinfusergametests.pure_infuser", new BlockPos(3, 0, 3), Runes.CREATIVE),
            of("greaterfortuneshrineupgradegametests.should_increaseoreloot", new BlockPos(12, 1, 13), Runes.TZEENTCH),
            of("elementpumpgametests.should_transfer6250elements", new BlockPos(0, 1, 1), Direction.NORTH, Runes.ZOD, Runes.ZOD, Runes.ZOD)
        ), MillTestCaseHolder.HOLDERS.stream()
            .map(MillTestCaseHolder::template)
            .distinct()
            .map(t -> of(t, Runes.CREATIVE))
    ).toList();

    public static final String GROUP_NAME = "rune";

    @SafeVarargs
    public static RuneTestCaseHolder of(String template, BlockPos pos, Direction side, ResourceKey<Rune>... runes) {
        return new RuneTestCaseHolder(template, pos, side, List.of(runes));
    }

    @SafeVarargs
    public static RuneTestCaseHolder of(String template, BlockPos pos, ResourceKey<Rune>... runes) {
        return of(template, pos, null, runes);
    }

    @SafeVarargs
    public static RuneTestCaseHolder of(String template, ResourceKey<Rune>... runes) {
        return of(template, new BlockPos(0, 1, 0), runes);
    }

    public Test createTest(String name, String description, BiConsumer<ECGameTestHelper, RuneTestCaseHolder> function) {
        return ECGameTestUtils.createTest(GROUP_NAME, name, description, template, h -> function.accept(h, this));
    }
}
