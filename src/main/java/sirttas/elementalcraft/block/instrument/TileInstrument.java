package sirttas.elementalcraft.block.instrument;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import sirttas.elementalcraft.block.retriever.BlockRetriever;
import sirttas.elementalcraft.block.tank.TileTank;
import sirttas.elementalcraft.block.tile.TileECContainer;
import sirttas.elementalcraft.nbt.ECNames;
import sirttas.elementalcraft.particle.ParticleHelper;
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

	@Override
	public void process() {
		recipe.process(this);
		recipe = null;
		BlockRetriever.sendOutputToRetriever(world, pos, getInventory(), outputSlot);
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
			onProgress();
			progress++;
		} else if (recipe == null) {
			progress = 0;
		}
	}

	protected void onProgress() {
		ParticleHelper.createElementFlowParticle(getTankElementType(), world, Vector3d.copyCentered(pos), Direction.UP, 1, world.rand);
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
