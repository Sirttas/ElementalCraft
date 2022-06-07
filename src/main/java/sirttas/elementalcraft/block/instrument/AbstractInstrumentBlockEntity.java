package sirttas.elementalcraft.block.instrument;

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
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.CapabilityRuneHandler;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.entity.AbstractECCraftingBlockEntity;
import sirttas.elementalcraft.block.retriever.RetrieverBlock;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractInstrumentBlockEntity<T extends IInstrument, R extends IInstrumentRecipe<T>> extends AbstractECCraftingBlockEntity<T, R> implements IInstrument {

	private int progress = 0;
	private final RuneHandler runeHandler;
	protected boolean lockable = false;
	private boolean locked = false;
	protected Vec3 particleOffset;

	protected AbstractInstrumentBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, RecipeType<R> recipeType, int transferSpeed, int maxRunes) {
		super(blockEntityType, pos, state, recipeType, transferSpeed);
		runeHandler = maxRunes > 0 ? new RuneHandler(maxRunes, this::setChanged) : null;
		particleOffset = Vec3.ZERO;
	}

	@Override
	public void process() {
		super.process();
		updateLock();
		if (this.level.isClientSide) {
			ParticleHelper.createCraftingParticle(getElementType(), level, Vec3.atCenterOf(worldPosition).add(particleOffset), level.random);
		}
	}

	public static <T extends IInstrument, R extends IInstrumentRecipe<T>> void tick(Level level, BlockPos pos, BlockState state, AbstractInstrumentBlockEntity<T, R> instrument) {
		if (!instrument.isPowered() && instrument.progressOnTick()) {
			instrument.makeProgress();
		}
		if (instrument.shouldRetrieverExtractOutput()) {
			RetrieverBlock.sendOutputToRetriever(level, instrument.worldPosition, instrument.getInventory(), instrument.outputSlot);
		}
		if (instrument.locked) {
			instrument.updateLock();
		}
	}
	
	protected boolean shouldRetrieverExtractOutput() {
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
			if (level.isClientSide && progress > 0 &&  getProgressRounded(this.transferSpeed, progress) > getProgressRounded(this.transferSpeed, oldProgress)) {
				ParticleHelper.createElementFlowParticle(getElementType(), level, Vec3.atCenterOf(worldPosition).add(particleOffset), Direction.UP, 1, level.random);
				renderProgressParticles();
			}
			return true;
		} else if (recipe == null) {
			progress = 0;
		}
		return false;
	}

	protected void renderProgressParticles() {}
	
	private ElementType getRecipeElementType() {
		if (recipe != null) {
			return recipe.getElementType();
		}
		return ElementType.NONE;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	protected void assemble() {
		getInventory().setItem(0, recipe.assemble((T) this));
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
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putInt(ECNames.PROGRESS, progress);
		compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
	}

	@Override
	public void load(@Nonnull CompoundTag compound) {
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
	public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
		if (!this.remove && cap == CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY) {
			return LazyOptional.of(runeHandler != null ? () -> runeHandler : null).cast();
		}
		return super.getCapability(cap, side);
	}
}
