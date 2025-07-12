package sirttas.elementalcraft.block.entity;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.container.IContainerBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ICraftingBlockEntity extends IContainerBlockEntity {

	boolean isRecipeAvailable();

	boolean isRunning();

	void process();

	@Nullable
	default <I extends RecipeInput, T extends Recipe<I>> T lookupRecipe(@Nonnull Level level, @Nullable RecipeType<T> recipeType, @Nonnull I recipeInput) {
		if (recipeType == null) {
			return null;
		}

		return level.getRecipeManager().getRecipeFor(recipeType, recipeInput, level)
				.map(RecipeHolder::value)
				.orElse(null);
	}
}
