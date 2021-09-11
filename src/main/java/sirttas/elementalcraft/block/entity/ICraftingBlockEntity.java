package sirttas.elementalcraft.block.entity;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.inventory.IInventoryBlockEntity;
import sirttas.elementalcraft.inventory.InventoryBlockEntityWrapper;
import sirttas.elementalcraft.recipe.IInventoryBlockEntityRecipe;

public interface ICraftingBlockEntity extends IInventoryBlockEntity {

	boolean isRecipeAvailable();

	boolean isRunning();

	int getProgress();

	void process();

	default <C extends ICraftingBlockEntity, U extends IInventoryBlockEntityRecipe<C>> U lookupRecipe(Level world, RecipeType<U> recipeType) {
		return world.getRecipeManager().getRecipeFor(recipeType, InventoryBlockEntityWrapper.from(cast()), world).orElse(null);
	}

	@SuppressWarnings("unchecked")
	default <C extends ICraftingBlockEntity> C cast() {
		return (C) this;
	}
}
