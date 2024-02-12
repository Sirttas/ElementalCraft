package sirttas.elementalcraft.block.shrine.upgrade.acceleration.overclocked;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.element.transfer.IElementTransferer;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;

import javax.annotation.Nonnull;

public class OverclockedAccelerationShrineUpgradeBlockEntity extends AbstractECBlockEntity {

    private final IElementTransferer transferer;

    public OverclockedAccelerationShrineUpgradeBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE, pos, state);
        transferer = new OverclockedAccelerationShrineUpgradeElementTransferer(this);
    }

    @Nonnull
    public IElementTransferer getTransferer() {
        return transferer;
    }
}
