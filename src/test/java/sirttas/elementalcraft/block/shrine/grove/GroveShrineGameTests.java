package sirttas.elementalcraft.block.shrine.grove;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;

import java.util.List;

@ForEachTest(groups = ShrineGameTestHelper.GROUP)
public class GroveShrineGameTests {

    private static final List<BlockPos> POSES = List.of(
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
    private static final String TEMPLATE = "elementalcraft:groveshrinegametests.should_generateflowers";

    @TestHolder
    @GameTest(template = TEMPLATE)
    public static void should_generateFlowers(GameTestHelper helper) {
        helper.startSequence()
                .thenExecuteAfter(1, () -> ShrineGameTestHelper.forcePeriods(helper, new BlockPos(3, 1, 3), POSES.size()))
                .thenExecuteAfter(1, () -> POSES.forEach(p -> helper.assertBlockState(p, b -> b.is(BlockTags.FLOWERS), _ -> Component.literal("Flower has not been generated"))))
                .thenSucceed();
    }

}
