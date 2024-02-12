package sirttas.elementalcraft.block.entity;

import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.container.ContainerBlockEntityWrapper;
import sirttas.elementalcraft.container.IContainerBlockEntity;
import sirttas.elementalcraft.recipe.IContainerBlockEntityRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ICraftingBlockEntity extends IContainerBlockEntity {

	boolean isRecipeAvailable();

	boolean isRunning();

	int getProgress();

	void process();

	@Nullable
	default <C extends ICraftingBlockEntity, U extends IContainerBlockEntityRecipe<C>> U lookupRecipe(@Nonnull Level level, @Nullable RecipeType<U> recipeType) { // TODO: maybe return a recipe holder for serialization, and an optional?
		if (recipeType == null) {
			return null;
		}

		return level.getRecipeManager().getRecipeFor(recipeType, getContainerWrapper(), level)
				.map(RecipeHolder::value)
				.orElse(null);
	}

	@SuppressWarnings("unchecked")
	default <C extends ICraftingBlockEntity> ContainerBlockEntityWrapper<C> getContainerWrapper() {
		return ContainerBlockEntityWrapper.from((C) this);
	}
}
