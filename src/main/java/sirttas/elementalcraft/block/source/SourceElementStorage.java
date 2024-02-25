package sirttas.elementalcraft.block.source;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.StaticElementStorage;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.ECItems;

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

		if (!simulate) {
			checkExhausted();
		}
		return value;
	}

	@Override
	public int extractElement(int count, ElementType type, boolean simulate) {
		if (Boolean.TRUE.equals(ECConfig.COMMON.disableSourceExhaustion.get())) {
			return count;
		} else if (!exhausted) {
			int value = super.extractElement(count, type, simulate);

			if (!simulate) {
				checkExhausted();
			}
			return value;
		}
		return 0;
	}

	@Override
	public int transferTo(IElementStorage other, ElementType type, float count, float multiplier) {
		var traitHolder = source.getTraitHolder();

		return super.transferTo(other, type, count * traitHolder.getSpeedModifier(), multiplier * traitHolder.getPreservationModifier());
	}
	
	@Override
	public boolean canPipeInsert(ElementType elementType, Direction side) {
		return false;
	}

	@Override
	public boolean canPipeExtract(ElementType elementType, Direction side) {
		return false;
	}

	@Override
	public boolean doesRenderGauge(Player player) {
		return source.isAnalyzed() && EntityHelper.handStream(player).anyMatch(i -> i.is(ECItems.SOURCE_ANALYSIS_GLASS.get()));
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

	public void setElementAmount(int amount) {
		this.elementAmount = amount;
		checkExhausted();
	}

	private void checkExhausted() {
		if (this.getElementAmount() <= 0) {
			this.exhausted = true;
		} else if (this.getElementAmount() >= this.elementCapacity) {
			this.exhausted = false;
		}
	}
}
