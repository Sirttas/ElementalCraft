package sirttas.elementalcraft.block.instrument.binder.improved;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.binder.BinderBlockEntity;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.BinderInfusionRecipeWrapper;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;

public class ImprovedBinderBlockEntity extends BinderBlockEntity implements IInfuser {
	public ImprovedBinderBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.BINDER_IMPROVED, pos, state, ECConfig.COMMON.improvedBinderTransferSpeed.get(), ECConfig.COMMON.improvedBinderMaxRunes.get());
	}

	@Override
	protected AbstractBindingRecipe lookupRecipe() {
		var bindingRecipe = super.lookupRecipe();

		if (bindingRecipe == null) {
			IInfusionRecipe infusionRecipe = this.lookupInfusionRecipe(level);

			return infusionRecipe != null ? new BinderInfusionRecipeWrapper(infusionRecipe) : null;
		}
		return bindingRecipe;
	}
}
