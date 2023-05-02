package sirttas.elementalcraft.block.shrine;

import net.minecraft.core.Direction;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.StaticElementStorage;

import javax.annotation.Nonnull;

public class ShrineElementStorage extends StaticElementStorage {

	private final AbstractShrineBlockEntity shrine;

	public ShrineElementStorage(@Nonnull AbstractShrineBlockEntity shrine) {
		super(ElementType.NONE, 0, shrine::setChanged);
		this.shrine = shrine;
	}

	@Override
	public boolean canPipeExtract(ElementType elementType, Direction side) {
		return false;
	}

	@Override
	public boolean doesRenderGauge() {
		return true;
	}

	@Nonnull
	public AbstractShrineBlockEntity getShrine() {
		return shrine;
	}

	void refresh() {
		this.elementType = shrine.getElementType();
		this.elementCapacity = shrine.getCapacity();
	}
}
