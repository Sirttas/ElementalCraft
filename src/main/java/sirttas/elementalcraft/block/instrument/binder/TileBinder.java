package sirttas.elementalcraft.block.instrument.binder;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.TileInstrument;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.recipe.instrument.BinderRecipe;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public class TileBinder extends TileInstrument {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockBinder.NAME) public static TileEntityType<TileBinder> TYPE;

	private NonNullList<ItemStack> stacks = NonNullList.withSize(10, ItemStack.EMPTY);

	public TileBinder() {
		super(TYPE);
		this.setPasive(true);
	}

	@Override
	public int getSizeInventory() {
		return 10;
	}

	public int getItemCount() {
		return (int) stacks.stream().filter(i -> !ItemEC.isEmpty(i)).count();
	}

	@Override
	public void func_230337_a_/* read */(BlockState state, CompoundNBT compound) {
		super.func_230337_a_/* read */(state, compound);
		this.stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.stacks);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		ItemStackHelper.saveAllItems(compound, this.stacks);
		return compound;
	}

	@Override
	public boolean isEmpty() {
		return stacks.isEmpty() || super.isEmpty();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return stacks.size() > index ? stacks.get(index) : ItemStack.EMPTY;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index < stacks.size()) {
			stacks.set(index, stack);
		} else if (ItemEC.isEmpty(stack)) {
			stacks.add(stack);
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IInstrumentRecipe<TileBinder> lookupRecipe() {
		return this.getWorld().getRecipeManager().getRecipe(BinderRecipe.TYPE, this, this.getWorld()).orElse(null);
	}

}
