package sirttas.elementalcraft.block.synthesizer.draining;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.GameType;
import net.neoforged.testframework.DynamicTest;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;

import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = DrainingSynthesizerGameTests.GROUP)
public class DrainingSynthesizerGameTests {

    public static final String GROUP = "synthesizer.draining";

    @TestHolder(description = "Checks if the draining synthesizer generates water from the player hunger.")
    @GameTest
    public static void should_generateWaterFromPlayerHunger(DynamicTest test) {
        test.registerGameTestTemplate(() -> StructureTemplateBuilder.withSize(1, 2, 1)
                .set(0, 0, 0, ECBlocks.CONTAINER.get().defaultBlockState())
                .set(0, 1, 0, ECBlocks.DRAINING_SYNTHESIZER.get().defaultBlockState()));

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var player = helper.makeMockPlayer(GameType.SURVIVAL);

            helper.moveEntityToOrigin(player);
            helper.getLevel().addFreshEntity(player);

            var ticks = new AtomicInteger(0);
            var storage = helper.requireElementContainer(new BlockPos(0, 0, 0));

            helper.startSequence().thenExecuteAfter(1, () -> {
                helper.useBlock(new BlockPos(0, 1, 0), player);
            }).thenIdle(1).thenExecuteFor(20, () -> {
                var t = ticks.incrementAndGet();

                helper.useBlock(new BlockPos(0, 1, 0), player);
                assertThat(storage.getElementType())
                        .isEqualTo(ElementType.WATER);
                assertThat(storage.getElementAmount())
                        .isEqualTo(t * 5);
                assertThat(player.getFoodData().exhaustionLevel)
                        .isPositive();
            }).thenExecute(player::discard)
            .thenSucceed();
        });
    }

}
