package sirttas.elementalcraft.block.shrine.lava;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;

import java.util.List;

@GameTestHolder(ElementalCraftApi.MODID)
public class LavaShrineGameTests {

    private static final List<BlockPos> POSES = List.of(
            new BlockPos(1, 2, 1),
            new BlockPos(1, 2, 2),
            new BlockPos(1, 2, 3),
            new BlockPos(2, 2, 1),
            new BlockPos(2, 2, 2),
            new BlockPos(2, 2, 3),
            new BlockPos(3, 2, 1),
            new BlockPos(3, 2, 2),
            new BlockPos(3, 2, 3)
    );

    // elementalcraft:lavashrinegametests.should_meltbasaltintolava
    @GameTest(batch = ShrineGameTestHelper.BATCH_NAME)
    public static void should_meltBasaltIntoLava(GameTestHelper helper) {
        helper.startSequence().thenExecute(() -> {
            POSES.forEach(p -> helper.setBlock(p, Blocks.BASALT));
        }).thenExecuteAfter(1, () -> {
            ShrineGameTestHelper.forcePeriods(helper, new BlockPos(2, 1, 2), POSES.size());
        }).thenExecuteAfter(1, () -> {
            POSES.forEach(p -> helper.assertBlockState(p, b -> b.is(Blocks.LAVA), () -> "Block has not been melted"));
        }).thenSucceed();
    }

}
