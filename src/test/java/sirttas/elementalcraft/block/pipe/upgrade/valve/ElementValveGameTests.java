package sirttas.elementalcraft.block.pipe.upgrade.valve;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.testframework.DynamicTest;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeGameTests;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.template.StructureTemplatePipeLine;

import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;
import static sirttas.elementalcraft.template.StructureTemplateHelper.elementStorage;
import static sirttas.elementalcraft.template.StructureTemplateHelper.withValue;

@ForEachTest(groups = ElementPipeGameTests.GROUP)
public class ElementValveGameTests {

    @TestHolder(description = "Checks that a pipe with a valve transfers elements only when powered.")
    @GameTest
    public static void should_transferElements_onlyWhen_powered(DynamicTest test) {
        test.registerGameTestTemplate(() -> {
            var builder = StructureTemplateBuilder.withSize(3, 3, 2)
                    .placeFloorLever(1, 1, 1, false)
                    .fill(0, 0, 0, 2, 0, 1, ECBlocks.WHITE_ROCK_BRICKS.get())
                    .set(0, 1, 0, ECBlocks.CREATIVE_CONTAINER.get().defaultBlockState(), withValue(elementStorage(ElementType.FIRE, 1000000)))
                    .set(2, 1, 0, ECBlocks.CONTAINER.get().defaultBlockState())
                    .set(1, 0, 1, Blocks.REDSTONE_LAMP.defaultBlockState());

            StructureTemplatePipeLine.builder()
                    .extract(Direction.WEST)
                    .insert(Direction.EAST)
                    .upgrade(Direction.WEST, PipeUpgradeTypes.ELEMENT_VALVE)
                    .build()
                    .place(builder, new BlockPos(1, 1, 0));
            return builder;
        });

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var targetStorage = helper.getBlockEntity(new BlockPos(2, 1, 0), ElementContainerBlockEntity.class).getElementStorage();
            var ticks = new AtomicInteger(0);

            helper.startSequence()
                    .thenIdle(1)
                    .thenExecuteFor(10, () -> assertThat(targetStorage.getElementAmount())
                            .as("Elements should not be transferred when the valve is not powered")
                            .isZero())
                    .thenExecute(() -> helper.pullLever(1, 1, 1))
                    .thenIdle(1)
                    .thenExecuteFor(10, () -> assertThat(targetStorage.getElementAmount())
                            .as("Elements should be transferred when the valve is powered")
                            .isEqualTo(500 * ticks.incrementAndGet()))
                    .thenSucceed();
        });
    }
}
