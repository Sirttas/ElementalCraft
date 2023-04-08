package sirttas.elementalcraft.block.container.reservoir;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.block.container.AbstractElementContainerBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.config.ECConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ReservoirBlockEntity extends AbstractElementContainerBlockEntity {
	
	public ReservoirBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.RESERVOIR, pos, state, self -> new ReservoirElementStorage(ElementType.getElementType(state), ECConfig.COMMON.reservoirCapacity.get(), self::setChanged));
	}

	@Override
	public boolean isSmall() {
		return false;
	}
	
	@Override
	public void setChanged() {
		if (isUpper()) {
			var lower = getLower();

			if (lower != null) {
				lower.setChanged();
			}
		}
		super.setChanged();
	}

	@Override
	public ISingleElementStorage getElementStorage() {
		if (isUpper()) {
			var lower = getLower();

			if (lower != null) {
				return lower.getElementStorage();
			}
		}
		return elementStorage;
	}

	private boolean isUpper() {
		return getBlockState().getValue(ReservoirBlock.HALF) == DoubleBlockHalf.UPPER;
	}

	@Nullable
	private ReservoirBlockEntity getLower() {
		if (level == null) {
			return null;
		}
		return (ReservoirBlockEntity) level.getBlockEntity(worldPosition.below());
	}
	
	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
		if (!this.remove && cap == ElementalCraftCapabilities.ELEMENT_STORAGE && isUpper()) {
			var lower = getLower();

			return lower != null ? lower.getCapability(cap, side) : LazyOptional.empty();
		}
		return super.getCapability(cap, side);
	}
}
