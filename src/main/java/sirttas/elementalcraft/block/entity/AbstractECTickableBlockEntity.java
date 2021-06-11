package sirttas.elementalcraft.block.entity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;

public abstract class AbstractECTickableBlockEntity extends AbstractECBlockEntity implements ITickableTileEntity {

	private boolean dirty = true;
	private int refreshTick = 0;

	protected AbstractECTickableBlockEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	@Override
	public void tick() {
		if (!this.level.isClientSide()) {
			if (isDirty() && refreshTick > 10) {
				super.setChanged();
				this.sendUpdate();
				dirty = false;
				refreshTick = 0;
			}
			refreshTick++;
		}
	}

	@Override
	public void setChanged() {
		dirty = true;
	}

	public boolean isDirty() {
		return dirty;
	}

	// TODO extract (capability ?)
	public ISingleElementStorage getTank() {
		return this.hasLevel() ? BlockEntityHelper.getElementContainer(this.getBlockState().getBlock(), level, worldPosition.below()).orElse(null) : null;
	}

	// TODO extract (capability ?)
	public ElementType getTankElementType() {
		ISingleElementStorage tank = getTank();

		return tank != null ? tank.getElementType() : ElementType.NONE;
	}
}
