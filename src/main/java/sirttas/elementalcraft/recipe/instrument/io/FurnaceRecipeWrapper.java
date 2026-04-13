package sirttas.elementalcraft.recipe.instrument.io;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.RuntimeRecipe;
import sirttas.elementalcraft.recipe.instrument.SingleElementInstrumentRecipe;

import javax.annotation.Nonnull;
import java.util.List;

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

    @Override
    public @NotNull List<RecipeDisplay> display() {
        return List.of(new IOInstrumentRecipeDisplay(
                getElementType(),
                getElementAmount(),
                recipe.input().display(),
                new SlotDisplay.ItemStackSlotDisplay(recipe.result()),
                new SlotDisplay.ItemSlotDisplay(recipe.getType() == RecipeType.SMELTING  ? ECBlocks.FIRE_FURNACE.get().asItem() : ECBlocks.FIRE_BLAST_FURNACE.get().asItem())));
    }
}
