package sirttas.elementalcraft.block.synthesizer.cracking.sculk;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.RegisterStructureTemplate;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = SculkCrackingSynthesizerGameTests.GROUP)
public class SculkCrackingSynthesizerGameTests {

    public static final String GROUP = "synthesizer.cracking.sculk";

    public static final String SCULK_CRACKING_SYNTHESIZER_TEMPLATE_NAME = "elementalcraft:sculk_cracking_synthesizer";

    @RegisterStructureTemplate(SCULK_CRACKING_SYNTHESIZER_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> TEMPLATE = StructureTemplateBuilder.lazy(17, 3, 17, builder -> builder
            .placeFloorLever(8, 2, 9, true)
            .fill(0, 0, 0, 16, 0, 16, ECBlocks.WHITE_ROCK_BRICKS.get())
            .fill(1, 0, 1, 15, 0, 15, Blocks.SCULK)
            .set(8, 1, 8, ECBlocks.CONTAINER.get().defaultBlockState())
            .set(8, 2, 8, ECBlocks.SCULK_CRACKING_SYNTHESIZER.get().defaultBlockState())
            .set(8, 1, 9, Blocks.REDSTONE_LAMP.defaultBlockState().setValue(RedstoneLampBlock.LIT, true)));

    @TestHolder(description = "Checks if the sculk cracking synthesizer generates earth from the surrounding sculk.")
    @GameTest(template = SCULK_CRACKING_SYNTHESIZER_TEMPLATE_NAME)
    public static void should_generateEarthFromSculk(ECGameTestHelper helper) {
        var ticks = new AtomicInteger(0);
        var storage = helper.requireElementContainer(new BlockPos(8, 1, 8));

        helper.startSequence()
                .thenExecute(() -> helper.pullLever(new BlockPos(8, 2, 9)))
                .thenIdle(1)
                .thenExecuteFor(20, () -> {
                    var t = ticks.incrementAndGet();

                    assertThat(storage.getElementType())
                            .isEqualTo(ElementType.EARTH);
                    assertThat(storage.getElementAmount())
                            .as("Sculk cracking synthesizer should generate earth over time (at a rate of 25 every tick)")
                            .isEqualTo(t * 25);
                }).thenSucceed();
    }

}
