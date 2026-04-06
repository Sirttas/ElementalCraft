package sirttas.elementalcraft.block.synthesizer.combustion;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.container.SingleStackContainer;

public class CombustionSynthesizerContainer extends SingleStackContainer {

    private final CombustionSynthesizerBlockEntity blockEntity;

    protected CombustionSynthesizerContainer(CombustionSynthesizerBlockEntity blockEntity) {
        super(blockEntity::setChanged);
        this.blockEntity = blockEntity;
    }

    @Override
    public boolean canPlaceItem(int index, @NotNull ItemStack stack) {
        return super.canPlaceItem(index, stack) && stack.getBurnTime(RecipeType.SMELTING, blockEntity.getLevel().fuelValues()) > 0;
    }
}
