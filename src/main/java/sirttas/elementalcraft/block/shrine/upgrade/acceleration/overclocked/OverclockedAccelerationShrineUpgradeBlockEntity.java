package sirttas.elementalcraft.block.shrine.upgrade.acceleration.overclocked;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.transfer.IElementTransferer;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class OverclockedAccelerationShrineUpgradeBlockEntity extends AbstractECBlockEntity {

    private final IElementTransferer transferer;

    public OverclockedAccelerationShrineUpgradeBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE, pos, state);
        transferer = new OverclockedAccelerationShrineUpgradeElementTransferer(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && (cap == ElementalCraftCapabilities.ELEMENT_TRANSFERER)) {
            return LazyOptional.of(() -> (T) this.transferer);
        }
        return super.getCapability(cap, side);
    }
}
