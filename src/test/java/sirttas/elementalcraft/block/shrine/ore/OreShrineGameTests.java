package sirttas.elementalcraft.block.shrine.ore;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;

import java.util.List;

@ForEachTest(groups = ShrineGameTestHelper.GROUP)
public class OreShrineGameTests {

    private static final List<BlockPos> IN_RANGE = List.of(
            new BlockPos(12, 1, 12),
            new BlockPos(-12, 1, 12),
            new BlockPos(12, 1, -12),
            new BlockPos(-12, 1, -12)
    );

    private static final List<BlockPos> OUTSIDE_RANGE = List.of(
            new BlockPos(12, 2, 12),
            new BlockPos(13, 1, 12),
            new BlockPos(12, 1, 13),
            new BlockPos(13, 1, 13),
            new BlockPos(-12, 2, 12),
            new BlockPos(-13, 1, 12),
            new BlockPos(-12, 1, 13),
            new BlockPos(-13, 1, 13),
            new BlockPos(12, 2, -12),
            new BlockPos(13, 1, -12),
            new BlockPos(12, 1, -13),
            new BlockPos(13, 1, -13),
            new BlockPos(-12, 2, -12),
            new BlockPos(-13, 1, -12),
            new BlockPos(-12, 1, -13),
            new BlockPos(-13, 1, -13)
    );
    private static final String TEMPLATE = "elementalcraft:oreshrinegametests.should_mineinrange";

    @TestHolder
    @GameTest(template = TEMPLATE)
    public static void should_mineInRange(GameTestHelper helper) {
        ShrineGameTestHelper.forcePeriods(helper, new BlockPos(13, 2, 13), IN_RANGE.size() + OUTSIDE_RANGE.size());
        helper.succeedIf(() -> {
            IN_RANGE.forEach(p -> helper.assertBlockState(p.offset(13, 0, 13), b -> b.is(Blocks.STONE), _ -> Component.literal("Block has not been mined")));
            OUTSIDE_RANGE.forEach(p -> helper.assertBlockState(p.offset(13, 0, 13), b -> b.is(Blocks.IRON_ORE), _ -> Component.literal("Block has been mined")));
        });
    }
}
