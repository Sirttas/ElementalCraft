package sirttas.elementalcraft.block.pureinfuser;

import java.util.stream.Stream;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementReceiver;
import sirttas.elementalcraft.block.tile.TileECContainer;
import sirttas.elementalcraft.inventory.SingleItemInventory;
import sirttas.elementalcraft.nbt.ECNames;

public class TilePedestal extends TileECContainer implements IElementReceiver {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME) public static TileEntityType<TilePedestal> TYPE;

	private int elementAmount = 0;
	protected int elementCapacity = 10000; // TODO CONFIG
	private final SingleItemInventory inventory;

	public TilePedestal() {
		super(TYPE);
		inventory = new SingleItemInventory(this::forceSync);
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public int getElementCapacity() {
		return elementCapacity;
	}

	@Override
	public ElementType getElementType() {
		return ((BlockPedestal) this.getBlockState().getBlock()).getElementType();
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		elementAmount = compound.getInt(ECNames.ELEMENT_AMOUNT);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt(ECNames.ELEMENT_AMOUNT, elementAmount);
		return compound;
	}

	@Override
	public int inserElement(int count, ElementType type, boolean simulate) {
		if (type != this.getElementType()) {
			return 0;
		} else {
			int newCount = Math.min(elementAmount + count, elementCapacity);
			int ret = count - newCount + elementAmount;

			if (!simulate) {
				elementAmount = newCount;
			}
			return ret;
		}
	}

	public int consumeElement(int i) {
		int newCount = Math.max(elementAmount - i, 0);
		int ret = elementAmount - newCount;

		elementAmount = newCount;
		this.forceSync();
		return ret;
	}

	public Direction getPureInfuserDirection() {
		return Stream.of(Direction.values()).filter(d -> d.getAxis().getPlane() == Direction.Plane.HORIZONTAL)
				.filter(d -> this.getWorld().getTileEntity(pos.offset(d, 3)) instanceof TilePureInfuser)
				.findAny().orElse(Direction.UP);
	}

	@Override
	public IInventory getInventory() {
		return inventory;
	}

	public ItemStack getItem() {
		return inventory.getStackInSlot(0);
	}

}
