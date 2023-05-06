package sirttas.elementalcraft.block.instrument.infuser;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.SingleItemContainer;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;

import javax.annotation.Nonnull;

public class InfuserBlockEntity extends AbstractInstrumentBlockEntity<IInfuser, IInfusionRecipe> implements IInfuser {

	private final SingleItemContainer inventory;

	public InfuserBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.INFUSER, pos, state, ECRecipeTypes.INFUSION.get(), ECConfig.COMMON.infuserTransferSpeed.get(), ECConfig.COMMON.infuserMaxRunes.get());
		inventory = new SingleItemContainer(this::setChanged);
		lockable = true;
	}

	@Override
	protected IInfusionRecipe lookupRecipe() {
		return this.lookupInfusionRecipe(level);
	}

	@Override
	protected boolean shouldRetrieverExtractOutput() {
		return this.recipe == null;
	}

	@Nonnull
	@Override
	public Container getInventory() {
		return inventory;
	}
}
