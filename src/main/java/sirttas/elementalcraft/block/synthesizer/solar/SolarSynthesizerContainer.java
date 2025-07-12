package sirttas.elementalcraft.block.synthesizer.solar;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.container.SingleStackContainer;
import sirttas.elementalcraft.tag.ECTags;

public class SolarSynthesizerContainer extends SingleStackContainer {

    protected SolarSynthesizerContainer(SolarSynthesizerBlockEntity blockEntity) {
        super(blockEntity::setChanged);
    }

    @Override
    public boolean canPlaceItem(int index, @NotNull ItemStack stack) {
        return super.canPlaceItem(index, stack) && stack.is(ECTags.Items.LENSES);
    }
}
