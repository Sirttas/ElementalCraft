package sirttas.elementalcraft.block.instrument.binder.improved;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.instrument.binder.IBinder;
import sirttas.elementalcraft.block.instrument.binder.BinderBlockEntity;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.IInventoryTileRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.BinderInfusionRecipeWrapper;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;

public class ImprovedBinderBlockEntity extends BinderBlockEntity implements IInfuser {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + ImprovedBinderBlock.NAME) public static final TileEntityType<ImprovedBinderBlockEntity> TYPE = null;

	public ImprovedBinderBlockEntity() {
		super(TYPE, ECConfig.COMMON.improvedBinderTransferSpeed.get(), ECConfig.COMMON.improvedBinderMaxRunes.get());
	}

	@Override
	protected IInventoryTileRecipe<IBinder> lookupRecipe() {
		IInventoryTileRecipe<IBinder> bindingRecipe = super.lookupRecipe();

		if (bindingRecipe == null) {
			IInfusionRecipe infusionRecipe = this.lookupInfusionRecipe(level);

			return infusionRecipe != null ? new BinderInfusionRecipeWrapper(infusionRecipe) : null;
		}
		return bindingRecipe;
	}
}
