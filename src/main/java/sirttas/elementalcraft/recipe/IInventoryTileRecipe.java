package sirttas.elementalcraft.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import sirttas.elementalcraft.inventory.IInventoryTile;
import sirttas.elementalcraft.inventory.InventoryTileWrapper;

public interface IInventoryTileRecipe<T extends IInventoryTile> extends IRecipe<InventoryTileWrapper<T>> {

	void process(T instrument);

	int getElementPerTick();

	int getDuration();

	boolean matches(T inv);

	@Override
	default boolean matches(InventoryTileWrapper<T> inv, World worldIn) {
		return matches(inv.getInstrument());
	}

	@Override
	default ItemStack getCraftingResult(InventoryTileWrapper<T> inv) {
		return getCraftingResult(inv.getInstrument());
	}

	default ItemStack getCraftingResult(T instrument) {
		return this.getRecipeOutput().copy();
	}
}
