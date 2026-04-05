package sirttas.elementalcraft.block.entity.crafting;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.entity.AbstractECContainerBlockEntity;
import sirttas.elementalcraft.block.entity.ICraftingBlockEntity;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.retriever.RetrieverBlock;
import sirttas.elementalcraft.container.IRuneableBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public abstract class AbstractECCraftingBlockEntity<I extends RecipeInput, R extends Recipe<@NotNull I>> extends AbstractECContainerBlockEntity implements ICraftingBlockEntity, IRuneableBlockEntity {

	protected final Holder<@NotNull IConfigurableBlockEntityProperties> properties;
	protected final RuneHandler runeHandler;
	protected R recipe;
	protected boolean locked = false;
	
	protected AbstractECCraftingBlockEntity(Supplier<? extends BlockEntityType<?>> blockEntityType, Holder<@NotNull IConfigurableBlockEntityProperties> properties, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
		this.properties = properties;
		this.runeHandler = new RuneHandler(getProperties().maxRunes(), this::setChanged);
	}

	public static <I extends RecipeInput, R extends Recipe<@NotNull I>> void tick(AbstractECCraftingBlockEntity<I, R> blockEntity) {
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
		return level instanceof ServerLevel serverLevel ? lookupRecipe(serverLevel, createRecipeInput()) : null;
	}

	@Nullable
	protected R lookupRecipe(@Nonnull ServerLevel level, @Nonnull I recipeInput) {
		return this.lookupRecipe(level, getProperties().getRecipeType(), recipeInput);
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
    protected void loadAdditional(@Nonnull ValueInput input) {
        super.loadAdditional(input);
        input.readChild(ECNames.RUNE_HANDLER, runeHandler);
        if (isLockable()) {
            locked = input.getBooleanOr(ECNames.LOCKED, false);
        }
    }
	@Override
	protected void saveAdditional(@Nonnull ValueOutput output) {
		super.saveAdditional(output);
        output.putChild(ECNames.RUNE_HANDLER, runeHandler);
		if (isLockable()) {
            output.putBoolean(ECNames.LOCKED, locked);
		}
	}

	public float getTransferSpeed() {
		return getProperties().transferSpeed();
	}
}
