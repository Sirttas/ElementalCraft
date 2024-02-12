package sirttas.elementalcraft.recipe.instrument.io;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.ILuckRecipe;

import javax.annotation.Nonnull;

public interface IIOInstrumentRecipe<T extends IInstrument> extends IInstrumentRecipe<T>, ILuckRecipe<T> {


	default int getInputSize() {
		return 1;
	}

	default boolean matches(ItemStack input, @Nonnull Level level) {
		return input.getCount() >= getInputSize();
	}
	
	@Override
	default boolean matches(@Nonnull T instrument, @Nonnull Level level) {
		var inv = instrument.getItemHandler(null);
		var craftingResult = assemble(instrument, level.registryAccess());
		var output = inv.getStackInSlot(1);

		return this.getValidElementTypes().contains(instrument.getContainerElementType()) && matches(inv.getStackInSlot(0), level)
				&& (output.isEmpty() || (ItemHandlerHelper.canItemStacksStack(output, craftingResult) && output.getCount() + craftingResult.getCount() <= inv.getSlotLimit(1)));
	}
}
