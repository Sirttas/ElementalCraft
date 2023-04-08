package sirttas.elementalcraft.api.element.storage;

import net.minecraft.core.Direction;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorageWrapper;

import javax.annotation.Nullable;

public class EmptyElementStorage implements IElementStorage {

	public static final IElementStorage INSTANCE = new EmptyElementStorage();

	private EmptyElementStorage() {}

	
	public static ISingleElementStorage getSingle(ElementType type) {
		return new SingleElementStorageWrapper(type, INSTANCE);
	}
	
	@Override
	public int getElementAmount(ElementType type) {
		return 0;
	}

	@Override
	public int getElementCapacity(ElementType type) {
		return 0;
	}

	@Override
	public int insertElement(int count, ElementType type, boolean simulate) {
		return count;
	}

	@Override
	public int extractElement(int count, ElementType type, boolean simulate) {
		return 0;
	}

	@Override
	public ISingleElementStorage forElement(ElementType type) {
		return getSingle(type);
	}

	@Override
	public boolean canPipeInsert(ElementType type, @Nullable Direction side) {
		return false;
	}

	@Override
	public boolean canPipeExtract(ElementType type, @Nullable Direction side) {
		return false;
	}
}
