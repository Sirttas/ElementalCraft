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
        var sorter = helper.getBlockEntity(pos);

        return Objects.requireNonNull(sorter);
    }
}
