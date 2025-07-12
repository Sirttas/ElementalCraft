package sirttas.elementalcraft.block.synthesizer.combustion;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.container.SingleStackContainer;

public class CombustionSynthesizerContainer extends SingleStackContainer {

    protected CombustionSynthesizerContainer(CombustionSynthesizerBlockEntity blockEntity) {
        super(blockEntity::setChanged);
    }

    @Override
    public boolean canPlaceItem(int index, @NotNull ItemStack stack) {
        return super.canPlaceItem(index, stack) && AbstractFurnaceBlockEntity.isFuel(stack);
    }
}
