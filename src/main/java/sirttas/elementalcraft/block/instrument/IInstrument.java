package sirttas.elementalcraft.block.instrument;

import sirttas.elementalcraft.block.tile.IForcableSync;
import sirttas.elementalcraft.inventory.IInventoryTile;

public interface IInstrument extends IForcableSync, IInventoryTile {

	boolean canProgress();

	boolean isRecipeAvailable();

	void process();

	float getProgress();

	boolean isRunning();
}