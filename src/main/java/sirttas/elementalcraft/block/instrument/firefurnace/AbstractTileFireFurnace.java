package sirttas.elementalcraft.block.instrument.firefurnace;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3d;
import sirttas.elementalcraft.block.instrument.TileInstrument;
import sirttas.elementalcraft.nbt.ECNames;
import sirttas.elementalcraft.nbt.NBTHelper;
import sirttas.elementalcraft.recipe.instrument.FurnaceRecipeWrapper;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public abstract class AbstractTileFireFurnace<T extends AbstractCookingRecipe> extends TileInstrument {

	private ItemStack input;
	private ItemStack output;
	private float exp;
	private IRecipeType<T> recipeType;

	protected AbstractTileFireFurnace(TileEntityType<? extends AbstractTileFireFurnace<T>> tileEntityTypeIn, IRecipeType<T> recipeType) {
		super(tileEntityTypeIn);
		input = ItemStack.EMPTY;
		output = ItemStack.EMPTY;
		exp = 0;
		this.recipeType = recipeType;
		this.setPasive(true);
	}

	public AbstractTileFireFurnace(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		this.input = NBTHelper.readItemStack(compound, ECNames.INPUT);
		this.output = NBTHelper.readItemStack(compound, ECNames.OUTPUT);
		this.exp = compound.getFloat(ECNames.XP);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		NBTHelper.writeItemStack(compound, ECNames.INPUT, this.input);
		NBTHelper.writeItemStack(compound, ECNames.OUTPUT, this.output);
		compound.putFloat(ECNames.XP, this.exp);
		return compound;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if (index == 0) {
			return input;
		} else if (index == 1) {
			return output;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index == 0) {
			input = stack;
		} else if (index == 1) {
			output = stack;
		}
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index == 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IInstrumentRecipe<AbstractTileFireFurnace<T>> lookupRecipe() {
		return this.getWorld().getRecipeManager().getRecipe(recipeType, this, this.getWorld()).map(FurnaceRecipeWrapper::new).orElse(null);
	}

	public void dropExperience(PlayerEntity player) {
		dropExperience(player.getPositionVec());
	}

	public void dropExperience(Vector3d pos) {
		while (exp > 0) {
			int j = ExperienceOrbEntity.getXPSplit((int) exp);
			exp -= j;
			world.addEntity(new ExperienceOrbEntity(world, pos.getX(), pos.getY() + 0.5D, pos.getZ() + 0.5D, j));
		}
		exp = 0;
	}

	public void addExperience(float exp) {
		this.exp += exp;
	}

}