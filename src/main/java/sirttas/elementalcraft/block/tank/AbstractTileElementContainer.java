package sirttas.elementalcraft.block.tank;

import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.tile.AbstractTileECTickable;

public abstract class AbstractTileElementContainer extends AbstractTileECTickable implements IElementContainer {

	protected final SingleElementStorage elementStorage;

	protected AbstractTileElementContainer(TileEntityType<?> tileEntityTypeIn, Function<Runnable, SingleElementStorage> elementStorage) {
		super(tileEntityTypeIn);
		this.elementStorage = elementStorage.apply(this::markDirty);
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		if (compound.contains(ECNames.ELEMENT_STORAGE)) {
			elementStorage.readNBT(compound.getCompound(ECNames.ELEMENT_STORAGE));
		} else { // TODO 1.17 remove
			elementStorage.readNBT(compound);
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put(ECNames.ELEMENT_STORAGE, elementStorage.writeNBT());
		return compound;
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(Capability<U> cap, @Nullable Direction side) {
		if (!this.removed && cap == CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY) {
			return LazyOptional.of(elementStorage != null ? () -> elementStorage : null).cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public ISingleElementStorage getElementStorage() {
		return elementStorage;
	}

}