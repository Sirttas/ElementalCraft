package sirttas.elementalcraft.block.instrument.purifier;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.TileInstrument;
import sirttas.elementalcraft.item.pureore.PureOreHelper;
import sirttas.elementalcraft.nbt.ECNames;
import sirttas.elementalcraft.nbt.NBTHelper;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.PurifierRecipe;

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
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.input = NBTHelper.readItemStack(compound, ECNames.INPUT);
		this.output = NBTHelper.readItemStack(compound, ECNames.OUTPUT);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		NBTHelper.writeItemStack(compound, ECNames.INPUT, this.input);
		NBTHelper.writeItemStack(compound, ECNames.OUTPUT, this.output);
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
		if (!input.isEmpty() && PureOreHelper.isValidOre(input)) {
			return new PurifierRecipe(input);
		}
		return null;
	}
}
