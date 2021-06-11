package sirttas.elementalcraft.block.container;

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
import sirttas.elementalcraft.block.entity.AbstractECTickableBlockEntity;

public abstract class AbstractElementContainerBlockEntity extends AbstractECTickableBlockEntity implements IElementContainer {

	protected final SingleElementStorage elementStorage;

	protected AbstractElementContainerBlockEntity(TileEntityType<?> tileEntityTypeIn, Function<Runnable, SingleElementStorage> elementStorage) {
		super(tileEntityTypeIn);
		this.elementStorage = elementStorage.apply(this::setChanged);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		if (compound.contains(ECNames.ELEMENT_STORAGE)) {
			elementStorage.deserializeNBT(compound.getCompound(ECNames.ELEMENT_STORAGE));
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.put(ECNames.ELEMENT_STORAGE, elementStorage.serializeNBT());
		return compound;
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(Capability<U> cap, @Nullable Direction side) {
		if (!this.remove && cap == CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY) {
			return LazyOptional.of(elementStorage != null ? () -> elementStorage : null).cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public ISingleElementStorage getElementStorage() {
		return elementStorage;
	}

}