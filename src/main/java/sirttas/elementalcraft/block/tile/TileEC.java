package sirttas.elementalcraft.block.tile;

import java.util.Optional;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import sirttas.elementalcraft.block.BlockEC;

public abstract class TileEC extends TileEntity {

	public TileEC(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	public boolean isPowered() {
		return this.hasWorld() && this.getWorld().isBlockPowered(this.getPos());
	}

	@Override
	public final SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		read(this.getBlockState(), packet.getNbtCompound());
	}

	@Nonnull
	@Override
	public final CompoundNBT getUpdateTag() {
		return write(new CompoundNBT());
	}

	public <T> Optional<T> getTileEntityAs(BlockPos pos, Class<T> clazz) {
		return this.hasWorld() ? BlockEC.getTileEntityAs(this.getWorld(), pos, clazz) : Optional.empty();
	}

}
