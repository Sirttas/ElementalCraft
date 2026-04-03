package sirttas.elementalcraft.recipe.instrument.io;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.RuntimeRecipe;
import sirttas.elementalcraft.recipe.instrument.SingleElementInstrumentRecipe;

import javax.annotation.Nonnull;

public class FurnaceRecipeWrapper implements IOInstrumentRecipe<IOInstrumentRecipeInput>, SingleElementInstrumentRecipe<IOInstrumentRecipeInput>, RuntimeRecipe<IOInstrumentRecipeInput> {

	private final AbstractCookingRecipe recipe;

	public FurnaceRecipeWrapper(AbstractCookingRecipe recipe) {
		this.recipe = recipe;
	}

    @Override
    public boolean matches(@NotNull IOInstrumentRecipeInput input, @Nonnull Level level) {
        return input.getElementType() == ElementType.FIRE && recipe.matches(new SingleRecipeInput(input.getItem(0)), level);
    }

	@Override
	public @NotNull ItemStack assemble(@NotNull IOInstrumentRecipeInput input) {
		return recipe.assemble(new SingleRecipeInput(input.getItem(0)));
	}

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return recipe.placementInfo();
    }

    public float experience() {
		return recipe.experience();
	}

	@Override
	public @NotNull ElementType getElementType() {
		return ElementType.FIRE;
	}

	public int duration() {
		return recipe.cookingTime();
	}

	@Override
	public int getElementAmount() {
		return duration() * (recipe.getType() == RecipeType.SMELTING ? ECConfig.SERVER.fireFurnaceElementAmount.get() : ECConfig.SERVER.fireBlastFurnaceElementAmount.get());
	}
}
