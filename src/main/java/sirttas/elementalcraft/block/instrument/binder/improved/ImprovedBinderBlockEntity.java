package sirttas.elementalcraft.block.instrument.binder.improved;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.binder.BinderBlockEntity;
import sirttas.elementalcraft.block.instrument.binder.IBinder;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.BinderInfusionRecipeWrapper;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;

public class ImprovedBinderBlockEntity extends BinderBlockEntity implements IInfuser {

	private static final Config<IBinder, AbstractBindingRecipe> CONFIG = new Config<>(
			ECBlockEntityTypes.BINDER_IMPROVED,
			ECRecipeTypes.BINDING,
			ECConfig.COMMON.improvedBinderTransferSpeed,
			ECConfig.COMMON.improvedBinderMaxRunes,
			0,
			true
	);

	public ImprovedBinderBlockEntity(BlockPos pos, BlockState state) {
		super(CONFIG, pos, state);
	}

	@Override
	protected AbstractBindingRecipe lookupRecipe() {
		if (getContainerElementType() == ElementType.NONE) {
			return null;
		}

		var bindingRecipe = super.lookupRecipe();

		if (bindingRecipe == null) {
			IInfusionRecipe infusionRecipe = this.lookupInfusionRecipe(level);

			return infusionRecipe != null ? new BinderInfusionRecipeWrapper(infusionRecipe) : null;
		}
		return bindingRecipe;
	}
}
