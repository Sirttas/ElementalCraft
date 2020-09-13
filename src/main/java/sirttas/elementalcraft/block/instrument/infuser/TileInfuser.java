package sirttas.elementalcraft.block.instrument.infuser;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.TileInstrument;
import sirttas.elementalcraft.nbt.ECNames;
import sirttas.elementalcraft.nbt.NBTHelper;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.AbstractInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;

public class TileInfuser extends TileInstrument {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockInfuser.NAME) public static TileEntityType<TileInfuser> TYPE;

	private ItemStack stack;
	private ToolInfusionRecipe toolInfusionRecipe = new ToolInfusionRecipe();

	public TileInfuser() {
		super(TYPE);
		stack = ItemStack.EMPTY;
		this.setPasive(true);
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		this.stack = NBTHelper.readItemStack(compound, ECNames.ITEM);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		NBTHelper.writeItemStack(compound, ECNames.ITEM, this.stack);
		return compound;
	}

	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
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

	@SuppressWarnings("unchecked")
	@Override
	protected IInstrumentRecipe<TileInfuser> lookupRecipe() {
		return toolInfusionRecipe.matches(this) ? toolInfusionRecipe.with(this.getTankElementType())
				: this.getWorld().getRecipeManager().getRecipe(AbstractInfusionRecipe.TYPE, this, this.getWorld()).orElse(null);
	}

	@Override
	public void process() {
		super.process();
		if (this.world.isRemote) {
			ParticleHelper.createCraftingParticle(getTankElementType(), world, Vector3d.copy(pos), world.rand);
		}
	}

}
