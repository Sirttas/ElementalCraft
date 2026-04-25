package sirttas.elementalcraft.item.cover;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.testframework.Test;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.RegisterStructureTemplate;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.cover.Coverable;
import sirttas.elementalcraft.item.chisel.ChiselGameTests;

import java.util.List;
import java.util.function.Supplier;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = ChiselGameTests.GROUP_NAME)
public class CoverFrameGameTests {

    public static final String GROUP_NAME = "stacks.cover_frame";

    public static final String RUDIMENTARY_PIPE_TEMPLATE_NAME = "elementalcraft:rudimentary_pipe_cover";
    public static final String PIPE_TEMPLATE_NAME = "elementalcraft:pipe_cover";
    public static final String IMPROVED_PIPE_TEMPLATE_NAME = "elementalcraft:improved_pipe_cover";
    public static final String RETRIEVER_TEMPLATE_NAME = "elementalcraft:retriever_cover";
    public static final String ORDERED_SORTER_TEMPLATE_NAME = "elementalcraft:ordered_sorter_cover";

    @RegisterStructureTemplate(RUDIMENTARY_PIPE_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> RUDIMENTARY_PIPE_TEMPLATE = StructureTemplateBuilder.lazy(3, 3, 3, b -> b.set(0, 0, 2, ECBlocks.PIPE_RUDIMENTARY.get().defaultBlockState()));
    @RegisterStructureTemplate(PIPE_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> PIPE_TEMPLATE = StructureTemplateBuilder.lazy(3, 3, 3, b -> b.set(0, 0, 2, ECBlocks.PIPE.get().defaultBlockState()));
    @RegisterStructureTemplate(IMPROVED_PIPE_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> IMPROVED_PIPE_TEMPLATE = StructureTemplateBuilder.lazy(3, 3, 3, b -> b.set(0, 0, 2, ECBlocks.PIPE_IMPROVED.get().defaultBlockState()));
    @RegisterStructureTemplate(RETRIEVER_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> RETRIEVER_TEMPLATE = StructureTemplateBuilder.lazy(3, 3, 3, b -> b.set(0, 0, 2, ECBlocks.RETRIEVER.get().defaultBlockState()));
    @RegisterStructureTemplate(ORDERED_SORTER_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> ORDERED_SORTER_TEMPLATE = StructureTemplateBuilder.lazy(3, 3, 3, b -> b.set(0, 0, 2, ECBlocks.ORDERED_SORTER.get().defaultBlockState()));

    public static List<Test> collectTests() {
        var i = 0;

        return List.of(
                ECGameTestUtils.createTest(
                        GROUP_NAME,
                        "CoverFrameGameTests.should_addCoverToCoverable_" + i++,
                        "Check if a rudimentary element pipe can be covered with a cover frame.",
                        RUDIMENTARY_PIPE_TEMPLATE_NAME,
                        CoverFrameGameTests::should_addCoverToCoverable),
                ECGameTestUtils.createTest(
                        GROUP_NAME,
                        "CoverFrameGameTests.should_addCoverToCoverable_" + i++,
                        "Check if an element pipe can be covered with a cover frame.",
                        PIPE_TEMPLATE_NAME,
                        CoverFrameGameTests::should_addCoverToCoverable),
                ECGameTestUtils.createTest(
                        GROUP_NAME,
                        "CoverFrameGameTests.should_addCoverToCoverable_" + i++,
                        "Check if an improved element pipe can be covered with a cover frame.",
                        IMPROVED_PIPE_TEMPLATE_NAME,
                        CoverFrameGameTests::should_addCoverToCoverable),
                ECGameTestUtils.createTest(
                        GROUP_NAME,
                        "CoverFrameGameTests.should_addCoverToCoverable_" + i++,
                        "Check if an instrument output retriever can be covered with a cover frame.",
                        RETRIEVER_TEMPLATE_NAME,
                        CoverFrameGameTests::should_addCoverToCoverable),
                ECGameTestUtils.createTest(
                        GROUP_NAME,
                        "CoverFrameGameTests.should_addCoverToCoverable_" + i++,
                        "Check if an ordered sorter can be covered with a cover frame.",
                        ORDERED_SORTER_TEMPLATE_NAME,
                        CoverFrameGameTests::should_addCoverToCoverable)
        );
    }

    private static void should_addCoverToCoverable(ECGameTestHelper helper) {
        var pos = new BlockPos(0, 1, 2);
        var player = helper.mockCoverFramePlayer();

        helper.startSequence()
                .thenExecute(() -> helper.useBlock(pos, player))
                .thenIdle(1)
                .thenExecute(() -> {
                    var coverable = helper.getCapability(Coverable.CAPABILITY, pos, null);

                    assertThat(coverable)
                            .isNotNull()
                            .satisfies(cover -> assertThat(cover.hasFrame()).as("Coverable should has a frame").isTrue());
                })
                .thenSucceed();
    }
}
