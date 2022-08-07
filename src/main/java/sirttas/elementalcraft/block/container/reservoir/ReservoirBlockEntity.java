package sirttas.elementalcraft.block.container.reservoir;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.block.container.AbstractElementContainerBlockEntity;
import sirttas.elementalcraft.config.ECConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ReservoirBlockEntity extends AbstractElementContainerBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + ReservoirBlock.NAME) public static final BlockEntityType<ReservoirBlockEntity> TYPE = null;	
	
	public ReservoirBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, r -> new ReservoirElementStorage(ElementType.getElementType(state), ECConfig.COMMON.reservoirCapacity.get(), r));
	}

	@Override
	public boolean isSmall() {
		return false;
	}
	
	@Override
	public void setChanged() {
		if (this.hasLevel() && getBlockState().getValue(ReservoirBlock.HALF) == DoubleBlockHalf.UPPER) {
			var lower = getLower();

			if (lower != null) {
				lower.setChanged();
			}
		}
		super.setChanged();
	}

	@Override
	public ISingleElementStorage getElementStorage() {
		if (getBlockState().getValue(ReservoirBlock.HALF) == DoubleBlockHalf.UPPER) {
			var lower = getLower();

			if (lower != null) {
				return lower.getElementStorage();
			}
		}
		return elementStorage;
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
		if (!this.remove && cap == CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY && this.getBlockState().getValue(ReservoirBlock.HALF) == DoubleBlockHalf.UPPER) {
			var lower = getLower();

			return lower != null ? lower.getCapability(cap, side) : LazyOptional.empty();
		}
		return super.getCapability(cap, side);
	}
}
