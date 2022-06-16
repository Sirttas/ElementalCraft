package sirttas.elementalcraft.block.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.item.TooltipImageBlockItem;

import javax.annotation.Nonnull;

public class ElementContainerBlockItem extends TooltipImageBlockItem {

    public ElementContainerBlockItem(AbstractElementContainerBlock block, Properties properties) {
        super(block, properties);
    }

    @Override
    public int getBarColor(@Nonnull ItemStack stack) {
        var tag = getElementStorageTag(stack);

        if (tag != null) {
            return ElementType.byName(getElementStorageTag(stack).getString(ECNames.ELEMENT_TYPE)).getColor();
        }
        return super.getBarColor(stack);
    }

    public CompoundTag getElementStorageTag(@Nonnull ItemStack stack) {
        return ((AbstractElementContainerBlock) getBlock()).getElementStorageTag(stack);
    }

    @Override
    public int getDamage(ItemStack stack) {
        var elementStorageNbt = getElementStorageTag(stack);

        if (elementStorageNbt != null) {
            int amount = elementStorageNbt.getInt(ECNames.ELEMENT_AMOUNT);
            int capacity = elementStorageNbt.getInt(ECNames.ELEMENT_CAPACITY);

            if (amount > 0) {
                return 1000 * (capacity - amount) / capacity;
            }
        }
        return 1000;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 1000;
    }

    @Override
    public boolean isBarVisible(@Nonnull ItemStack stack) {
        return true;
    }
}
