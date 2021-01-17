package sirttas.elementalcraft.block.tile;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.block.tank.IElementContainer;
import sirttas.elementalcraft.tag.ECTags;

public abstract class TileECTickable extends TileEC implements ITickableTileEntity {

	private boolean dirty = true;

	public TileECTickable(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	@Override
	public void tick() {
		if (isDirty()) {
			super.markDirty();
			this.sendUpdate();
			dirty = false;
		}
	}

	@Override
	public void markDirty() {
		dirty = true;
	}

	public boolean isDirty() {
		return dirty && this.world.isRemote();
	}

	// TODO extract (capability ?)
	public IElementStorage getTank() {
		return getTileEntityAs(pos.down(), IElementContainer.class).filter(t -> !t.isSmall() || ECTags.Blocks.SMALL_TANK_COMPATIBLES.contains(this.getBlockState().getBlock()))
				.map(IElementContainer::getElementStorage).orElse(null);
	}

	// TODO extract (capability ?)
	public ElementType getTankElementType() {
		IElementStorage tank = getTank();

		return tank != null ? tank.getElementType() : ElementType.NONE;
	}
}
