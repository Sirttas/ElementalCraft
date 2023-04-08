package sirttas.elementalcraft.container;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.items.IItemHandler;
import sirttas.elementalcraft.block.entity.BlockEntityGameTestHelper;

import javax.annotation.Nonnull;

public class ContainerGameTestHelper {

    @Nonnull
    public static IItemHandler getItemHandler(GameTestHelper helper, BlockPos pos) {
        return ECContainerHelper.getItemHandler(BlockEntityGameTestHelper.getBlockEntity(helper, pos), null);
    }

}
