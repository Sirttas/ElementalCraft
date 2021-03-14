package sirttas.elementalcraft.block.instrument.purifier;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.AbstractTileInstrument;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.instrument.io.PurifierRecipe;

public class TilePurifier extends AbstractTileInstrument<TilePurifier, PurifierRecipe> {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPurifier.NAME) public static final TileEntityType<TilePurifier> TYPE = null;

	private final PurifierInventory inventory;

	public TilePurifier() {
		super(TYPE, null, ECConfig.COMMON.purifierTransferSpeed.get(), ECConfig.COMMON.purifierMaxRunes.get());
		outputSlot = 1;
		inventory = new PurifierInventory(this::markDirty);
	}

	@Override
	protected IItemHandler createHandler() {
		return new SidedInvWrapper(inventory, null);
	}

	@Override
	protected PurifierRecipe lookupRecipe() {
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
