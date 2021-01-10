package sirttas.elementalcraft.block.tile;

import sirttas.elementalcraft.inventory.IInventoryTile;

public interface ICraftingTile extends IForcableSync, IInventoryTile {

	boolean isRecipeAvailable();

	boolean isRunning();

	int getProgress();

	void process();
}
