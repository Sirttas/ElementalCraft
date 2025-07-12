package sirttas.elementalcraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public abstract class AbstractECBlockEntity extends BlockEntity {

	private boolean dirty = true;
	
	protected AbstractECBlockEntity(Supplier<? extends BlockEntityType<?>> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType.get(), pos, state);
	}

	@Override
	public void setChanged() {
		dirty = true;
	}

	public boolean isDirty() {
		return dirty;
	}

	@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "ConstantConditions"})
	public boolean isPowered() {
		return this.hasLevel() && this.getLevel().hasNeighborSignal(this.getBlockPos());
	}

	@Override
	public final ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Nonnull
	@Override
	public final CompoundTag getUpdateTag(@Nonnull HolderLookup.Provider provider) {
		return saveWithoutMetadata(provider);
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
			var packet = getUpdatePacket();

			serverLevel.getChunkSource().chunkMap.getPlayers(new ChunkPos(worldPosition), false).forEach(e -> e.connection.send(packet));
		}
	}
}
