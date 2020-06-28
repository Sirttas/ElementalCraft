package sirttas.elementalcraft.block.pureinfuser;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.tile.TileECContainer;
import sirttas.elementalcraft.block.tile.element.IElementReceiver;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.nbt.ECNBTTags;
import sirttas.elementalcraft.nbt.NBTHelper;

public class TilePedestal extends TileECContainer implements IElementReceiver {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME) public static TileEntityType<TilePedestal> TYPE;

	private ElementType elementType = ElementType.NONE;
	private int elementAmount = 0;
	protected int elementMax = 10000; // TODO CONFIG
	private ItemStack stack;

	public TilePedestal() {
		super(TYPE);
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public int getMaxElement() {
		return elementMax;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		elementType = ElementType.byName(compound.getString(ECNBTTags.ELEMENT_TYPE));
		elementAmount = compound.getInt(ECNBTTags.ELEMENT_AMOUNT);
		this.stack = NBTHelper.readItemStack(compound, ECNBTTags.ITEM);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putString(ECNBTTags.ELEMENT_TYPE, elementType.getName());
		compound.putInt(ECNBTTags.ELEMENT_AMOUNT, elementAmount);
		NBTHelper.writeItemStack(compound, ECNBTTags.ITEM, this.stack);
		return compound;
	}

	@Override
	public int inserElement(int count, ElementType type, boolean simulate) {
		if (type != this.elementType && this.elementType != ElementType.NONE) {
			return 0;
		} else {
			int newCount = elementAmount + count;

			newCount = newCount < elementMax ? newCount : elementMax;

			int ret = count - newCount + elementAmount;

			if (!simulate) {
				elementAmount = newCount;
				if (this.elementType == ElementType.NONE) {
					this.elementType = type;
				}
			}
			return ret;
		}
	}

	public int consumeElement(int i) {
		int newCount = elementAmount - i;
		newCount = newCount > 0 ? newCount : 0;
		int ret = elementAmount - newCount;

		elementAmount = newCount;
		return ret;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return ItemEC.isEmpty(stack);
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return index == 0 ? stack : ItemStack.EMPTY;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index == 0) {
			this.stack = stack;
		}
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index == 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void clear() {
		this.stack = ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		if (index == 0) {
			ItemStack ret = stack;

			this.stack = ItemStack.EMPTY;
			return ret;
		}
		return ItemStack.EMPTY;
	}

}
