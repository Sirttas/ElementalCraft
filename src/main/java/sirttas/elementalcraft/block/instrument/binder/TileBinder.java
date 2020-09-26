package sirttas.elementalcraft.block.instrument.binder;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.TileInstrument;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.instrument.BinderRecipe;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public class TileBinder extends TileInstrument {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockBinder.NAME) public static TileEntityType<TileBinder> TYPE;

	private NonNullList<ItemStack> stacks = NonNullList.withSize(10, ItemStack.EMPTY);

	public TileBinder() {
		super(TYPE);
	}

	@Override
	public int getSizeInventory() {
		return 10;
	}

	public int getItemCount() {
		return (int) stacks.stream().filter(i -> !i.isEmpty()).count();
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
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
		} else if (stack.isEmpty()) {
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

	@Override
	public void process() {
		super.process();
		if (this.world.isRemote) {
			ParticleHelper.createCraftingParticle(getTankElementType(), world, new Vec3d(pos).add(0, 0.2, 0), world.rand);
		}
	}

}
