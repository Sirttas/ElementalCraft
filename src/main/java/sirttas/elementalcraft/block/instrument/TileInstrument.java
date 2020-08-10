package sirttas.elementalcraft.block.instrument;

import java.util.stream.IntStream;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import sirttas.elementalcraft.block.tank.TileTank;
import sirttas.elementalcraft.block.tile.TileECContainer;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.nbt.ECNBTTags;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public abstract class TileInstrument extends TileECContainer implements IInstrument {

	protected float progress = 0;
	private IInstrumentRecipe<TileInstrument> recipe;

	public TileInstrument(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	@Override
	public boolean isReciptAvalable() {
		if (recipe != null && recipe.matches(this)) {
			return true;
		}
		recipe = this.lookupRecipe();
		return recipe != null;
	}

	protected abstract <T extends TileInstrument> IInstrumentRecipe<T> lookupRecipe();

	@Override
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
		TileTank tank = getTank();

		if (recipe != null && progress >= recipe.getDuration()) {
			process();
			progress = 0;
		} else if (this.isReciptAvalable() && canProgress() && tank.extractElement(recipe.getElementPerTick(), recipe.getElementType(), true) == recipe.getElementPerTick()) {
			tank.extractElement(recipe.getElementPerTick(), recipe.getElementType(), false);
			progress++;
		} else {
			progress = 0;
		}
	}

	@Override
	public boolean isRunning() {
		return progress > 0;
	}

	@Override
	public boolean canProgress() {
		return true;
	}

	@Override
	public CompoundNBT write(CompoundNBT cmp) {
		super.write(cmp);
		cmp.putFloat(ECNBTTags.PROGRESS, progress);
		return cmp;
	}

	@Override
	public void func_230337_a_/* read */(BlockState state, CompoundNBT cmp) {
		super.func_230337_a_/* read */(state, cmp);
		progress = cmp.getFloat(ECNBTTags.PROGRESS);
	}

	@Override
	public float getProgress() {
		return progress;
	}

	@Override
	public void clear() {
		recipe = null;
		progress = 0;
		IntStream.range(0, getSizeInventory()).forEach(i -> this.setInventorySlotContents(i, ItemStack.EMPTY));
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i <= getSizeInventory(); i++) {
			if (!ItemEC.isEmpty(this.getStackInSlot(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack ret = getStackInSlot(index);

		setInventorySlotContents(index, ItemStack.EMPTY);
		return ret;
	}
}
