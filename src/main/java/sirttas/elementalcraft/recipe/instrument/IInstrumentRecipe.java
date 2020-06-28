package sirttas.elementalcraft.recipe.instrument;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.instrument.IInstrument;

public interface IInstrumentRecipe<T extends IInstrument & IInventory> extends IRecipe<T> {

	void process(T instrument);

	ElementType getElementType();

	int getElementPerTick();

	int getDuration();

	boolean matches(T inv);

	@Override
	default boolean matches(T inv, World worldIn) {
		return matches(inv);
	}
}
