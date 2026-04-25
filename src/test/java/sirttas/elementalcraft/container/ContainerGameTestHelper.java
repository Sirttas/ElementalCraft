package sirttas.elementalcraft.container;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class ContainerGameTestHelper {

    private ContainerGameTestHelper() {}

    @Nonnull
    public static IItemHandler getItemHandler(GameTestHelper helper, BlockPos pos) {
        return IItemHandler.of(ECContainerHelper.getItemResourceHandler(helper.getBlockEntity(pos, BlockEntity.class), null));
    }

}
