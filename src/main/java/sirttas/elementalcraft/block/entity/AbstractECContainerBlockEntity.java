package sirttas.elementalcraft.block.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.inventory.IInventoryBlockEntity;

public abstract class AbstractECContainerBlockEntity extends AbstractECBlockEntity implements Clearable, IInventoryBlockEntity, ContainerListener {

	private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(this::createHandler);

	protected AbstractECContainerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
		this.clearContent();
		super.onDataPacket(net, packet);
	}

	@Nonnull
	protected IItemHandler createHandler() {
		return new InvWrapper(this.getInventory());
	}

	@Override
	public void clearContent() {
		this.getInventory().clearContent();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		Container inv = getInventory();

		if (inv instanceof INBTSerializable && compound.contains(ECNames.INVENTORY)) {
			((INBTSerializable) inv).deserializeNBT(compound.get(ECNames.INVENTORY));
		}
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
		Container inv = getInventory();

		if (inv instanceof INBTSerializable) {
			compound.put(ECNames.INVENTORY, ((INBTSerializable<?>) inv).serializeNBT());
		}
		return compound;
	}

	@Override
	@Nonnull
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (!this.remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return getItemHandler().cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public LazyOptional<IItemHandler> getItemHandler() {
		return itemHandler;
	}

	@Override
	public void containerChanged(Container invBasic) {
		this.setChanged();
	}
}
