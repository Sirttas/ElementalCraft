package sirttas.elementalcraft.block.extractor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.testframework.DynamicTest;
import net.neoforged.testframework.Test;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.RegisterStructureTemplate;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;
import sirttas.elementalcraft.block.sorter.ISorterBlock;
import sirttas.elementalcraft.block.source.SourceBlockEntity;
import sirttas.elementalcraft.block.source.SourceElementStorage;
import sirttas.elementalcraft.block.source.trait.SourceTraitTestHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.rune.Runes;
import sirttas.elementalcraft.template.ECStructureTemplateBuilder;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static sirttas.elementalcraft.template.StructureTemplateNbtHelper.runeHandler;
import static sirttas.elementalcraft.template.StructureTemplateNbtHelper.withValue;

@ForEachTest(groups = ElementExtractorGameTests.GROUP)
public class ElementExtractorGameTests {
    public static final String GROUP = "level.blocks.extractors";

    public static final String RUDIMENTARY_EXTRACTOR_TEMPLATE_NAME = "elementalcraft:rudimentary_extractor";
    public static final String RUDIMENTARY_EXTRACTOR_WITH_RUNES_TEMPLATE_NAME = "elementalcraft:rudimentary_extractor_with_runes";
    public static final String EXTRACTOR_TEMPLATE_NAME = "elementalcraft:extractor";
    public static final String EXTRACTOR_WITH_RUNES_TEMPLATE_NAME = "elementalcraft:extractor_with_runes";
    public static final String IMPROVED_EXTRACTOR_TEMPLATE_NAME = "elementalcraft:improved_extractor";
    public static final String IMPROVED_EXTRACTOR_WITH_RUNES_TEMPLATE_NAME = "elementalcraft:improved_extractor_with_runes";

