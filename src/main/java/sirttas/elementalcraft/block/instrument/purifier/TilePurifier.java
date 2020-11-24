package sirttas.elementalcraft.block.instrument.purifier;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.TileInstrument;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.PurifierRecipe;

public class TilePurifier extends TileInstrument {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPurifier.NAME) public static TileEntityType<TilePurifier> TYPE;

	private final PurifierInventory inventory;

	public TilePurifier() {
		super(TYPE);
		outputSlot = 1;
		inventory = new PurifierInventory(this::forceSync);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IInstrumentRecipe<TilePurifier> lookupRecipe() {
		ItemStack input = inventory.getStackInSlot(0);

		if (!input.isEmpty() && ElementalCraft.PURE_ORE_MANAGER.isValidOre(input)) {
			PurifierRecipe recipe = new PurifierRecipe(input);

			if (recipe.matches(this)) {
				return new PurifierRecipe(input);
			}
		}
		return null;
	}

	@Override
	public IInventory getInventory() {
		return inventory;
	}
}
