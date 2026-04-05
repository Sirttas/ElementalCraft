package sirttas.elementalcraft.block.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.container.IContainerBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ICraftingBlockEntity extends IContainerBlockEntity {

	boolean isRecipeAvailable();

	boolean isRunning();

	void process();

	@Nullable
	default <I extends RecipeInput, T extends Recipe<@NotNull I>> T lookupRecipe(@Nonnull ServerLevel level, @Nullable RecipeType<@NotNull T> recipeType, @Nonnull I recipeInput) {
		if (recipeType == null) {
			return null;
		}

		return level.recipeAccess().getRecipeFor(recipeType, recipeInput, level)
				.map(RecipeHolder::value)
				.orElse(null);
	}
}
