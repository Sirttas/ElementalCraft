package sirttas.elementalcraft.block.source.breeder.pedestal;

import net.minecraft.core.Direction;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.StaticElementStorage;
import sirttas.elementalcraft.config.ECConfig;

public class SourceBreederPedestalElementContainer extends StaticElementStorage {

    private final SourceBreederPedestalBlockEntity blockEntity;

    public SourceBreederPedestalElementContainer(SourceBreederPedestalBlockEntity blockEntity) {
        super(ElementType.NONE, ECConfig.COMMON.sourceBreederPedestalCapacity.get(), blockEntity::setChanged);
        this.blockEntity = blockEntity;
    }

    @Override
    public int insertElement(int count, ElementType type, boolean simulate) {
        this.elementType = blockEntity.getElementType();
        return super.insertElement(count, type, simulate);
    }

    @Override
    public int extractElement(int count, ElementType type, boolean simulate) {
        this.elementType = blockEntity.getElementType();
        return super.extractElement(count, type, simulate);
    }

    @Override
    public boolean canPipeExtract(ElementType elementType, Direction side) {
        return false;
    }
}
