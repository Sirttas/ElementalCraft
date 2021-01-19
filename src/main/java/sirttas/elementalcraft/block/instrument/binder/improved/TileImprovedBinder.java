package sirttas.elementalcraft.block.instrument.binder.improved;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.binder.TileBinder;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.recipe.IInventoryTileRecipe;

public class TileImprovedBinder extends TileBinder implements IInfuser {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockImprovedBinder.NAME) public static TileEntityType<TileImprovedBinder> TYPE;

	public TileImprovedBinder() {
		super(TYPE);
	}

	@Override
	protected IInventoryTileRecipe<?> lookupRecipe() {
		IInventoryTileRecipe<?> bindingRecipe = super.lookupRecipe();

		return bindingRecipe != null ? bindingRecipe : this.lookupInfusionRecipe(world);
	}
}
