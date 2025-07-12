package sirttas.elementalcraft.container;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class ContainerGameTestHelper {

    private ContainerGameTestHelper() {}

    @Nonnull
    public static IItemHandler getItemHandler(GameTestHelper helper, BlockPos pos) {
        return ECContainerHelper.getItemHandler(helper.getBlockEntity(pos), null);
    }

}
