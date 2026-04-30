package sirttas.elementalcraft.block.shrine.harvest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;

import java.util.List;

@ForEachTest(groups = ShrineGameTestHelper.GROUP)
public class HarvestShrineGameTests {
    public static final List<BlockPos> POSES = List.of(
            new BlockPos(1, 1, 1),
            new BlockPos(1, 1, 2),
            new BlockPos(1, 1, 3),
            new BlockPos(1, 1, 4),
            new BlockPos(1, 1, 5),
            new BlockPos(2, 1, 1),
            new BlockPos(2, 1, 2),
            new BlockPos(2, 1, 3),
            new BlockPos(2, 1, 4),
            new BlockPos(2, 1, 5),
            new BlockPos(3, 1, 1),
            new BlockPos(3, 1, 2),
            new BlockPos(3, 1, 4),
            new BlockPos(3, 1, 5),
            new BlockPos(4, 1, 1),
            new BlockPos(4, 1, 2),
            new BlockPos(4, 1, 3),
            new BlockPos(4, 1, 4),
            new BlockPos(4, 1, 5),
            new BlockPos(5, 1, 1),
            new BlockPos(5, 1, 2),
            new BlockPos(5, 1, 3),
            new BlockPos(5, 1, 4),
            new BlockPos(5, 1, 5)
    );
    private static final String TEMPLATE = "elementalcraft:harvestshrinegametests.should_harvestwheat";

    @TestHolder
    @GameTest(template = TEMPLATE)
    public static void should_harvestWheat(GameTestHelper helper) {
        helper.startSequence().thenExecute(() -> {
            ShrineGameTestHelper.forcePeriods(helper, new BlockPos(3, 3, 3), POSES.size());
        }).thenExecuteAfter(1, () -> {
            POSES.forEach(p -> helper.assertBlockState(p, b -> b.is(Blocks.AIR), _ -> Component.literal("Block has not been harvested")));
            helper.assertItemEntityCountIs(Items.WHEAT, new BlockPos(3, 1, 3), 3, POSES.size());
        }).thenSucceed();
    }
}
