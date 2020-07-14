package sirttas.elementalcraft.block.instrument.firefurnace;

import java.util.Optional;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.TileInstrument;
import sirttas.elementalcraft.nbt.ECNBTTags;
import sirttas.elementalcraft.nbt.NBTHelper;
import sirttas.elementalcraft.recipe.instrument.FurnaceRecipeWraper;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public class TileFireFurnace extends TileInstrument {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFireFurnace.NAME) public static TileEntityType<TileFireFurnace> TYPE;

	private ItemStack input;
	private ItemStack output;
	private float exp;

	public TileFireFurnace() {
		super(TYPE);
		input = ItemStack.EMPTY;
		output = ItemStack.EMPTY;
		exp = 0;
		this.setPasive(true);
	}

	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public void func_230337_a_/* read */(BlockState state, CompoundNBT compound) {
		super.func_230337_a_/* read */(state, compound);
		this.input = NBTHelper.readItemStack(compound, ECNBTTags.INPUT);
		this.output = NBTHelper.readItemStack(compound, ECNBTTags.OUTPUT);
		this.exp = compound.getFloat(ECNBTTags.XP);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		NBTHelper.writeItemStack(compound, ECNBTTags.INPUT, this.input);
		NBTHelper.writeItemStack(compound, ECNBTTags.OUTPUT, this.output);
		compound.putFloat(ECNBTTags.XP, this.exp);
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
	protected IInstrumentRecipe<TileFireFurnace> lookupRecipe() {
		Optional<FurnaceRecipe> opt = this.getWorld().getRecipeManager().getRecipe(IRecipeType.SMELTING, this, this.getWorld());

		if (opt.isPresent()) {
			return new FurnaceRecipeWraper(opt.get());
		}
		return null;
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
