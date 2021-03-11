package sirttas.elementalcraft.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.EmptyElementStorage;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.StaticElementStorage;

public class WorldElementStorage implements IElementStorage {
	
	private final SubStorage fire;
	private final SubStorage water;
	private final SubStorage earth;
	private final SubStorage air;

	private WorldElementStorage() {
		this(new SubStorage(ElementType.FIRE), new SubStorage(ElementType.WATER), new SubStorage(ElementType.EARTH), new SubStorage(ElementType.AIR));
	}

	private WorldElementStorage(SubStorage fire, SubStorage water, SubStorage earth, SubStorage air) {
		this.fire = fire;
		this.water = water;
		this.earth = earth;
		this.air = air;
	}

	public static ICapabilityProvider createProvider() {
		return CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY != null ? new CapabilityProvider(new WorldElementStorage()) : null;
	}

	@Override
	public int getElementAmount(ElementType type) {
		return getSubStorage(type).getElementAmount(type);
	}

	@Override
	public int getElementCapacity(ElementType type) {
		return getSubStorage(type).getElementCapacity(type);
	}
	
	@Override
	public int insertElement(int count, ElementType type, boolean simulate) {
		return getSubStorage(type).insertElement(count, type, simulate);
	}

	@Override
	public int extractElement(int count, ElementType type, boolean simulate) {
		return getSubStorage(type).extractElement(count, type, simulate);
	}

	public CompoundNBT writeNBT() {
		CompoundNBT compound = new CompoundNBT();

		compound.put(ElementType.FIRE.getString(), fire.writeNBT());
		compound.put(ElementType.WATER.getString(), water.writeNBT());
		compound.put(ElementType.EARTH.getString(), earth.writeNBT());
		compound.put(ElementType.AIR.getString(), air.writeNBT());
		return compound;
	}

	public void readNBT(CompoundNBT compound) {
		fire.readNBT(compound.getCompound(ElementType.FIRE.getString()));
		water.readNBT(compound.getCompound(ElementType.WATER.getString()));
		earth.readNBT(compound.getCompound(ElementType.EARTH.getString()));
		air.readNBT(compound.getCompound(ElementType.AIR.getString()));
	}
	
	private IElementStorage getSubStorage(ElementType elementType) {
		switch (elementType) {
		case FIRE:
			return fire;
		case AIR:
			return air;
		case EARTH:
			return earth;
		case WATER:
			return water;
		default:
			return EmptyElementStorage.INSTANCE;
		}
	}

	private static class SubStorage extends StaticElementStorage {
		
		public SubStorage(ElementType type, int elementAmount, int elementCapacity) {
			super(type, elementCapacity);
			this.elementAmount = elementAmount;
		}

		public SubStorage(ElementType type) {
			super(type, 1000000);
		}
	}
	
	private static class CapabilityProvider implements ICapabilitySerializable<CompoundNBT> {

		private final WorldElementStorage storage;
		
		
		public CapabilityProvider(WorldElementStorage storage) {
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
