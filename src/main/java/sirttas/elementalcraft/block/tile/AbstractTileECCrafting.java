package sirttas.elementalcraft.block.tile;

import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntityType;
import sirttas.elementalcraft.block.retriever.BlockRetriever;
import sirttas.elementalcraft.recipe.IInventoryTileRecipe;

public abstract class AbstractTileECCrafting<T extends ICraftingTile, R extends IInventoryTileRecipe<T>> extends AbstractTileECContainer implements ICraftingTile {

	protected final IRecipeType<R> recipeType;
	protected final int transferSpeed;

	protected IInventoryTileRecipe<T> recipe;
	protected int outputSlot = 0;
	
	protected AbstractTileECCrafting(TileEntityType<?> tileEntityTypeIn, IRecipeType<R> recipeType, int transferSpeed) {
		super(tileEntityTypeIn);
		this.recipeType = recipeType;
		this.transferSpeed = transferSpeed;
	}

	@Override
	public boolean isRecipeAvailable() {
		if (recipe != null && recipe.matches(cast())) {
			return true;
		}
		recipe = this.lookupRecipe();
		if (recipe != null) {
			this.markDirty();
			return true;
		}
		return false;
	}

	@Override
	public void process() {
		if (!world.isRemote) {
			recipe.process(cast());
			BlockRetriever.sendOutputToRetriever(world, pos, getInventory(), outputSlot);
		}
		recipe = null;
		this.markDirty();
	}

	protected IInventoryTileRecipe<T> lookupRecipe() {
		return lookupRecipe(this.getWorld(), recipeType);
	}

	@Override
	public void clear() {
		super.clear();
		recipe = null;
	}
}
