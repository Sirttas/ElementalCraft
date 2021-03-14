package sirttas.elementalcraft.block.instrument.mill;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.AbstractTileInstrument;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.interaction.mekanism.MekanismInteraction;
import sirttas.elementalcraft.inventory.IOInventory;
import sirttas.elementalcraft.recipe.IInventoryTileRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.AbstractGrindingRecipe;

public class TileAirMill extends AbstractTileInstrument<TileAirMill, AbstractGrindingRecipe> {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockAirMill.NAME) public static final TileEntityType<TileAirMill> TYPE = null;

	private final IOInventory inventory;

	public TileAirMill() {
		super(TYPE, AbstractGrindingRecipe.TYPE, ECConfig.COMMON.airMillTransferSpeed.get(), ECConfig.COMMON.airMillMaxRunes.get());
		outputSlot = 1;
		inventory = new IOInventory(this::markDirty);
	}

	@Override
	protected IItemHandler createHandler() {
		return new SidedInvWrapper(inventory, null);
	}

	@Override
	public IInventory getInventory() {
		return inventory;
	}
	
	@Override
	protected IInventoryTileRecipe<TileAirMill> lookupRecipe() {
		IInventoryTileRecipe<TileAirMill> recipe = super.lookupRecipe();
		
		if (recipe == null && ECinteractions.isMekanismActive()) {
			return MekanismInteraction.lookupCrusherRecipe(world, inventory);
		}
		return recipe;
	}
}
