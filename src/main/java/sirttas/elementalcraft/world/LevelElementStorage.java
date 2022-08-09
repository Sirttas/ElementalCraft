package sirttas.elementalcraft.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.EmptyElementStorage;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.StaticElementStorage;

public class LevelElementStorage implements IElementStorage, INBTSerializable<CompoundTag> {
	
	private final SubStorage fire;
	private final SubStorage water;
	private final SubStorage earth;
	private final SubStorage air;

	private LevelElementStorage() {
		this(new SubStorage(ElementType.FIRE), new SubStorage(ElementType.WATER), new SubStorage(ElementType.EARTH), new SubStorage(ElementType.AIR));
	}

	private LevelElementStorage(SubStorage fire, SubStorage water, SubStorage earth, SubStorage air) {
		this.fire = fire;
		this.water = water;
		this.earth = earth;
		this.air = air;
	}

	public static ICapabilityProvider createProvider() {
		return CapabilityElementStorage.createProvider(new LevelElementStorage());
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

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag compound = new CompoundTag();

		compound.put(ElementType.FIRE.getSerializedName(), fire.serializeNBT());
		compound.put(ElementType.WATER.getSerializedName(), water.serializeNBT());
		compound.put(ElementType.EARTH.getSerializedName(), earth.serializeNBT());
		compound.put(ElementType.AIR.getSerializedName(), air.serializeNBT());
		return compound;
	}

	@Override
	public void deserializeNBT(CompoundTag compound) {
		fire.deserializeNBT(compound.getCompound(ElementType.FIRE.getSerializedName()));
		water.deserializeNBT(compound.getCompound(ElementType.WATER.getSerializedName()));
		earth.deserializeNBT(compound.getCompound(ElementType.EARTH.getSerializedName()));
		air.deserializeNBT(compound.getCompound(ElementType.AIR.getSerializedName()));
	}
	
	private IElementStorage getSubStorage(ElementType elementType) {
		return switch (elementType) {
			case FIRE -> fire;
			case AIR -> air;
			case EARTH -> earth;
			case WATER -> water;
			default -> EmptyElementStorage.INSTANCE;
		};
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
}
