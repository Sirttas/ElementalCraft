package sirttas.elementalcraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.block.retriever.RetrieverBlock;
import sirttas.elementalcraft.recipe.IInventoryTileRecipe;

public abstract class AbstractECCraftingBlockEntity<T extends ICraftingBlockEntity, R extends IInventoryTileRecipe<T>> extends AbstractECContainerBlockEntity implements ICraftingBlockEntity {

	protected final RecipeType<R> recipeType;
	protected final int transferSpeed;

	protected IInventoryTileRecipe<T> recipe;
	protected int outputSlot = 0;
	
	protected AbstractECCraftingBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, RecipeType<R> recipeType, int transferSpeed) {
		super(blockEntityType, pos, state);
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
			this.setChanged();
			return true;
		}
		return false;
	}

	@Override
	public void process() {
		if (!level.isClientSide) {
			recipe.process(cast());
			RetrieverBlock.sendOutputToRetriever(level, worldPosition, getInventory(), outputSlot);
		}
		recipe = null;
		this.setChanged();
	}

	protected IInventoryTileRecipe<T> lookupRecipe() {
		return lookupRecipe(this.getLevel(), recipeType);
	}

	@Override
	public void clearContent() {
		super.clearContent();
		recipe = null;
	}
}
