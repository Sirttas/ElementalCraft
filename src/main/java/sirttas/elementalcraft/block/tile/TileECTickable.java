package sirttas.elementalcraft.block.tile;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.block.tank.IElementContainer;
import sirttas.elementalcraft.tag.ECTags;

public abstract class TileECTickable extends TileEC implements ITickableTileEntity {

	private boolean dirty = true;
	private int refreshTick = 0;

	public TileECTickable(TileEntityType<?> tileEntityTypeIn) {
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
