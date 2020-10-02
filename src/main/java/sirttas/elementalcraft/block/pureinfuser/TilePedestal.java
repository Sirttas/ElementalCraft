package sirttas.elementalcraft.block.pureinfuser;

import java.util.Optional;
import java.util.stream.Stream;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.tile.TileECContainer;
import sirttas.elementalcraft.block.tile.element.IElementReceiver;
import sirttas.elementalcraft.inventory.SingleItemInventory;
import sirttas.elementalcraft.nbt.ECNames;

public class TilePedestal extends TileECContainer implements IElementReceiver {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME) public static TileEntityType<TilePedestal> TYPE;

	private int elementAmount = 0;
	protected int elementMax = 10000; // TODO CONFIG
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
	public int getMaxElement() {
		return elementMax;
	}

	@Override
	public ElementType getElementType() {
		return ((BlockPedestal) this.getBlockState().getBlock()).getElementType();
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
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
			int newCount = Math.min(elementAmount + count, elementMax);
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

	private Optional<TilePureInfuser> getPureInfuser() {
		return Stream.of(Direction.values()).filter(d -> d.getAxis().getPlane() == Direction.Plane.HORIZONTAL)
				.map(d -> this.getWorld().getTileEntity(pos.offset(d, 3))).filter(TilePureInfuser.class::isInstance).map(TilePureInfuser.class::cast).findAny();
	}

	public boolean isPureInfuserRunning() {
		Optional<TilePureInfuser> opt = getPureInfuser();

		return opt.isPresent() && opt.get().isRunning();
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
