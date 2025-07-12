package sirttas.elementalcraft.block.synthesizer.culinary;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.container.SingleStackContainer;

public class CulinarySynthesizerContainer extends SingleStackContainer {

    protected CulinarySynthesizerContainer(CulinarySynthesizerBlockEntity blockEntity) {
        super(blockEntity::setChanged);
    }

    @Override
    public boolean canPlaceItem(int index, @NotNull ItemStack stack) {
        return super.canPlaceItem(index, stack) && stack.getFoodProperties(null) != null;
    }
}
