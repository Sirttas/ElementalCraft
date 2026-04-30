package sirttas.elementalcraft.block.synthesizer.cracking;

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
import sirttas.elementalcraft.rune.Runes;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;
import static sirttas.elementalcraft.template.StructureTemplateHelper.runeHandler;
import static sirttas.elementalcraft.template.StructureTemplateHelper.withValue;

@ForEachTest(groups = CrackingSynthesizerGameTests.GROUP)
public class CrackingSynthesizerGameTests {

    public static final String GROUP = "synthesizer.cracking";

    public static final String CRACKING_SYNTHESIZER_TEMPLATE_NAME = "elementalcraft:cracking_synthesizer";
    public static final String CRACKING_SYNTHESIZER_WITH_RUNE_TEMPLATE_NAME = "elementalcraft:cracking_synthesizer_with_rune";

    @RegisterStructureTemplate(CRACKING_SYNTHESIZER_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> CRACKING_SYNTHESIZER_TEMPLATE = StructureTemplateBuilder.lazy(11, 3, 11, builder -> builder
            .placeFloorLever(5, 2, 6, true)
            .fill(0, 0, 0, 10, 0, 10, ECBlocks.WHITE_ROCK_BRICKS.get())
            .fill(1, 0, 1, 9, 0, 9, Blocks.STONE)
            .set(5, 1, 5, ECBlocks.CONTAINER.get().defaultBlockState())
            .set(5, 2, 5, ECBlocks.CRACKING_SYNTHESIZER.get().defaultBlockState())
            .set(5, 1, 6, Blocks.REDSTONE_LAMP.defaultBlockState().setValue(RedstoneLampBlock.LIT, true)));

    @RegisterStructureTemplate(CRACKING_SYNTHESIZER_WITH_RUNE_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> CRACKING_SYNTHESIZER_WITH_RUNE_TEMPLATE = StructureTemplateBuilder.lazy(13, 3, 13, builder -> builder
            .fill(0, 0, 0, 12, 0, 12, ECBlocks.WHITE_ROCK_BRICKS.get())
            .set(6, 1, 6, ECBlocks.CONTAINER.get().defaultBlockState())
            .set(6, 2, 6, ECBlocks.CRACKING_SYNTHESIZER.get().defaultBlockState(), withValue(runeHandler(Runes.TYRIA))));

    @TestHolder(description = "Checks if the cracking synthesizer generates earth from the surrounding stones.")
    @GameTest(template = CRACKING_SYNTHESIZER_TEMPLATE_NAME)
    public static void should_generateEarthFromStone(ECGameTestHelper helper) {
        var ticks = new AtomicInteger(0);
        var storage = helper.requireElementContainer(new BlockPos(5, 1, 5));

        helper.startSequence()
                .thenExecute(() -> helper.pullLever(new BlockPos(5, 2, 6)))
                .thenIdle(1)
                .thenExecuteFor(20, () -> {
                    var t = ticks.incrementAndGet();

                    assertThat(storage.getElementType())
                            .isEqualTo(ElementType.EARTH);
                    assertThat(storage.getElementAmount())
                            .as("Cracking synthesizer should generate earth over time (at a rate of 25 every tick)")
                            .isEqualTo(t * 5);
                }).thenSucceed();
    }

}
