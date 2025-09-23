package sirttas.elementalcraft.block.source;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.ECItems;

public class SourceElementStorage extends SingleElementStorage {

	public static final int DEFAULT_CAPACITY = 5000000;

	private final SourceBlockEntity source;
	
	public SourceElementStorage(SourceBlockEntity source) {
		super(ElementType.NONE, DEFAULT_CAPACITY, source::setChanged);
		this.source = source;
		this.elementAmount = this.elementCapacity;
	}

	protected void setElementType(ElementType elementType) {
		this.elementType = elementType;
	}

	public SourceBlockEntity getSource() {
		return source;
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
		return source.isAnalyzed() || EntityHelper.handStream(player).anyMatch(i -> i.is(ECItems.SOURCE_ANALYSIS_GLASS.get()));
	}
	
	protected void setElementCapacity(int elementCapacity) {
		this.elementCapacity = elementCapacity;
	}
}
