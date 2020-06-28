package sirttas.elementalcraft.block.tile;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import sirttas.elementalcraft.nbt.ECNBTTags;

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
		read(packet.getNbtCompound());
	}

	@Nonnull
	@Override
	public final CompoundNBT getUpdateTag() {
		return write(new CompoundNBT());
	}

	public void readFromItemStack(ItemStack stack) {
		if (stack == null || stack.getTag() == null) {
			return;
		}
		CompoundNBT root = stack.getTag();
		if (!root.contains(ECNBTTags.EC_NBT_TE)) {
			return;
		}
		this.read(root);
	}

	public void writeToItemStack(ItemStack stack) {
		if (stack == null) {
			return;
		}
		if (stack.getTag() == null) {
			stack.setTag(new CompoundNBT());
		}

		CompoundNBT root = stack.getTag();
		root.putBoolean(ECNBTTags.EC_NBT_TE, true);
		this.write(root);
	}

}
