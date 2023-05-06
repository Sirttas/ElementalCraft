package sirttas.elementalcraft.block.instrument.io.mill.saw;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.io.AbstractIOInstrumentBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.IOContainer;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;

import javax.annotation.Nonnull;

public class WaterMillWoodSawBlockEntity extends AbstractIOInstrumentBlockEntity<WaterMillWoodSawBlockEntity, SawingRecipe> {

	private final IOContainer inventory;

	public WaterMillWoodSawBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.WATER_MILL_WOOD_SAW, pos, state, ECRecipeTypes.SAWING.get(), ECConfig.COMMON.waterMillWoodSawTransferSpeed.get(), ECConfig.COMMON.waterMillWoodSawMaxRunes.get());
		inventory = new IOContainer(this::setChanged);
	}

	@Nonnull
    @Override
	protected IItemHandler createHandler() {
		return new SidedInvWrapper(inventory, null);
	}

	@Nonnull
    @Override
	public Container getInventory() {
		return inventory;
	}
}
