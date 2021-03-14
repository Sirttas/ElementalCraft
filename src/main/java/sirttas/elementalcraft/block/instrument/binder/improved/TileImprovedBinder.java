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

public class TileImprovedBinder extends TileBinder implements IInfuser {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockImprovedBinder.NAME) public static final TileEntityType<TileImprovedBinder> TYPE = null;

	public TileImprovedBinder() {
		super(TYPE, ECConfig.COMMON.improvedBinderTransferSpeed.get(), ECConfig.COMMON.improvedBinderMaxRunes.get());
	}

	@Override
	protected IInventoryTileRecipe<IBinder> lookupRecipe() {
		IInventoryTileRecipe<IBinder> bindingRecipe = super.lookupRecipe();

		return bindingRecipe != null ? bindingRecipe : new BinderInfusionRecipeWrapper(this.lookupInfusionRecipe(world));
	}
}
