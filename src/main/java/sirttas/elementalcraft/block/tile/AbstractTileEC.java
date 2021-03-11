package sirttas.elementalcraft.block.tile;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;

public abstract class AbstractTileEC extends TileEntity {

	protected AbstractTileEC(TileEntityType<?> tileEntityTypeIn) {
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

	protected void sendUpdate() {
		SUpdateTileEntityPacket packet = getUpdatePacket();

		if (world instanceof ServerWorld) {
			PacketDistributor.TRACKING_CHUNK.with(() -> ((ServerWorld) world).getChunkAt(pos)).send(packet);
		}
	}

}
