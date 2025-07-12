package sirttas.elementalcraft.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;

import javax.annotation.Nonnull;

public interface IECRecipe<I extends RecipeInput> extends Recipe<I> {

	@Override
	default boolean canCraftInDimensions(int width, int height) {
		return true;
	}
	
	@Nonnull
    @Override
	default ItemStack assemble(@Nonnull I input, @Nonnull HolderLookup.Provider provider) {
		return this.getResultItem(provider).copy();
	}
	
}
