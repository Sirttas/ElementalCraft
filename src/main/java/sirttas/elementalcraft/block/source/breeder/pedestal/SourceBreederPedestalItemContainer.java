package sirttas.elementalcraft.block.source.breeder.pedestal;

import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.container.SingleItemContainer;
import sirttas.elementalcraft.item.ECItems;

import javax.annotation.Nonnull;

public class SourceBreederPedestalItemContainer extends SingleItemContainer {

    public SourceBreederPedestalItemContainer(Runnable syncCallback) {
        super(syncCallback);
    }

    @Override
    public boolean canPlaceItem(int slot, @Nonnull ItemStack stack) {
        if (slot == 0 && stack.is(ECItems.RECEPTACLE.get())) {
            var blockEntityTag = stack.getTagElement(ECNames.BLOCK_ENTITY_TAG);

            if (blockEntityTag != null) {
                return blockEntityTag.getBoolean(ECNames.ANALYZED);
            }
        }
        return false;
    }

}
