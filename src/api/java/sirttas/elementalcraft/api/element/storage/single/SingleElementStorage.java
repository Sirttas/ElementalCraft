package sirttas.elementalcraft.api.element.storage.single;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.name.ECNames;

public class SingleElementStorage implements ISingleElementStorage {
	
	protected int elementAmount;
	protected int elementCapacity;
	protected ElementType elementType;
	private final Runnable syncCallback;
	

	public SingleElementStorage(int elementCapacity) {
		this(elementCapacity, null);
	}

	public SingleElementStorage(int elementCapacity, Runnable syncCallback) {
		this(ElementType.NONE, 0, elementCapacity, syncCallback);
	}

	public SingleElementStorage(ElementType elementType, int elementAmount, int elementCapacity) {
		this(elementType, elementAmount, elementCapacity, null);
	}
		
	private SingleElementStorage(ElementType elementType, int elementAmount, int elementCapacity, Runnable syncCallback) {
		this.elementCapacity = elementCapacity;
		this.elementAmount = elementAmount;
		this.elementType = elementType;
		this.syncCallback = syncCallback;
	}
	
	@Nullable
	public static ICapabilityProvider createProvider(SingleElementStorage storage) {
		return CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY != null ? new CapabilityProvider(storage) : null;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public int getElementCapacity() {
		return elementCapacity;
	}

	@Override
	public int insertElement(int count, ElementType type, boolean simulate) {
		if (type != this.elementType && this.elementType != ElementType.NONE) {
			return count - this.extractElement(count, simulate);
		} else {
			int newCount = Math.min(elementAmount + count, elementCapacity);
			int ret = count - newCount + elementAmount;

			if (!simulate) {
				elementAmount = newCount;
				if (this.elementType == ElementType.NONE) {
					this.elementType = type;
				}
				if (ret < count) {
					markDirty();
				}
			}
			return ret;
		}
	}

	@Override
	public int extractElement(int count, ElementType type, boolean simulate) {
		if (type != this.elementType) {
			return 0;
		}
		int newCount = Math.max(elementAmount - count, 0);
		int ret = elementAmount - newCount;

		if (!simulate) {
			elementAmount = newCount;
			if (this.elementAmount <= 0) {
				this.elementType = ElementType.NONE;
			}
			if (ret > 0) {
				markDirty();
			}
		}
		return ret;
	}

	public void markDirty() {
		if (syncCallback != null) {
			syncCallback.run();
		}
	}

	@Override
	public String toString() {
		return elementAmount + "/" + elementCapacity + " " + elementType.getString();
	}

	public CompoundNBT writeNBT() {
		CompoundNBT compound = new CompoundNBT();

		compound.putString(ECNames.ELEMENT_TYPE, getElementType().getString());
		compound.putInt(ECNames.ELEMENT_AMOUNT, getElementAmount());
		compound.putInt(ECNames.ELEMENT_CAPACITY, getElementCapacity());
		return compound;
	}

	public void readNBT(CompoundNBT compound) {
		elementType = ElementType.byName(compound.getString(ECNames.ELEMENT_TYPE));
		elementAmount = compound.getInt(ECNames.ELEMENT_AMOUNT);
		elementCapacity = compound.getInt(ECNames.ELEMENT_CAPACITY);

	}
	
	
	private static class CapabilityProvider implements ICapabilitySerializable<CompoundNBT> {

		private final SingleElementStorage storage;
		
		
		public CapabilityProvider(SingleElementStorage storage) {
			this.storage = storage;
		}

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			return CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> storage));
		}

		@Override
		public CompoundNBT serializeNBT() {
			return storage.writeNBT();
		}

		@Override
		public void deserializeNBT(CompoundNBT nbt) {
			storage.readNBT(nbt);
		}
	}
}
