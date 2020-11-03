package sirttas.elementalcraft.block.tile;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.tank.TileTank;
import sirttas.elementalcraft.network.NetworkHelper;
import sirttas.elementalcraft.tag.ECTags;

public abstract class TileECTickable extends TileEC implements ITickableTileEntity, IForcableSync {

	public TileECTickable(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	private boolean toSync = true;

	@Override
	public void forceSync() {
		toSync = true;
	}

	private void sync() {
		if (toSync) {
			BlockState bs = this.getWorld().getBlockState(pos);

			this.getWorld().notifyBlockUpdate(pos, bs, bs, 3);
			markDirty();
			this.getWorld().notifyNeighborsOfStateChange(pos, getBlockState().getBlock());
			NetworkHelper.dispatchTEToNearbyPlayers(this);
			toSync = false;
		}
	}

	@Override
	public void tick() {
		sync();
	}

	@Override
	public void markDirty() {
		this.forceSync();
		super.markDirty();
	}

	protected final boolean isToSync() {
		return toSync;
	}

	// TODO extract (capability ?)
	public TileTank getTank() {
		return getTileEntityAs(pos.down(), TileTank.class).filter(t -> !t.isSmall() || ECTags.Blocks.SMALL_TANK_COMPATIBLES.contains(this.getBlockState().getBlock())).orElse(null);
	}

	// TODO extract (capability ?)
	public ElementType getTankElementType() {
		TileTank tank = getTank();

		return tank != null ? tank.getElementType() : ElementType.NONE;
	}
}
