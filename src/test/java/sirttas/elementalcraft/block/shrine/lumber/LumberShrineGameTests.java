package sirttas.elementalcraft.block.shrine.lumber;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;

import java.util.List;

@GameTestHolder(ElementalCraftApi.MODID)
public class LumberShrineGameTests {
    private static final List<BlockPos> POSES = List.of(
            new BlockPos(1, 2, 1),
            new BlockPos(1, 2, 2),
            new BlockPos(1, 2, 3),
            new BlockPos(1, 2, 4),
            new BlockPos(1, 2, 5),
            new BlockPos(2, 2, 1),
            new BlockPos(2, 2, 2),
            new BlockPos(2, 2, 3),
            new BlockPos(2, 2, 4),
            new BlockPos(2, 2, 5),
            new BlockPos(3, 2, 1),
            new BlockPos(3, 2, 2),
            new BlockPos(3, 2, 4),
            new BlockPos(3, 2, 5),
            new BlockPos(4, 2, 1),
            new BlockPos(4, 2, 2),
            new BlockPos(4, 2, 3),
            new BlockPos(4, 2, 4),
            new BlockPos(4, 2, 5),
            new BlockPos(5, 2, 1),
            new BlockPos(5, 2, 2),
            new BlockPos(5, 2, 3),
            new BlockPos(5, 2, 4),
            new BlockPos(5, 2, 5)
    );

    // elementalcraft:lumbershrinegametests.should_cutoakblocks
    @GameTest(batch = ShrineGameTestHelper.BATCH_NAME)
    public static void should_cutOakBlocks(GameTestHelper helper) {
        helper.startSequence().thenExecute(() -> {
            POSES.forEach(p -> helper.setBlock(p, Blocks.OAK_LOG));
        }).thenExecuteAfter(1, () -> {
            ShrineGameTestHelper.forcePeriods(helper, new BlockPos(3, 2, 3), POSES.size());
        }).thenExecuteAfter(1, () -> {
            POSES.forEach(p -> helper.assertBlockState(p, b -> b.is(Blocks.AIR), () -> "Block has not been cut"));
            helper.assertItemEntityCountIs(Blocks.OAK_LOG.asItem(), new BlockPos(3, 2, 3), 3, POSES.size());
        }).thenExecute(() -> ECGameTestHelper.discardItems(helper, new BlockPos(3, 2, 3), 3))
        .thenSucceed();
    }
}
