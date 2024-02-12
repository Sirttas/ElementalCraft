package sirttas.elementalcraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.container.IContainerBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class AbstractECContainerBlockEntity extends AbstractECBlockEntity implements Clearable, IContainerBlockEntity, ContainerListener {

	protected AbstractECContainerBlockEntity(Supplier<? extends BlockEntityType<?>> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
	}

	@Override
	public void onDataPacket(@Nonnull Connection net, @Nonnull ClientboundBlockEntityDataPacket packet) {
		this.clearContent();
		super.onDataPacket(net, packet);
	}

	@Override
	public void clearContent() {
		this.getInventory().clearContent();
	}

	@Override
	public void load(@Nonnull CompoundTag compound) {
		super.load(compound);
		Container inv = getInventory();

		if (inv instanceof INBTSerializable nbtInv && compound.contains(ECNames.INVENTORY)) {
			nbtInv.deserializeNBT(compound.get(ECNames.INVENTORY));
		}
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);
		Container inv = getInventory();

		if (inv instanceof INBTSerializable nbtInv) {
			compound.put(ECNames.INVENTORY, nbtInv.serializeNBT());
		}
	}

	@Override
	@Nonnull
	public IItemHandler getItemHandler(@Nullable Direction direction) {
		var inv = this.getInventory();

		if (inv instanceof WorldlyContainer worldContainer) {
			return new SidedInvWrapper(worldContainer, direction);
		}
		return new InvWrapper(this.getInventory());
	}

	@Override
	public void containerChanged(@Nonnull Container invBasic) {
		this.setChanged();
	}
}
