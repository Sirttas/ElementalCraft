package sirttas.elementalcraft.block.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ElementContainerBlockItem extends BlockItem {

    public ElementContainerBlockItem(AbstractElementContainerBlock block, Properties properties) {
        super(block, properties);
    }

    @Override
    public int getBarColor(@Nonnull ItemStack stack) {
        var tag = getElementStorageTag(stack);

        if (tag != null) {
            return ElementType.byName(tag.getString(ECNames.ELEMENT_TYPE)).getColor();
        }
        return super.getBarColor(stack);
    }

    @Override
    @Nonnull
    public Optional<TooltipComponent> getTooltipImage(@Nonnull ItemStack stack) { // TODO move to item
        var elementStorageNbt = getElementStorageTag(stack);

        if (elementStorageNbt != null) {
            ElementType elementType = ElementType.byName(elementStorageNbt.getString(ECNames.ELEMENT_TYPE));
            int amount = elementStorageNbt.getInt(ECNames.ELEMENT_AMOUNT);
            int capacity = elementStorageNbt.getInt(ECNames.ELEMENT_CAPACITY);

            if (amount > 0) {
                return Optional.of(new AbstractElementContainerBlock.Tooltip(elementType, amount, capacity));
            }
        }
        return Optional.empty();
    }

    private CompoundTag getElementStorageTag(@Nonnull ItemStack stack) {
        CompoundTag tag = stack.getTag();

        if (tag != null && tag.contains(ECNames.BLOCK_ENTITY_TAG)) {
            CompoundTag blockNbt = tag.getCompound(ECNames.BLOCK_ENTITY_TAG);

            if (blockNbt.contains(ECNames.ELEMENT_STORAGE)) {
                return blockNbt.getCompound(ECNames.ELEMENT_STORAGE);
            }
        }
        return null;
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
