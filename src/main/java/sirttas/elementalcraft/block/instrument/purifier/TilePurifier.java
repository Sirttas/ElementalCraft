package sirttas.elementalcraft.block.instrument.purifier;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.TileInstrument;
import sirttas.elementalcraft.nbt.ECNBTTags;
import sirttas.elementalcraft.nbt.NBTHelper;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public class TilePurifier extends TileInstrument {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPurifier.NAME) public static TileEntityType<TilePurifier> TYPE;

	private ItemStack input;
	private ItemStack output;

	public TilePurifier() {
		super(TYPE);
		input = ItemStack.EMPTY;
		output = ItemStack.EMPTY;
		this.setPasive(true);
	}

	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		this.input = NBTHelper.readItemStack(compound, ECNBTTags.INPUT);
		this.output = NBTHelper.readItemStack(compound, ECNBTTags.OUTPUT);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		NBTHelper.writeItemStack(compound, ECNBTTags.INPUT, this.input);
		NBTHelper.writeItemStack(compound, ECNBTTags.OUTPUT, this.output);
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
	protected IInstrumentRecipe<TilePurifier> lookupRecipe() {
		return null;
	}
}
