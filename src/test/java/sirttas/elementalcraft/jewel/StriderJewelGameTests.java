package sirttas.elementalcraft.jewel;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.testframework.DynamicTest;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;

import static org.assertj.core.api.Assertions.assertThat;

@ForEachTest(groups = StriderJewelGameTests.GROUP)
public class StriderJewelGameTests {

    public static final String GROUP = "jewels.strider";

    @GameTest
    @TestHolder(description = "Checks if a player can walk on water with a water strider jewel.")
    public static void should_allowPlayerToWalkOnWater(DynamicTest test) {
        test.registerGameTestTemplate(() -> StructureTemplateBuilder.withSize(5, 10, 5)
                .fill(0, 0, 0, 4, 4, 4, ECBlocks.WHITE_ROCK_BRICKS.get())
                .fill(1, 1, 1, 3, 4, 3, Blocks.WATER));

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var player = helper.mockPlayerWithJewel(new Vec3(2, 8, 2), Jewels.WATER_STRIDER);

            helper.startSequence()
                    .thenExecuteAfter(100, () -> {
                        helper.assertJewelActive(player, Jewels.WATER_STRIDER);
                        assertThat(player.getOnPos())
                                .isEqualTo(helper.absolutePos(new BlockPos(2, 4, 2)));
                        helper.assertElementUsed(player, ElementType.WATER);
                    }).thenExecute(player::discard)
                    .thenSucceed();
        });
    }

    @GameTest
    @TestHolder(description = "Checks if a player can walk on lava with a strider jewel.")
    public static void should_allowPlayerToWalkOnLava(DynamicTest test) {
        test.registerGameTestTemplate(() -> StructureTemplateBuilder.withSize(5, 10, 5)
                .fill(0, 0, 0, 4, 4, 4, ECBlocks.WHITE_ROCK_BRICKS.get())
                .fill(1, 1, 1, 3, 4, 3, Blocks.LAVA));

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var player = helper.mockPlayerWithJewel(new Vec3(2, 8, 2), Jewels.STRIDER);

            helper.startSequence()
                    .thenExecuteAfter(100, () -> {
                        helper.assertJewelActive(player, Jewels.STRIDER);
                        assertThat(player.getOnPos())
                                .isEqualTo(helper.absolutePos(new BlockPos(2, 4, 2)));
                        helper.assertElementUsed(player, ElementType.FIRE);
                    }).thenExecute(player::discard)
                    .thenSucceed();
        });
    }

}