    @RegisterStructureTemplate(RUDIMENTARY_EXTRACTOR_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> RUDIMENTARY_EXTRACTOR_TEMPLATE = createExtractorTemplate(ECBlocks.RUDIMENTARY_EXTRACTOR);
    @RegisterStructureTemplate(RUDIMENTARY_EXTRACTOR_WITH_RUNES_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> RUDIMENTARY_EXTRACTOR_WITH_RUNES_TEMPLATE = createExtractorTemplate(ECBlocks.RUDIMENTARY_EXTRACTOR, Runes.ZOD);
    @RegisterStructureTemplate(EXTRACTOR_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> EXTRACTOR_TEMPLATE = createExtractorTemplate(ECBlocks.EXTRACTOR);
    @RegisterStructureTemplate(EXTRACTOR_WITH_RUNES_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> EXTRACTOR_WITH_RUNES_TEMPLATE = createExtractorTemplate(ECBlocks.EXTRACTOR, Runes.ZOD, Runes.ZOD);
    @RegisterStructureTemplate(IMPROVED_EXTRACTOR_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> IMPROVED_EXTRACTOR_TEMPLATE = createExtractorTemplate(ECBlocks.IMPROVED_EXTRACTOR);
    @RegisterStructureTemplate(IMPROVED_EXTRACTOR_WITH_RUNES_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> IMPROVED_EXTRACTOR_WITH_RUNES_TEMPLATE = createExtractorTemplate(ECBlocks.IMPROVED_EXTRACTOR, Runes.ZOD, Runes.ZOD, Runes.ZOD);

    @SafeVarargs
    private static Supplier<StructureTemplate> createExtractorTemplate(Supplier<? extends AbstractElementExtractorBlock> extractor, ResourceKey<Rune>... runes) {
        return ECStructureTemplateBuilder.lazy(1, 3, 2, builder -> {
            var sourceTag = new CompoundTag();

            sourceTag.put(ECNames.SOURCE_TRAITS_HOLDER, SourceTraitTestHelper.createDefaultTraits());

            return builder.placeFloorLever(0, 1, 1, true)
                    .set(0, 0, 0, ECBlocks.CONTAINER.get().defaultBlockState())
                    .set(0, 1, 0, extractor.get().defaultBlockState(), withValue(runeHandler(runes)))
                    .set(0, 2, 0, ECBlocks.FIRE_SOURCE.get().defaultBlockState(), sourceTag);
        });
    }

    public static Collection<Test> collectTests() {
        var i = 0;
        var j = 0;

        return List.of(
                ECGameTestUtils.createTest(
                        GROUP,
                        "ElementExtractorGameTests.should_extractElementFromSource_" + i++,
                        "Check if a rudimentary extractor extracts elements from a source.",
                        RUDIMENTARY_EXTRACTOR_TEMPLATE_NAME,
                        h -> should_extractElementFromSource(h, 5)),
                ECGameTestUtils.createTest(
                        GROUP,
                        "ElementExtractorGameTests.should_extractElementFromSource_" + i++,
                        "Check if a rudimentary extractor with runes extracts elements from a source.",
                        RUDIMENTARY_EXTRACTOR_WITH_RUNES_TEMPLATE_NAME,
                        h -> should_extractElementFromSource(h, 8)),
                ECGameTestUtils.createTest(
                        GROUP,
                        "ElementExtractorGameTests.should_extractElementFromSource_" + i++,
                        "Check if an extractor extracts elements from a source.",
                        EXTRACTOR_TEMPLATE_NAME,
                        h -> should_extractElementFromSource(h, 25)),
                ECGameTestUtils.createTest(
                        GROUP,
                        "ElementExtractorGameTests.should_extractElementFromSource_" + i++,
                        "Check if an extractor with runes extracts elements from a source.",
                        EXTRACTOR_WITH_RUNES_TEMPLATE_NAME,
                        h -> should_extractElementFromSource(h, 50)),
                ECGameTestUtils.createTest(
                        GROUP,
                        "ElementExtractorGameTests.should_extractElementFromSource_" + i++,
                        "Check if an improved extractor extracts elements from a source.",
                        IMPROVED_EXTRACTOR_TEMPLATE_NAME,
                        h -> should_extractElementFromSource(h, 100)),
                ECGameTestUtils.createTest(
                        GROUP,
                        "ElementExtractorGameTests.should_extractElementFromSource_" + i++,
                        "Check if an improved extractor with runes extracts elements from a source.",
                        IMPROVED_EXTRACTOR_WITH_RUNES_TEMPLATE_NAME,
                        h -> should_extractElementFromSource(h, 250)),

                ECGameTestUtils.createTest(
                        GROUP,
                        "ElementExtractorGameTests.should_exhaustSource_" + j++,
                        "Check if a rudimentary extractor exhausts a source.",
                        RUDIMENTARY_EXTRACTOR_TEMPLATE_NAME,
                        h -> should_exhaustSource(h, 5)),
                ECGameTestUtils.createTest(
                        GROUP,
                        "ElementExtractorGameTests.should_exhaustSource_" + j++,
                        "Check if a rudimentary extractor with runes exhausts a source.",
                        RUDIMENTARY_EXTRACTOR_WITH_RUNES_TEMPLATE_NAME,
                        h -> should_exhaustSource(h, 8)),
                ECGameTestUtils.createTest(
                        GROUP,
                        "ElementExtractorGameTests.should_exhaustSource_" + j++,
                        "Check if an extractor exhausts a source.",
                        EXTRACTOR_TEMPLATE_NAME,
                        h -> should_exhaustSource(h, 25)),
                ECGameTestUtils.createTest(
                        GROUP,
                        "ElementExtractorGameTests.should_exhaustSource_" + j++,
                        "Check if an extractor with runes exhausts a source.",
                        EXTRACTOR_WITH_RUNES_TEMPLATE_NAME,
                        h -> should_exhaustSource(h, 50)),
                ECGameTestUtils.createTest(
                        GROUP,
                        "ElementExtractorGameTests.should_exhaustSource_" + j++,
                        "Check if an improved extractor exhausts a source.",
                        IMPROVED_EXTRACTOR_TEMPLATE_NAME,
                        h -> should_exhaustSource(h, 100)),
                ECGameTestUtils.createTest(
                        GROUP,
                        "ElementExtractorGameTests.should_exhaustSource_" + j++,
                        "Check if an improved extractor with runes exhausts a source.",
                        IMPROVED_EXTRACTOR_WITH_RUNES_TEMPLATE_NAME,
                        h -> should_exhaustSource(h, 250))
        );
    }

    private static void should_extractElementFromSource(ECGameTestHelper helper, int transferRate) {
        var storage = helper.getBlockEntity(BlockPos.ZERO, ElementContainerBlockEntity.class).getElementStorage();
        var sourceStorage = helper.getBlockEntity(new BlockPos(0, 2, 0), SourceBlockEntity.class).getElementStorage();
        var ticks = new AtomicInteger(0);

        helper.startSequence()
                .thenExecute(() -> helper.pullLever(0, 1, 1))
                .thenIdle(1)
                .thenExecuteFor(10, () -> {
                    var i = ticks.incrementAndGet();

                    assertThat(sourceStorage.getElementAmount(ElementType.FIRE)).isLessThanOrEqualTo(SourceElementStorage.DEFAULT_CAPACITY - (transferRate * i));
                    assertThat(storage.getElementAmount(ElementType.FIRE)).isEqualTo(transferRate * i);
                })
                .thenSucceed();
    }

    private static void should_exhaustSource(ECGameTestHelper helper, int transferRate) {
        var storage = helper.getBlockEntity(BlockPos.ZERO, ElementContainerBlockEntity.class).getElementStorage();
        var sourceStorage = (SourceElementStorage) helper.getBlockEntity(new BlockPos(0, 2, 0), SourceBlockEntity.class).getElementStorage();

        helper.startSequence()
                .thenExecute(() -> sourceStorage.setElementAmount(transferRate))
                .thenExecuteAfter(1, () -> helper.pullLever(0, 1, 1))
                .thenExecuteAfter(5, () -> {
                    helper.assertBlockNotPresent(ECBlocks.FIRE_SOURCE.get(), 0, 2, 0);
                    assertThat(storage.getElementAmount(ElementType.FIRE)).isPositive();
                })
                .thenSucceed();
    }

    @TestHolder(description = "Checks that the source stabilizer gets dropped when the source gets exhausted.")
    @GameTest
    private static void should_dropStabilizer_when_sourceGetExhausted(DynamicTest test) {
        test.registerGameTestTemplate(() -> {
            var sourceTag = new CompoundTag();

            sourceTag.put(ECNames.SOURCE_TRAITS_HOLDER, SourceTraitTestHelper.createDefaultTraits());
            sourceTag.putBoolean(ECNames.STABILIZED, true);
            return ECStructureTemplateBuilder.withSize(1, 3, 2)
                    .placeFloorLever(0, 1, 1, true)
                    .set(0, 0, 0, ECBlocks.CONTAINER.get().defaultBlockState())
                    .set(0, 1, 0, ECBlocks.IMPROVED_EXTRACTOR.get().defaultBlockState())
                    .set(0, 2, 0, ECBlocks.FIRE_SOURCE.get().defaultBlockState(), sourceTag)
                    .unpack();
        });

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var storage = helper.getBlockEntity(BlockPos.ZERO, ElementContainerBlockEntity.class).getElementStorage();
            var sourceStorage = (SourceElementStorage) helper.getBlockEntity(new BlockPos(0, 2, 0), SourceBlockEntity.class).getElementStorage();

            helper.startSequence()
                    .thenExecute(() -> sourceStorage.setElementAmount(100))
                    .thenExecuteAfter(1, () -> helper.pullLever(0, 1, 1))
                    .thenExecuteAfter(5, () -> {
                        helper.assertBlockNotPresent(ECBlocks.FIRE_SOURCE.get(), 0, 2, 0);
                        helper.assertItemEntityPresent(ECItems.SOURCE_STABILIZER.get());
                        assertThat(storage.getElementAmount(ElementType.FIRE)).isGreaterThanOrEqualTo(100);
                    })
                    .thenSucceed();
        });
    }

    @TestHolder(description = "Checks that the source stabilizer gets retrieved by the retriever when the source gets exhausted.")
    @GameTest
    private static void should_retrieveStabilizer_when_sourceGetExhausted(DynamicTest test) {
        test.registerGameTestTemplate(() -> {
            var sourceTag = new CompoundTag();

            sourceTag.put(ECNames.SOURCE_TRAITS_HOLDER, SourceTraitTestHelper.createDefaultTraits());
            sourceTag.putBoolean(ECNames.STABILIZED, true);
            return ECStructureTemplateBuilder.withSize(2, 3, 2)
                    .placeFloorLever(0, 1, 1, true)
                    .set(1, 0, 0, Blocks.CHEST.defaultBlockState())
                    .set(1, 1, 0, ECBlocks.RETRIEVER.get().defaultBlockState()
                            .setValue(ISorterBlock.SOURCE, Direction.WEST)
                            .setValue(ISorterBlock.TARGET, Direction.DOWN))
                    .set(0, 0, 0, ECBlocks.CONTAINER.get().defaultBlockState())
                    .set(0, 1, 0, ECBlocks.IMPROVED_EXTRACTOR.get().defaultBlockState())
                    .set(0, 2, 0, ECBlocks.FIRE_SOURCE.get().defaultBlockState(), sourceTag)
                    .unpack();
        });

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var storage = helper.getBlockEntity(BlockPos.ZERO, ElementContainerBlockEntity.class).getElementStorage();
            var sourceStorage = (SourceElementStorage) helper.getBlockEntity(new BlockPos(0, 2, 0), SourceBlockEntity.class).getElementStorage();

            helper.startSequence()
                    .thenExecute(() -> sourceStorage.setElementAmount(100))
                    .thenExecuteAfter(1, () -> helper.pullLever(0, 1, 1))
                    .thenExecuteAfter(5, () -> {
                        helper.assertBlockNotPresent(ECBlocks.FIRE_SOURCE.get(), 0, 2, 0);
                        helper.assertItemEntityNotPresent(ECItems.SOURCE_STABILIZER.get());
                        helper.assertContainerContains(1, 0, 0, ECItems.SOURCE_STABILIZER.get());
                        assertThat(storage.getElementAmount(ElementType.FIRE)).isGreaterThanOrEqualTo(100);
                    })
                    .thenSucceed();
        });
    }
}
