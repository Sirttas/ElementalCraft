package sirttas.elementalcraft.block.entity;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

public abstract class AbstractECBlockEntity extends BlockEntity {

	private boolean dirty = true;
	
	protected AbstractECBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
	}

	@Override
	public void setChanged() {
		dirty = true;
	}

	public boolean isDirty() {
		return dirty;
	}
	
	public boolean isPowered() {
		return this.hasLevel() && this.getLevel().hasNeighborSignal(this.getBlockPos());
	}

	@Override
	public final ClientboundBlockEntityDataPacket getUpdatePacket() {
		return new ClientboundBlockEntityDataPacket(worldPosition, 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
		load(packet.getTag());
	}

	@Nonnull
	@Override
	public final CompoundTag getUpdateTag() {
		return save(new CompoundTag());
	}

	public void sendUpdate() {
		if (isDirty()) {
			super.setChanged();
			this.sendUpdatePacket();
			dirty = false;
		}
	}
	
	private void sendUpdatePacket() {
		if (level instanceof ServerLevel serverLevel) {
			PacketDistributor.TRACKING_CHUNK.with(() -> serverLevel.getChunkAt(worldPosition)).send(getUpdatePacket());
		}
	}

}
