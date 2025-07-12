package sirttas.elementalcraft.block.source.breeder;

import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.container.SingleItemContainer;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;

public class SourceBreederContainer extends SingleItemContainer {

    public SourceBreederContainer(Runnable syncCallback) {
        super(syncCallback);
    }

    @Override
    public boolean canPlaceItem(int slot, @Nonnull ItemStack stack) {
        return slot == 0 && stack.is(ECTags.Items.SOURCE_SEEDS);
    }

}
