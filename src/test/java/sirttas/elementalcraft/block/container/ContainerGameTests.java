package sirttas.elementalcraft.block.container;

import net.minecraft.core.BlockPos;
import net.neoforged.testframework.gametest.GameTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.testframework.Test;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.RegisterStructureTemplate;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.container.reservoir.ReservoirGameTests;

import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@ForEachTest(groups = ContainerGameTests.GROUP)
public class ContainerGameTests {

    public static final String GROUP = "level.blocks.containers";

    public static final String EMPTY_SMALL_CONTAINER_TEMPLATE_NAME = "elementalcraft:empty_small_container";
    public static final String EMPTY_CONTAINER_TEMPLATE_NAME = "elementalcraft:empty_container";
    public static final String EMPTY_CREATIVE_CONTAINER_TEMPLATE_NAME = "elementalcraft:empty_creative_container";

    @RegisterStructureTemplate(EMPTY_SMALL_CONTAINER_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> EMPTY_SMALL_CONTAINER_TEMPLATE = StructureTemplateBuilder.lazy(1, 2, 1, b -> b.set(0, 0, 0, ECBlocks.SMALL_CONTAINER.get().defaultBlockState()));
    @RegisterStructureTemplate(EMPTY_CONTAINER_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> EMPTY_CONTAINER_TEMPLATE = StructureTemplateBuilder.lazy(1, 2, 1, b -> b.set(0, 0, 0, ECBlocks.CONTAINER.get().defaultBlockState()));
    @RegisterStructureTemplate(EMPTY_CREATIVE_CONTAINER_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> EMPTY_CREATIVE_CONTAINER_TEMPLATE = StructureTemplateBuilder.lazy(1, 2, 1, b -> b.set(0, 0, 0, ECBlocks.CREATIVE_CONTAINER.get().defaultBlockState()));

    public static List<Test> should_supportARudimentaryExtractor() {
        var i = 0;

        return List.of(
                ECGameTestUtils.createTest(
                        GROUP,
                        "should_supportARudimentaryExtractor_" + i++,
                        "Check if a rudimentary extractor can be placed on top of a small element container",
                        EMPTY_SMALL_CONTAINER_TEMPLATE_NAME,
                        h -> should_supportARudimentaryExtractor(h, new BlockPos(0, 2, 0))),
                ECGameTestUtils.createTest(
                        GROUP, "should_supportARudimentaryExtractor_" + i++,
                        "Check if a rudimentary extractor can be placed on top of an element container",
                        EMPTY_CONTAINER_TEMPLATE_NAME,
                        h -> should_supportARudimentaryExtractor(h, new BlockPos(0, 2, 0))),
                ECGameTestUtils.createTest(
                        GROUP, "should_supportARudimentaryExtractor_" + i++,
                        "Check if a rudimentary extractor can be placed on top of a fire reservoir",
                        ReservoirGameTests.FIRE_RESERVOIR_TEMPLATE_NAME,
                        h -> should_supportARudimentaryExtractor(h, new BlockPos(0, 3, 0))),
                ECGameTestUtils.createTest(
                        GROUP, "should_supportARudimentaryExtractor_" + i++,
                        "Check if a rudimentary extractor can be placed on top of a water reservoir",
                        ReservoirGameTests.WATER_RESERVOIR_TEMPLATE_NAME,
                        h -> should_supportARudimentaryExtractor(h, new BlockPos(0, 3, 0))),
                ECGameTestUtils.createTest(
                        GROUP, "should_supportARudimentaryExtractor_" + i++,
                        "Check if a rudimentary extractor can be placed on top of an earth reservoir",
                        ReservoirGameTests.EARTH_RESERVOIR_TEMPLATE_NAME,
                        h -> should_supportARudimentaryExtractor(h, new BlockPos(0, 3, 0))),
                ECGameTestUtils.createTest(
                        GROUP, "should_supportARudimentaryExtractor_" + i++,
                        "Check if a rudimentary extractor can be placed on top of an air reservoir",
                        ReservoirGameTests.AIR_RESERVOIR_TEMPLATE_NAME,
                        h -> should_supportARudimentaryExtractor(h, new BlockPos(0, 3, 0))),
                ECGameTestUtils.createTest(
                        GROUP, "should_supportARudimentaryExtractor_" + i++,
                        "Check if a rudimentary extractor can be placed on top of a creative element container",
                        EMPTY_CREATIVE_CONTAINER_TEMPLATE_NAME,
                        h -> should_supportARudimentaryExtractor(h, new BlockPos(0, 2, 0))));
    }

    private static void should_supportARudimentaryExtractor(ECGameTestHelper helper, BlockPos pos) {
        helper.startSequence().thenExecute(() -> {
            helper.setBlock(pos, ECBlocks.RUDIMENTARY_EXTRACTOR.get());
            updateShape(helper, pos.below());
        }).thenExecuteAfter(10, ECGameTestUtils.fixAssertions(() -> {
            helper.assertBlockPresent(ECBlocks.RUDIMENTARY_EXTRACTOR.get(), pos);
            assertThat(helper.getBlockState(pos))
                    .as("The rudimentary extractor should be able to survive on top of the container")
                    .satisfies(state -> assertThat(state.canSurvive(helper.getLevel(), helper.absolutePos(pos))).isTrue());
        })).thenSucceed();
    }

    @TestHolder(description = "Check that a small element container does not support an extractor")
    @GameTest(template = EMPTY_SMALL_CONTAINER_TEMPLATE_NAME)
    private static void shouldNot_supportAnExtractor(ECGameTestHelper helper) {
        var pos = new BlockPos(0, 2, 0);

        helper.startSequence().thenExecute(() -> {
            helper.setBlock(pos, ECBlocks.EXTRACTOR.get());
            updateShape(helper, pos.below());
        }).thenExecuteAfter(10, ECGameTestUtils.fixAssertions(() -> {
            helper.assertBlockNotPresent(ECBlocks.EXTRACTOR.get(), pos);
        })).thenSucceed();
    }

    private static void updateShape(ECGameTestHelper helper, BlockPos pos) {
        var level = helper.getLevel();
        var absolutePos = helper.absolutePos(pos);

        level.getBlockState(absolutePos).updateNeighbourShapes(level, absolutePos, 3);
    }
}
