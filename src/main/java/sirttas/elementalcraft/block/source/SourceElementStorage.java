package sirttas.elementalcraft.block.source;

import net.minecraft.core.Direction;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.StaticElementStorage;
import sirttas.elementalcraft.config.ECConfig;

public class SourceElementStorage extends StaticElementStorage {

	private boolean exhausted = false;
	private final SourceBlockEntity source;
	
	public SourceElementStorage(SourceBlockEntity source) {
		super(ElementType.NONE, 500000, source::setChanged);
		this.source = source;
		this.elementAmount = this.elementCapacity;
	}

	protected void setElementType(ElementType elementType) {
		this.elementType = elementType;
	}

	@Override
	public int insertElement(int count, ElementType type, boolean simulate) {
		int value = super.insertElement(count, type, simulate);

		if (!simulate && this.getElementAmount() >= this.elementCapacity) {
			this.exhausted = false;
		}
		return value;
	}

	@Override
	public int extractElement(int count, ElementType type, boolean simulate) {
		if (Boolean.TRUE.equals(ECConfig.COMMON.disableSourceExhaustion.get())) {
			return count;
		} else if (!exhausted) {
			int value = super.extractElement(count, type, simulate);

			if (!simulate && this.getElementAmount() <= 0) {
				this.exhausted = true;
			}
			return value;
		}
		return 0;
	}

	@Override
	public int transferTo(IElementStorage other, ElementType type, float count, float multiplier) {
		return super.transferTo(other, type, count * source.getSpeedModifier(), multiplier * source.getPreservationModifier());
	}
	
	@Override
	public boolean canPipeInsert(ElementType elementType, Direction side) {
		return false;
	}

	@Override
	public boolean canPipeExtract(ElementType elementType, Direction side) {
		return false;
	}

	public boolean isExhausted() {
		return exhausted;
	}

	protected void setExhausted(boolean exhausted) {
		this.exhausted = exhausted;
	}
	
	protected void setElementCapacity(int elementCapacity) {
		this.elementCapacity = elementCapacity;
	}

}
