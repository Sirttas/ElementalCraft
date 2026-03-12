package sirttas.elementalcraft.block.entity.crafting;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.entity.AbstractECContainerBlockEntity;
import sirttas.elementalcraft.block.entity.ICraftingBlockEntity;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.retriever.RetrieverBlock;
import sirttas.elementalcraft.container.IRuneableBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public abstract class AbstractECCraftingBlockEntity<I extends RecipeInput, R extends Recipe<I>> extends AbstractECContainerBlockEntity implements ICraftingBlockEntity, IRuneableBlockEntity {

	protected final Holder<IConfigurableBlockEntityProperties> properties;
	protected final RuneHandler runeHandler;
	protected R recipe;
	protected boolean locked = false;
	
	protected AbstractECCraftingBlockEntity(Supplier<? extends BlockEntityType<?>> blockEntityType, Holder<IConfigurableBlockEntityProperties> properties, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
		this.properties = properties;
		this.runeHandler = new RuneHandler(getProperties().maxRunes(), this::setChanged);
	}

	public static <I extends RecipeInput, R extends Recipe<I>> void tick(AbstractECCraftingBlockEntity<I, R> blockEntity) {
		if (blockEntity.shouldRetrieverExtractOutput()) {
			blockEntity.retrieve();
		}
		if (blockEntity.locked) {
			blockEntity.updateLock();
		}
	}

	@Nonnull
	protected abstract I createRecipeInput();

	@Override
	public boolean isRecipeAvailable() {
		if (recipe != null && recipe.matches(createRecipeInput(), level)) {
			return true;
		}
		if (!this.getInventory().isEmpty()) {
			recipe = this.lookupRecipe();
			if (recipe != null) {
				this.setChanged();
				return true;
			}
		}
		return false;
	}

	@Override
	public void process() {
		if (!level.isClientSide()) {
			assemble();
		}
		recipe = null;
		updateLock();
		this.setChanged();
	}

	protected void retrieve() {
		if (getProperties().retrieveAll()) {
			for (int i = 0; i < getInventory().getContainerSize(); i++) {
				RetrieverBlock.sendOutputToRetriever(level, worldPosition, getInventory(), i);
			}
		} else {
			RetrieverBlock.sendOutputToRetriever(level, worldPosition, getInventory(), getOutputSlot());
		}
	}

	protected int getProgressRounded(float transferAmount, float progress) {
		return Math.round(progress / (transferAmount * 3));
	}

	protected abstract void assemble();

	@Nullable
    protected final R lookupRecipe() {
		return lookupRecipe(createRecipeInput());
	}

	@Nullable
	protected R lookupRecipe(@Nonnull I recipeInput) {
		return level != null ? this.lookupRecipe(level, getProperties().getRecipeType(), recipeInput) : null;
	}

	protected boolean shouldRetrieverExtractOutput() {
		return (!isLockable() || locked) && !getInventory().getItem(getOutputSlot()).isEmpty();
	}

	public boolean isLockable() {
		return getProperties().lockable();
	}

	public int getOutputSlot() {
		return getProperties().outputSlot();
	}

	protected void updateLock() {
		if (isLockable()) {
			locked = !getInventory().getItem(getOutputSlot()).isEmpty();
		}
	}

	public boolean isLocked() {
		return isLockable() && locked;
	}

	@Override
	@Nonnull
	public RuneHandler getRuneHandler() {
		return runeHandler;
	}

	@Nonnull
	public CraftingBlockEntityProperties getProperties() {
		if (properties.isBound() && this.properties.value() instanceof CraftingBlockEntityProperties craftingBlockEntityProperties) {
			return craftingBlockEntityProperties;
		}
		return CraftingBlockEntityProperties.DEFAULT;
	}

	@Override
	protected void saveAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.saveAdditional(compound, provider);
		compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
		if (isLockable()) {
			compound.putBoolean(ECNames.LOCKED, locked);
		}
	}

    @Override
	protected void loadAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.loadAdditional(compound, provider);
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, Tag.OBJECT_HEADER));
		}
		if (isLockable() && compound.contains(ECNames.LOCKED)) {
			locked = compound.getBoolean(ECNames.LOCKED);
		}
	}

	public float getTransferSpeed() {
		return getProperties().transferSpeed();
	}
}
