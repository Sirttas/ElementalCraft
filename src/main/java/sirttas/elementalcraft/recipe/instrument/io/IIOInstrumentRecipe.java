package sirttas.elementalcraft.recipe.instrument.io;

import java.util.Random;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public interface IIOInstrumentRecipe<T extends IInstrument> extends IInstrumentRecipe<T> {

	default Random getRand(T instrument) {
		return new Random();
	}
	
	default int getLuck(T instrument) {
		return 0;
	}
	
	default boolean matches(ItemStack input) {
		return true;
	}
	
	@Override
	default boolean matches(T instrument) {
		ItemStack craftingResult = getCraftingResult(instrument);

		return instrument.getItemHandler().map(inv -> {
			ItemStack output = inv.getStackInSlot(1);

			return instrument.getTankElementType() == this.getElementType() && matches(inv.getStackInSlot(0))
					&& (output.isEmpty() || (ItemHandlerHelper.canItemStacksStack(output, craftingResult) && output.getCount() + craftingResult.getCount() <= inv.getSlotLimit(1)));
		}).orElse(false);
	}
	
	@Override
	default void process(T instrument) {
		IInventory inv = instrument.getInventory();
		ItemStack in = inv.getStackInSlot(0);
		ItemStack result = inv.getStackInSlot(1);
		ItemStack craftingResult = getCraftingResult(instrument);
		int luck = getLuck(instrument);

		if (craftingResult.isItemEqual(result) && result.getCount() + craftingResult.getCount() <= result.getMaxStackSize()) {
			in.shrink(1);
			result.grow(craftingResult.getCount());
		} else if (result.isEmpty()) {
			in.shrink(1);
			inv.setInventorySlotContents(1, craftingResult.copy());
		}
		if (luck > 0 && getRand(instrument).nextInt(100) < luck) {
			result.grow(1);
		}
		if (in.isEmpty()) {
			inv.removeStackFromSlot(0);
		}
	}
	
}
