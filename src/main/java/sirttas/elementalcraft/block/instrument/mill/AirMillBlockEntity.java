package sirttas.elementalcraft.block.instrument.mill;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.inventory.IOInventory;
import sirttas.elementalcraft.recipe.IInventoryTileRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

public class AirMillBlockEntity extends AbstractInstrumentBlockEntity<AirMillBlockEntity, IGrindingRecipe> {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + AirMillBlock.NAME) public static final BlockEntityType<AirMillBlockEntity> TYPE = null;

	private final IOInventory inventory;

	public AirMillBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, IGrindingRecipe.TYPE, ECConfig.COMMON.airMillTransferSpeed.get(), ECConfig.COMMON.airMillMaxRunes.get());
		outputSlot = 1;
		inventory = new IOInventory(this::setChanged);
	}

	@Override
	protected IItemHandler createHandler() {
		return new SidedInvWrapper(inventory, null);
	}

	@Override
	public Container getInventory() {
		return inventory;
	}
	
	@Override
	protected IInventoryTileRecipe<AirMillBlockEntity> lookupRecipe() {
		IInventoryTileRecipe<AirMillBlockEntity> recipe = super.lookupRecipe();
		
		if (recipe == null && ECinteractions.isMekanismActive()) {
			return null; // TODO MekanismInteraction.lookupCrusherRecipe(level, inventory);
		}
		return recipe;
	}
}
