package sirttas.elementalcraft.block.tile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.IClearable;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import sirttas.elementalcraft.inventory.IInventoryTile;
import sirttas.elementalcraft.nbt.ECNames;

public abstract class TileECContainer extends TileECTickable implements IClearable, IInventoryTile, IInventoryChangedListener {

	private LazyOptional<IItemHandler> itemHandler = LazyOptional.of(this::createUnSidedHandler);

	public TileECContainer(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		this.clear();
		super.onDataPacket(net, packet);
	}

	@Nonnull
	protected IItemHandler createUnSidedHandler() {
		return new InvWrapper(this.getInventory());
	}

	@Override
	public void clear() {
		this.getInventory().clear();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		IInventory inv = getInventory();

		if (inv instanceof INBTSerializable && compound.contains(ECNames.INVENTORY)) {
			((INBTSerializable) inv).deserializeNBT(compound.get(ECNames.INVENTORY));
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		IInventory inv = getInventory();

		if (inv instanceof INBTSerializable) {
			compound.put(ECNames.INVENTORY, ((INBTSerializable<?>) inv).serializeNBT());
		}
		return compound;
	}

	@Override
	@Nonnull
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (!this.removed && cap == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return itemHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void onInventoryChanged(IInventory invBasic) {
		this.forceSync();
	}
}
