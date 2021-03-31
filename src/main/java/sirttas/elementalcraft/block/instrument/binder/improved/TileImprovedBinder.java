package sirttas.elementalcraft.block.instrument.binder.improved;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.binder.IBinder;
import sirttas.elementalcraft.block.instrument.binder.TileBinder;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.IInventoryTileRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.BinderInfusionRecipeWrapper;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;

public class TileImprovedBinder extends TileBinder implements IInfuser {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockImprovedBinder.NAME) public static final TileEntityType<TileImprovedBinder> TYPE = null;

	public TileImprovedBinder() {
		super(TYPE, ECConfig.COMMON.improvedBinderTransferSpeed.get(), ECConfig.COMMON.improvedBinderMaxRunes.get());
	}

	@Override
	protected IInventoryTileRecipe<IBinder> lookupRecipe() {
		IInventoryTileRecipe<IBinder> bindingRecipe = super.lookupRecipe();

		if (bindingRecipe == null) {
			IInfusionRecipe infusionRecipe = this.lookupInfusionRecipe(world);

			return infusionRecipe != null ? new BinderInfusionRecipeWrapper(infusionRecipe) : null;
		}
		return bindingRecipe;
	}
}
