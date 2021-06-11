package sirttas.elementalcraft.block.instrument.mill;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.interaction.mekanism.MekanismInteraction;
import sirttas.elementalcraft.inventory.IOInventory;
import sirttas.elementalcraft.recipe.IInventoryTileRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

public class AirMillBlockEntity extends AbstractInstrumentBlockEntity<AirMillBlockEntity, IGrindingRecipe> {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + AirMillBlock.NAME) public static final TileEntityType<AirMillBlockEntity> TYPE = null;

	private final IOInventory inventory;

	public AirMillBlockEntity() {
		super(TYPE, IGrindingRecipe.TYPE, ECConfig.COMMON.airMillTransferSpeed.get(), ECConfig.COMMON.airMillMaxRunes.get());
		outputSlot = 1;
		inventory = new IOInventory(this::setChanged);
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
	protected IInventoryTileRecipe<AirMillBlockEntity> lookupRecipe() {
		IInventoryTileRecipe<AirMillBlockEntity> recipe = super.lookupRecipe();
		
		if (recipe == null && ECinteractions.isMekanismActive()) {
			return MekanismInteraction.lookupCrusherRecipe(level, inventory);
		}
		return recipe;
	}
}
