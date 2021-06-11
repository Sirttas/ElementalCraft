package sirttas.elementalcraft.block.entity;

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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.inventory.IInventoryTile;

public abstract class AbstractECContainerBlockEntity extends AbstractECTickableBlockEntity implements IClearable, IInventoryTile, IInventoryChangedListener {

	private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(this::createHandler);

	protected AbstractECContainerBlockEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
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
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		IInventory inv = getInventory();

		if (inv instanceof INBTSerializable && compound.contains(ECNames.INVENTORY)) {
			((INBTSerializable) inv).deserializeNBT(compound.get(ECNames.INVENTORY));
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		IInventory inv = getInventory();

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

	public LazyOptional<IItemHandler> getItemHandler() {
		return itemHandler;
	}

	@Override
	public void containerChanged(IInventory invBasic) {
		this.setChanged();
	}
}
