package sirttas.elementalcraft.block.pureinfuser;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.tile.TileECContainer;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.nbt.ECNBTTags;
import sirttas.elementalcraft.nbt.NBTHelper;
import sirttas.elementalcraft.recipe.PureInfusionRecipe;

public class TilePureInfuser extends TileECContainer {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPureInfuser.NAME) public static TileEntityType<TilePureInfuser> TYPE;

	private ItemStack stack;
	private float progress = 0;
	private PureInfusionRecipe recipe;

	public TilePureInfuser() {
		super(TYPE);
	}


	public boolean isRunning() {
		return progress > 0;
	}

	public boolean isReciptAvalable() {
		if (recipe != null && recipe.matches(this)) {
			return true;
		}
		recipe = this.getWorld().getRecipeManager().getRecipe(PureInfusionRecipe.TYPE, this, this.getWorld()).orElse(null);
		return recipe != null;
	}

	public void process() {
		recipe.process(this);
		recipe = null;
		this.forceSync();
	}

	@Override
	public void tick() {
		super.tick();
		makeProgress();
	}

	protected void makeProgress() {
		if (recipe != null && progress >= recipe.getDuration()) {
			process();
			progress = 0;
		} else if (this.isReciptAvalable() && consume(Direction.NORTH) && consume(Direction.SOUTH) && consume(Direction.WEST) && consume(Direction.EAST)) {
			progress++;
		} else {
			progress = 0;
		}
	}

	public float getProgress() {
		return progress;
	}

	public ItemStack getStackInPedestal(ElementType type) {
		TilePedestal pedestal = getPedestal(type);

		return pedestal != null ? pedestal.getStackInSlot(0) : ItemStack.EMPTY;
	}

	public TilePedestal getPedestal(ElementType type) {
		TilePedestal pedestal = getPedestal(Direction.NORTH);

		if (pedestal == null || pedestal.getElementType() != type) {
			pedestal = getPedestal(Direction.SOUTH);
		}
		if (pedestal == null || pedestal.getElementType() != type) {
			pedestal = getPedestal(Direction.WEST);
		}
		if (pedestal == null || pedestal.getElementType() != type) {
			pedestal = getPedestal(Direction.EAST);
		}
		return pedestal != null && pedestal.getElementType() == type ? pedestal : null;
	}

	public void emptyPedestals() {
		setPedestalInventory(Direction.NORTH, ItemStack.EMPTY);
		setPedestalInventory(Direction.SOUTH, ItemStack.EMPTY);
		setPedestalInventory(Direction.WEST, ItemStack.EMPTY);
		setPedestalInventory(Direction.EAST, ItemStack.EMPTY);
	}

	private void setPedestalInventory(Direction direction, ItemStack stack) {
		TilePedestal pedestal = getPedestal(direction);

		if (pedestal != null) {
			pedestal.setInventorySlotContents(0, stack);
		}
	}

	private TilePedestal getPedestal(Direction direction) {
		TileEntity te = this.hasWorld() ? this.getWorld().getTileEntity(pos.offset(direction, 3)) : null;
		return te instanceof TilePedestal ? (TilePedestal) te : null;
	}

	private boolean consume(Direction direction) {
		TilePedestal pedestal = getPedestal(direction);
		int elementPerTick = recipe.getElementPerTick();

		return pedestal != null && pedestal.consumeElement(elementPerTick) == elementPerTick;
	}

	@Override
	public void func_230337_a_/* read */(BlockState state, CompoundNBT compound) {
		super.func_230337_a_/* read */(state, compound);
		this.stack = NBTHelper.readItemStack(compound, ECNBTTags.ITEM);
		progress = compound.getFloat(ECNBTTags.PROGRESS);

	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		NBTHelper.writeItemStack(compound, ECNBTTags.ITEM, this.stack);
		compound.putFloat(ECNBTTags.PROGRESS, progress);
		return compound;
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
		recipe = null;
		progress = 0;
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
