package sirttas.elementalcraft.block.synthesizer.mill;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
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

@ForEachTest(groups = AirMillSynthesizerGameTests.GROUP)
public class AirMillSynthesizerGameTests {

    public static final String GROUP = "synthesizer.air.mill";

    public static final String TEMPLATE_NAME = "elementalcraft:air_mill_synthesizer";

    @RegisterStructureTemplate(TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> TEMPLATE = StructureTemplateBuilder.lazy(1, 3, 2, builder -> builder
            .placeFloorLever(0, 1, 1, true)
            .set(0, 0, 0, ECBlocks.CONTAINER.get().defaultBlockState())
            .set(0, 1, 0, ECBlocks.AIR_MILL_SYNTHESIZER.get().defaultBlockState().setValue(AirMillSynthesizerBlock.HALF, DoubleBlockHalf.LOWER))
            .set(0, 2, 0, ECBlocks.AIR_MILL_SYNTHESIZER.get().defaultBlockState().setValue(AirMillSynthesizerBlock.HALF, DoubleBlockHalf.UPPER))
            .set(0, 0, 1, Blocks.REDSTONE_LAMP.defaultBlockState().setValue(RedstoneLampBlock.LIT, true)));

    @TestHolder(description = "Checks that the air mill synthesizer generates air.")
    @GameTest(template = TEMPLATE_NAME)
    public static void should_generateAir(ECGameTestHelper helper) {
        var ticks = new AtomicInteger(0);
        var synthesizer = helper.getBlockEntity(new BlockPos(0, 1, 0), AirMillSynthesizerBlockEntity.class);
        var storage = helper.requireElementContainer(BlockPos.ZERO);

        helper.startSequence()
                .thenExecute(() -> helper.pullLever(new BlockPos(0, 1, 1)))
                .thenIdle(1)
                .thenExecuteFor(20, () -> {
                    var t = ticks.incrementAndGet();

                    assertThat(synthesizer.getDamage())
                            .as("Air mill synthesizer should take damage over time (at a rate of 1 every two ticks)")
                            .isEqualTo((int) Math.ceil(t / 2F));
                    assertThat(storage.getElementType())
                            .isEqualTo(ElementType.AIR);
                    assertThat(storage.getElementAmount())
                            .as("Air mill synthesizer should generate air over time (at a rate of 25 every tick)")
                            .isEqualTo(t * 25);
                }).thenSucceed();
    }

}
