package sirttas.elementalcraft.block.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.item.TooltipImageBlockItem;

import javax.annotation.Nonnull;

import net.minecraft.world.item.Item.Properties;

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
    public boolean isBarVisible(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(@Nonnull ItemStack stack) {
        var elementStorageNbt = getElementStorageTag(stack);

        if (elementStorageNbt != null) {
            int amount = elementStorageNbt.getInt(ECNames.ELEMENT_AMOUNT);
            int capacity = elementStorageNbt.getInt(ECNames.ELEMENT_CAPACITY);

            if (amount > 0) {
                return Math.round(amount * 13.0F / capacity);
            }
        }
        return 0;
    }
}
