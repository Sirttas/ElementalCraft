package sirttas.elementalcraft.recipe.instrument.io;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.instrument.ISingleElementInstrumentRecipe;

import javax.annotation.Nonnull;

public class FurnaceRecipeWrapper implements IOInstrumentRecipe<IOInstrumentRecipeInput>, ISingleElementInstrumentRecipe<IOInstrumentRecipeInput> {

	private final AbstractCookingRecipe recipe;

	public FurnaceRecipeWrapper(AbstractCookingRecipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public @NotNull ItemStack assemble(@NotNull IOInstrumentRecipeInput input, @Nonnull HolderLookup.Provider provider) {
		return recipe.assemble(new SingleRecipeInput(input.getItem(0)), provider);
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return recipe.canCraftInDimensions(width, height);
	}

	@Nonnull
    @Override
	public ItemStack getResultItem(@Nonnull HolderLookup.Provider provider) {
		return recipe.getResultItem(provider);
	}

	@Nonnull
    @Override
	public RecipeSerializer<?> getSerializer() {
		return recipe.getSerializer();
	}

	@Nonnull
    @Override
	public RecipeType<?> getType() {
		return recipe.getType();
	}
	
	public float getExperience() {
		return recipe.getExperience();
	}

	@Override
	public @NotNull ElementType getElementType() {
		return ElementType.FIRE;
	}

	public int getDuration() {
		return recipe.getCookingTime();
	}

	@Override
	public int getElementAmount() {
		return getDuration() * (recipe.getType() == RecipeType.SMELTING ? ECConfig.SERVER.fireFurnaceElementAmount.get() : ECConfig.SERVER.fireBlastFurnaceElementAmount.get());
	}

	@Override
	public boolean matches(@NotNull IOInstrumentRecipeInput input, @Nonnull Level level) {
		return input.getElementType() == ElementType.FIRE && recipe.matches(new SingleRecipeInput(input.getItem(0)), level);
	}
}
