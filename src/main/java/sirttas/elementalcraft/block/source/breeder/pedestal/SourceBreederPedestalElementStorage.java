package sirttas.elementalcraft.block.source.breeder.pedestal;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.StaticElementStorage;
import sirttas.elementalcraft.config.ECConfig;

public class SourceBreederPedestalElementStorage extends StaticElementStorage {

    private final SourceBreederPedestalBlockEntity blockEntity;

    public SourceBreederPedestalElementStorage(SourceBreederPedestalBlockEntity blockEntity) {
        super(ElementType.NONE, ECConfig.COMMON.sourceBreederPedestalCapacity.get(), blockEntity::setChanged);
        this.blockEntity = blockEntity;
    }

    @Override
    public int insertElement(int count, ElementType type, boolean simulate) {
        refreshElement();
        return super.insertElement(count, type, simulate);
    }

    @Override
    public int extractElement(int count, ElementType type, boolean simulate) {
        refreshElement();
        return super.extractElement(count, type, simulate);
    }

    void refreshElement() {
        var oldType = this.elementType;
        this.elementType = blockEntity.getElementType();

        if (oldType != this.elementType) {
            this.elementAmount = 0;
            blockEntity.setChanged();
        }
    }

    @Override
    public boolean canPipeExtract(ElementType elementType, Direction side) {
        return false;
    }

    @Override
    public boolean doesRenderGauge(Player player) {
        return true;
    }
}
