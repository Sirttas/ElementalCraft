package sirttas.elementalcraft.block.shrine.ore;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;

import java.util.List;

@GameTestHolder(ElementalCraftApi.MODID)
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

    // elementalcraft:oreshrinegametests.should_mineinrange
    @GameTest(batch = ShrineGameTestHelper.BATCH_NAME)
    public static void should_mineInRange(GameTestHelper helper) {
        ShrineGameTestHelper.forcePeriods(helper, new BlockPos(13, 2, 13), IN_RANGE.size() + OUTSIDE_RANGE.size());
        helper.succeedIf(() -> {
            IN_RANGE.forEach(p -> helper.assertBlockState(p.offset(13, 0, 13), b -> b.is(Blocks.STONE), () -> "Block has not been mined"));
            OUTSIDE_RANGE.forEach(p -> helper.assertBlockState(p.offset(13, 0, 13), b -> b.is(Blocks.IRON_ORE), () -> "Block has been mined"));
        });
    }
}
