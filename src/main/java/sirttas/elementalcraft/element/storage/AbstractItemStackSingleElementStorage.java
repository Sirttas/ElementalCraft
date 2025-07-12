package sirttas.elementalcraft.element.storage;

import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.element.storage.single.ISettableSingleElementStorage;
import sirttas.elementalcraft.component.ECDataComponents;

public abstract class AbstractItemStackSingleElementStorage implements ISettableSingleElementStorage {

    protected final ItemStack stack;

    protected AbstractItemStackSingleElementStorage(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public int getElementAmount() {
        return stack.getOrDefault(ECDataComponents.ELEMENT_AMOUNT, 0);
    }

    @Override
    public void setElementAmount(int newCount) {
        stack.set(ECDataComponents.ELEMENT_AMOUNT, newCount);
    }
}
