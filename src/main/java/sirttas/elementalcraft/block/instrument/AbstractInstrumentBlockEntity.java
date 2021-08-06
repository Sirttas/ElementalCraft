package sirttas.elementalcraft.block.instrument;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.CapabilityRuneHandler;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.entity.AbstractECCraftingBlockEntity;
import sirttas.elementalcraft.block.retriever.RetrieverBlock;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public abstract class AbstractInstrumentBlockEntity<T extends IInstrument, R extends IInstrumentRecipe<T>> extends AbstractECCraftingBlockEntity<T, R> implements IInstrument {

	private int progress = 0;
	private final RuneHandler runeHandler;
	protected boolean lockable = false;
	private boolean locked = false;

	protected AbstractInstrumentBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, RecipeType<R> recipeType, int transferSpeed, int maxRunes) {
		super(blockEntityType, pos, state, recipeType, transferSpeed);
		runeHandler = maxRunes > 0 ? new RuneHandler(maxRunes) : null;
	}

	@Override
	public void process() {
		super.process();
		updateLock();
	}

	public static <T extends IInstrument, R extends IInstrumentRecipe<T>> void tick(Level level, BlockPos pos, BlockState state, AbstractInstrumentBlockEntity<T, R> instrument) {
		if (!instrument.isPowered() && instrument.progressOnTick()) {
			instrument.makeProgress();
		}
		if (instrument.shouldRetriverExtractOutput()) {
			RetrieverBlock.sendOutputToRetriever(level, instrument.worldPosition, instrument.getInventory(), instrument.outputSlot);
		}
		if (instrument.locked) {
			instrument.updateLock();
		}
	}
	
	protected boolean shouldRetriverExtractOutput() {
		return !lockable || locked;
	}
	
	private void updateLock() {
		if (lockable) {
			locked = !getInventory().getItem(outputSlot).isEmpty();
		}
	}

	public boolean isLocked() {
		return lockable && locked;
	}
	
	protected boolean progressOnTick() {
		return true;
	}

	protected boolean makeProgress() {
		ISingleElementStorage container = getContainer();

		if (recipe != null && progress >= recipe.getElementAmount()) {
			process();
			progress = 0;
			return true;
		} else if (this.isRecipeAvailable() && container != null) {
			float preservation = runeHandler.getElementPreservation();
			int oldProgress = progress;

			progress += container.extractElement(Math.min(Math.round(runeHandler.getTransferSpeed(this.transferSpeed) / preservation), container.getElementAmount() - 1), getRecipeElementType(), false) * preservation;
			if (progress > 0 &&  progress / this.transferSpeed >= oldProgress / this.transferSpeed) {
				onProgress();
			}
			return true;
		} else if (recipe == null) {
			progress = 0;
		}
		return false;
	}

	private ElementType getRecipeElementType() {
		if (recipe instanceof IElementTypeProvider) {
			return ((IElementTypeProvider) recipe).getElementType();
		}
		return ElementType.NONE;
	}

	protected void onProgress() {
		if (level.isClientSide) {
			ParticleHelper.createElementFlowParticle(getElementType(), level, Vec3.atCenterOf(worldPosition), Direction.UP, 1, level.random);
		}
	}

	@Override
	public ElementType getElementType() {
		ElementType tankType = this.getContainerElementType();
		
		return tankType != ElementType.NONE || recipe == null ? tankType : getRecipeElementType();
	}

	@Override
	public boolean isRunning() {
		return progress > 0;
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
		compound.putInt(ECNames.PROGRESS, progress);
		compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
		return compound;
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		progress = compound.getInt(ECNames.PROGRESS);
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, 8));
		}
	}

	@Override
	public void clearContent() {
		super.clearContent();
		progress = 0;
	}

	@Override
	public int getProgress() {
		return progress;
	}

	public float getProgressRatio() {
		return (float) progress / recipe.getElementAmount();
	}

	public RuneHandler getRuneHandler() {
		return runeHandler;
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(Capability<U> cap, @Nullable Direction side) {
		if (!this.remove && cap == CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY) {
			return LazyOptional.of(runeHandler != null ? () -> runeHandler : null).cast();
		}
		return super.getCapability(cap, side);
	}
}
