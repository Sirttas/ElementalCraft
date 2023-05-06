package sirttas.elementalcraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.retriever.RetrieverBlock;
import sirttas.elementalcraft.recipe.IContainerBlockEntityRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public abstract class AbstractECCraftingBlockEntity<T extends ICraftingBlockEntity, R extends IContainerBlockEntityRecipe<T>> extends AbstractECContainerBlockEntity implements ICraftingBlockEntity {

	protected final RecipeType<R> recipeType;
	protected final int transferSpeed;
	protected final RuneHandler runeHandler;

	protected R recipe;
	protected int outputSlot = 0;
	
	protected AbstractECCraftingBlockEntity(Config<T, R> config, BlockPos pos, BlockState state) {
		super(config.blockEntityType(), pos, state);
		this.recipeType = config.recipeType() != null ? config.recipeType().get() : null;
		this.transferSpeed = config.transferSpeed().get();
		this.runeHandler = new RuneHandler(config.maxRunes().get(), this::setChanged);
	}

	@Override
	public boolean isRecipeAvailable() {
		if (recipe != null && recipe.matches(cast())) {
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
		if (!level.isClientSide) {
			assemble();
			RetrieverBlock.sendOutputToRetriever(level, worldPosition, getInventory(), outputSlot);
		}
		recipe = null;
		this.setChanged();
	}

	protected int getProgressRounded(float transferAmount, float progress) {
		return Math.round(progress / (transferAmount * 3));
	}

	protected abstract void assemble();
	
	protected R lookupRecipe() {
		return level != null ? lookupRecipe(level, recipeType) : null;
	}

	@Nonnull
	public RuneHandler getRuneHandler() {
		return runeHandler;
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);
		compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
	}

	@Override
	public void load(@Nonnull CompoundTag compound) {
		super.load(compound);
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, 8));
		}
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
		if (!this.remove && cap == ElementalCraftCapabilities.RUNE_HANDLE) {
			return LazyOptional.of(this::getRuneHandler).cast();
		}
		return super.getCapability(cap, side);
	}

	public record Config<T extends ICraftingBlockEntity, R extends IContainerBlockEntityRecipe<T>>(
			Supplier<? extends BlockEntityType<?>> blockEntityType,
			Supplier<? extends RecipeType<R>> recipeType,
			Supplier<Integer> transferSpeed,
			Supplier<Integer> maxRunes
	) {}

}
