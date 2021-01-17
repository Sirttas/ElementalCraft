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
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.tile.TileECTickable;

public abstract class AbstractTileElementContainer extends TileECTickable implements IElementContainer {

	protected final IElementStorage elementStorage;

	public AbstractTileElementContainer(TileEntityType<?> tileEntityTypeIn, Function<Runnable, IElementStorage> elementStorage) {
		super(tileEntityTypeIn);
		this.elementStorage = elementStorage.apply(this::markDirty);
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		if (compound.contains(ECNames.ELEMENT_STORAGE)) {
			CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY.readNBT(elementStorage, null, compound.get(ECNames.ELEMENT_STORAGE));
		} else { // TODO 1.17 remove
			CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY.readNBT(elementStorage, null, compound);
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put(ECNames.ELEMENT_STORAGE, CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY.writeNBT(elementStorage, null));
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
	public IElementStorage getElementStorage() {
		return elementStorage;
	}

}