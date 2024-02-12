package sirttas.elementalcraft.element.storage;

import net.minecraft.world.level.block.entity.BlockEntity;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

public class ElementStorageGameTestHelper {

    private ElementStorageGameTestHelper() { }

    public static IElementStorage get(BlockEntity blockEntity) {
        assertThat(blockEntity).isNotNull();

        var storage = BlockEntityHelper.getCapability(ElementalCraftCapabilities.ElementStorage.BLOCK, blockEntity, null);

        assertThat(storage).isNotNull();

        return storage;
    }
}
