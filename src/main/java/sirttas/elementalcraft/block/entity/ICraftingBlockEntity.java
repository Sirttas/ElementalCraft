package sirttas.elementalcraft.block.entity;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.container.ContainerBlockEntityWrapper;
import sirttas.elementalcraft.container.IContainerBlockEntity;
import sirttas.elementalcraft.recipe.IContainerBlockEntityRecipe;

public interface ICraftingBlockEntity extends IContainerBlockEntity {

	boolean isRecipeAvailable();

	boolean isRunning();

	int getProgress();

	void process();

	default <C extends ICraftingBlockEntity, U extends IContainerBlockEntityRecipe<C>> U lookupRecipe(Level level, RecipeType<U> recipeType) {
		if (recipeType == null) {
			return null;
		}

		return level.getRecipeManager().getRecipeFor(recipeType, ContainerBlockEntityWrapper.from(cast()), level).orElse(null);
	}

	@SuppressWarnings("unchecked")
	default <C extends ICraftingBlockEntity> C cast() {
		return (C) this;
	}
}
