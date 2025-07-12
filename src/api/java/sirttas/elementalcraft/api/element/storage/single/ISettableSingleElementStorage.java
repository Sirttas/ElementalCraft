package sirttas.elementalcraft.api.element.storage.single;

import sirttas.elementalcraft.api.element.ElementType;

public interface ISettableSingleElementStorage extends ISingleElementStorage {

    void setElementAmount(int newCount);

    @Override
    default int insertElement(int count, ElementType type, boolean simulate) {
        var elementType = getElementType();
        var elementCapacity = getElementCapacity();
        var elementAmount = getElementAmount();

        if (type != elementType) {
            return count;
        }

        int newCount = Math.min(elementAmount + count, elementCapacity);

        if (newCount < 0) { // overflow
            newCount = elementCapacity;
        }

        if (!simulate) {
            setElementAmount(newCount);
        }
        return count - (newCount - elementAmount);
    }

    @Override
    default int extractElement(int count, ElementType type, boolean simulate) {
        var elementType = getElementType();
        var elementCapacity = getElementCapacity();
        var elementAmount = getElementAmount();

        if (type != elementType) {
            return 0;
        }

        int newCount = Math.max(elementAmount - count, 0);

        if (newCount > elementCapacity) { // underflow
            newCount = 0;
        }

        if (!simulate) {
            setElementAmount(newCount);
        }
        return elementAmount - newCount;
    }

}
