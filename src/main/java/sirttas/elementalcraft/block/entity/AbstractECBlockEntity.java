package sirttas.elementalcraft.block.entity;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;

public abstract class AbstractECBlockEntity extends TileEntity {

	protected AbstractECBlockEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	public boolean isPowered() {
		return this.hasLevel() && this.getLevel().hasNeighborSignal(this.getBlockPos());
	}

	@Override
	public final SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		load(this.getBlockState(), packet.getTag());
	}

	@Nonnull
	@Override
	public final CompoundNBT getUpdateTag() {
		return save(new CompoundNBT());
	}

	protected void sendUpdate() {
		SUpdateTileEntityPacket packet = getUpdatePacket();

		if (level instanceof ServerWorld) {
			PacketDistributor.TRACKING_CHUNK.with(() -> ((ServerWorld) level).getChunkAt(worldPosition)).send(packet);
		}
	}

}
