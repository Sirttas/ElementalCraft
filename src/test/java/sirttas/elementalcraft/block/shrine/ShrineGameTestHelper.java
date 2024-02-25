package sirttas.elementalcraft.block.shrine;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import sirttas.elementalcraft.block.entity.BlockEntityGameTestHelper;

import javax.annotation.Nonnull;

public class ShrineGameTestHelper {

    public static final String BATCH_NAME = "shrine";

    private ShrineGameTestHelper() {}

    public static void forcePeriod(GameTestHelper helper, BlockPos pos) {
        forcePeriods(helper, pos, 1);
    }

    public static void forcePeriods(GameTestHelper helper, BlockPos pos, int periods) {
        forcePeriods(getShrine(helper, pos), periods);
    }

    public static void forcePeriod(AbstractShrineBlockEntity shrine) {
        forcePeriods(shrine, 1);
    }

    public static void forcePeriods(AbstractShrineBlockEntity shrine, int periods) {
        var storage = shrine.getElementStorage();

        shrine.refresh();
        for (int i = 0; i < periods; i++) {
            storage.fill();
            shrine.doPeriod();
        }
    }

    @Nonnull
    public static AbstractShrineBlockEntity getShrine(GameTestHelper helper, BlockPos pos) {
        return (AbstractShrineBlockEntity) BlockEntityGameTestHelper.getBlockEntity(helper, pos);
    }
}
