package sirttas.elementalcraft.block.source.breeder.pedestal;

import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.container.SingleItemContainer;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;

public class SourceBreederPedestalContainer extends SingleItemContainer {

    public SourceBreederPedestalContainer(Runnable syncCallback) {
        super(syncCallback);
    }

    @Override
    public boolean canPlaceItem(int slot, @Nonnull ItemStack stack) {
        return slot == 0 && stack.is(ECTags.Items.FULL_RECEPTACLES);
    }

}
