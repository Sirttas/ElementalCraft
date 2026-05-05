package sirttas.elementalcraft.block.pipe.upgrade.priority;

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
public class PipePriorityRingsGameTests {

    @TestHolder(description = "Checks that the rigs make the west container fill first.")
    @GameTest
    public static void should_transferWestFirst(DynamicTest test) {
        test.registerGameTestTemplate(() -> {
            var builder = StructureTemplateBuilder.withSize(2, 3, 4)
                    .fill(0, 0, 0, 1, 0, 3, ECBlocks.WHITE_ROCK_BRICKS.get())
                    .placeFloorLever(0, 1, 1, false)
                    .set(0, 0, 1, Blocks.REDSTONE_LAMP.defaultBlockState())
                    .set(1, 1, 0, ECBlocks.CONTAINER.get().defaultBlockState(), withValue(elementStorage(ElementType.WATER, 100000)))
                    .set(0, 1, 3, ECBlocks.CONTAINER.get().defaultBlockState())
                    .set(1, 1, 3, ECBlocks.CONTAINER.get().defaultBlockState());

            StructureTemplatePipeLine.builder()
                    .extract(Direction.NORTH).upgrade(Direction.SOUTH, PipeUpgradeTypes.ELEMENT_VALVE)
                    .lay(Direction.SOUTH).insert(Direction.SOUTH)
                    .lay(Direction.WEST).insert(Direction.SOUTH).upgrade(Direction.EAST, PipeUpgradeTypes.PIPE_PRIORITY_RINGS)
                    .build()
                    .place(builder, new BlockPos(1, 1, 1));
            return builder;
        });

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var targetStorage1 = helper.getBlockEntity(new BlockPos(0, 1, 3), ElementContainerBlockEntity.class).getElementStorage();
            var targetStorage2 = helper.getBlockEntity(new BlockPos(1, 1, 3), ElementContainerBlockEntity.class).getElementStorage();
            var ticks = new AtomicInteger(0);

            helper.startSequence()
                    .thenExecute(() -> helper.pullLever(0, 1, 1))
                    .thenIdle(1)
                    .thenExecuteFor(10, () -> {
                        assertThat(targetStorage1.getElementAmount()).isEqualTo(500 * ticks.incrementAndGet());
                        assertThat(targetStorage2.getElementAmount()).isZero();
                    })
                    .thenSucceed();
        });
    }

    @TestHolder(description = "Checks that the rigs make the far container fill first.")
    @GameTest
    public static void should_transferFarFirst(DynamicTest test) {
        test.registerGameTestTemplate(() -> {
            var builder = StructureTemplateBuilder.withSize(2, 3, 6)
                    .fill(0, 0, 0, 1, 0, 5, ECBlocks.WHITE_ROCK_BRICKS.get())
                    .placeFloorLever(0, 1, 1, false)
                    .set(0, 0, 1, Blocks.REDSTONE_LAMP.defaultBlockState())
                    .set(1, 1, 0, ECBlocks.CONTAINER.get().defaultBlockState(), withValue(elementStorage(ElementType.WATER, 100000)))
                    .set(1, 1, 3, ECBlocks.CONTAINER.get().defaultBlockState())
                    .set(1, 1, 5, ECBlocks.CONTAINER.get().defaultBlockState());

            StructureTemplatePipeLine.builder()
                    .extract(Direction.NORTH).upgrade(Direction.SOUTH, PipeUpgradeTypes.ELEMENT_VALVE)
                    .lay(Direction.SOUTH).insert(Direction.SOUTH)
                    .lay(Direction.WEST)
                    .lay(Direction.SOUTH).upgrade(Direction.SOUTH, PipeUpgradeTypes.PIPE_PRIORITY_RINGS)
                    .lay(Direction.SOUTH, 2).insert(Direction.EAST)
                    .build()
                    .place(builder, new BlockPos(1, 1, 1));
            return builder;
        });

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var targetStorage1 = helper.getBlockEntity(new BlockPos(1, 1, 5), ElementContainerBlockEntity.class).getElementStorage();
            var targetStorage2 = helper.getBlockEntity(new BlockPos(1, 1, 3), ElementContainerBlockEntity.class).getElementStorage();
            var ticks = new AtomicInteger(0);

            helper.startSequence()
                    .thenExecute(() -> helper.pullLever(0, 1, 1))
                    .thenIdle(1)
                    .thenExecuteFor(10, () -> {
                        assertThat(targetStorage1.getElementAmount()).isEqualTo(500 * ticks.incrementAndGet());
                        assertThat(targetStorage2.getElementAmount()).isZero();
                    })
                    .thenSucceed();
        });
    }
}
