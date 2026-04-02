package sirttas.elementalcraft.recipe.instrument.io;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.recipe.ILuckRecipe;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

import javax.annotation.Nonnull;

public interface IOInstrumentRecipe<I extends IOInstrumentRecipeInput> extends IInstrumentRecipe<I>, ILuckRecipe<I> {

	default int getInputSize() {
		return 1;
	}

	default boolean matches(@NotNull ItemStack input, @Nonnull Level level) {
		return input.getCount() >= getInputSize();
	}
	
	@Override
	default boolean matches(@Nonnull I input, @Nonnull Level level) {
		var craftingResult = assemble(input, level.registryAccess());
		var output = input.getItem(1);

		return this.getValidElementTypes().contains(input.getElementType()) && matches(input.getItem(0), level)
				&& (output.isEmpty() || (ItemStack.isSameItemSameComponents(output, craftingResult) && output.getCount() + craftingResult.getCount() <= input.getItemLimit(1)));
	}
}
