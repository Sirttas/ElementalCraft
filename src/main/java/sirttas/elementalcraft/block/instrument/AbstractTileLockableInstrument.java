package sirttas.elementalcraft.block.instrument;

import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntityType;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public abstract class AbstractTileLockableInstrument<T extends IInstrument, R extends IInstrumentRecipe<T>> extends AbstractTileInstrument<T, R> {

	private boolean locked = false;

	protected AbstractTileLockableInstrument(TileEntityType<?> tileEntityTypeIn, IRecipeType<R> recipeType, int transferSpeed, int maxRunes) {
		super(tileEntityTypeIn, recipeType, transferSpeed, maxRunes);
	}

	@Override
	public void process() {
		super.process();
		updateLock();
	}

	@Override
	public void tick() {
		super.tick();
		if (locked) {
			updateLock();
		}
	}

	private void updateLock() {
		locked = !getInventory().getStackInSlot(outputSlot).isEmpty();
	}

	public boolean isLocked() {
		return locked;
	}
}
