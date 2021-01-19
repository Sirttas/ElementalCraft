package sirttas.elementalcraft.block.tile;

import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.world.World;
import sirttas.elementalcraft.inventory.IInventoryTile;
import sirttas.elementalcraft.inventory.InventoryTileWrapper;
import sirttas.elementalcraft.recipe.IInventoryTileRecipe;

public interface ICraftingTile extends IInventoryTile {

	boolean isRecipeAvailable();

	boolean isRunning();

	int getProgress();

	void process();

	default <C extends ICraftingTile, U extends IInventoryTileRecipe<C>> U lookupRecipe(World world, IRecipeType<U> recipeType) {
		return world.getRecipeManager().getRecipe(recipeType, InventoryTileWrapper.from(cast()), world).orElse(null);
	}

	@SuppressWarnings("unchecked")
	default <C extends ICraftingTile> C cast() {
		return (C) this;
	}
}
