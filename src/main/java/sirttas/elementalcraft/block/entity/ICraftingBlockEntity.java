package sirttas.elementalcraft.block.entity;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.container.IContainerBlockEntity;
import sirttas.elementalcraft.container.ContainerBlockEntityWrapper;
import sirttas.elementalcraft.recipe.IContainerBlockEntityRecipe;

public interface ICraftingBlockEntity extends IContainerBlockEntity {

	boolean isRecipeAvailable();

	boolean isRunning();

	int getProgress();

	void process();

	default <C extends ICraftingBlockEntity, U extends IContainerBlockEntityRecipe<C>> U lookupRecipe(Level world, RecipeType<U> recipeType) {
		return world.getRecipeManager().getRecipeFor(recipeType, ContainerBlockEntityWrapper.from(cast()), world).orElse(null);
	}

	@SuppressWarnings("unchecked")
	default <C extends ICraftingBlockEntity> C cast() {
		return (C) this;
	}
}
