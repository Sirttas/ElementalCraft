package sirttas.elementalcraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nonnull;
import java.util.Objects;

public class BlockEntityGameTestHelper {

    private BlockEntityGameTestHelper() {}

    @Nonnull
    public static BlockEntity getBlockEntity(GameTestHelper helper, BlockPos pos) {
        var be = helper.getBlockEntity(pos);

        if (be == null) {
            helper.fail("No block entity fond for block: " + helper.getBlockState(pos), pos);
        }
        return Objects.requireNonNull(be);
    }
}
