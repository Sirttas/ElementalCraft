package sirttas.elementalcraft.block.instrument;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import sirttas.elementalcraft.block.retriever.BlockRetriever;
import sirttas.elementalcraft.block.tank.TileTank;
import sirttas.elementalcraft.block.tile.TileECContainer;
import sirttas.elementalcraft.nbt.ECNames;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public abstract class TileInstrument extends TileECContainer implements IInstrument {

	protected float progress = 0;
	private IInstrumentRecipe<IInstrument> recipe;
	protected int outputSlot = 0;

	public TileInstrument(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	@Override
	public boolean isRecipeAvailable() {
		if (recipe != null && recipe.matches(this)) {
			return true;
		}
		if (recipe != null) {
			this.forceSync();
		}
		recipe = this.lookupRecipe();
		return recipe != null;
	}

	protected abstract <T extends IInstrument> IInstrumentRecipe<T> lookupRecipe();

	private void sendOutputToRetriever() {
		IInventory inventory = this.getInventory();

		for (Direction direction : Direction.values()) {
			BlockState blockState = world.getBlockState(pos.offset(direction));

			if (blockState.getBlock() instanceof BlockRetriever && blockState.get(BlockRetriever.SOURCE) == direction.getOpposite()) {
				ItemStack output = BlockRetriever.retrive(blockState, world, pos.offset(direction), inventory.getStackInSlot(outputSlot));
				
				inventory.setInventorySlotContents(outputSlot, output);
				if (output.isEmpty()) {
					return;
				}
			}
		}
	}

	@Override
	public void process() {
		recipe.process(this);
		recipe = null;
		sendOutputToRetriever();
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
		} else if (this.isRecipeAvailable() && tank != null && canProgress() && tank.extractElement(recipe.getElementPerTick(), recipe.getElementType(), true) == recipe.getElementPerTick()) {
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
		cmp.putFloat(ECNames.PROGRESS, progress);
		return cmp;
	}

	@Override
	public void read(BlockState state, CompoundNBT cmp) {
		super.read(state, cmp);
		progress = cmp.getFloat(ECNames.PROGRESS);
	}

	@Override
	public float getProgress() {
		return progress;
	}

	@Override
	public void clear() {
		super.clear();
		recipe = null;
		progress = 0;
	}
}
