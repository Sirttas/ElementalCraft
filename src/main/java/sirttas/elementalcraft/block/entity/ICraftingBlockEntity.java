package sirttas.elementalcraft.block.entity;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.inventory.IInventoryTile;
import sirttas.elementalcraft.inventory.InventoryTileWrapper;
import sirttas.elementalcraft.recipe.IInventoryTileRecipe;

public interface ICraftingBlockEntity extends IInventoryTile {

	boolean isRecipeAvailable();

	boolean isRunning();

	int getProgress();

	void process();

	default <C extends ICraftingBlockEntity, U extends IInventoryTileRecipe<C>> U lookupRecipe(Level world, RecipeType<U> recipeType) {
		return world.getRecipeManager().getRecipeFor(recipeType, InventoryTileWrapper.from(cast()), world).orElse(null);
	}

	@SuppressWarnings("unchecked")
	default <C extends ICraftingBlockEntity> C cast() {
		return (C) this;
	}
}
