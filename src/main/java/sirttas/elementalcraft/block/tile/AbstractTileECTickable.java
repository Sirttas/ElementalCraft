package sirttas.elementalcraft.block.tile;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;

public abstract class AbstractTileECTickable extends AbstractTileEC implements ITickableTileEntity {

	private boolean dirty = true;
	private int refreshTick = 0;

	protected AbstractTileECTickable(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	@Override
	public void tick() {
		if (!this.world.isRemote()) {
			if (isDirty() && refreshTick > 10) {
				super.markDirty();
				this.sendUpdate();
				dirty = false;
				refreshTick = 0;
			}
			refreshTick++;
		}
	}

	@Override
	public void markDirty() {
		dirty = true;
	}

	public boolean isDirty() {
		return dirty;
	}

	// TODO extract (capability ?)
	public ISingleElementStorage getTank() {
		return this.hasWorld() ? TileEntityHelper.getElementContainer(this.getBlockState().getBlock(), world, pos.down()).orElse(null) : null;
	}

	// TODO extract (capability ?)
	public ElementType getTankElementType() {
		ISingleElementStorage tank = getTank();

		return tank != null ? tank.getElementType() : ElementType.NONE;
	}
}
