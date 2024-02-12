package sirttas.elementalcraft.block.instrument;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.AbstractECCraftingBlockEntity;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

import javax.annotation.Nonnull;

public abstract class AbstractInstrumentBlockEntity<T extends IInstrument, R extends IInstrumentRecipe<T>> extends AbstractECCraftingBlockEntity<T, R> implements IInstrument {

	private int progress = 0;
	private ISingleElementStorage containerCache;
	protected Vec3 particleOffset;

	protected AbstractInstrumentBlockEntity(Config<T, R> config, BlockPos pos, BlockState state) {
		super(config, pos, state);
		particleOffset = Vec3.ZERO;
	}

	@Override
	public void process() {
		super.process();
		if (this.level.isClientSide) {
			ParticleHelper.createCraftingParticle(getElementType(), level, Vec3.atCenterOf(worldPosition).add(particleOffset), level.random);
		}
	}

	public static <T extends IInstrument, R extends IInstrumentRecipe<T>> void tick(Level level, BlockPos pos, BlockState state, AbstractInstrumentBlockEntity<T, R> instrument) {
		if (!instrument.isPowered() && instrument.progressOnTick()) {
			instrument.makeProgress();
		}
		AbstractECCraftingBlockEntity.tick(instrument);
	}
	
	protected boolean progressOnTick() {
		return true;
	}

	protected boolean makeProgress() {
		var container = getContainer();

		if (recipe != null && progress >= getElementAmount()) {
			process();
			progress = 0;
			return true;
		} else if (this.isRecipeAvailable() && container != null) {
			float preservation = runeHandler.getElementPreservation();
			int oldProgress = progress;
			var transfer = ceilTransfer(container, Math.round(runeHandler.getTransferSpeed(this.transferSpeed) / preservation));

			progress += Math.round(container.extractElement(transfer, getRecipeElementType(), false) * preservation);
			if (level.isClientSide && progress > 0 && getProgressRounded(this.transferSpeed, progress) > getProgressRounded(this.transferSpeed, oldProgress)) {
				ParticleHelper.createElementFlowParticle(getElementType(), level, Vec3.atCenterOf(worldPosition).add(particleOffset), Direction.UP, 1, level.random);
				renderProgressParticles();
			}
			return true;
		} else if (recipe == null) {
			progress = 0;
		}
		return false;
	}

	private int ceilTransfer(ISingleElementStorage container, int transfer) {
		var max = container.getElementAmount();

		if (transfer >= max) {
			if (progress + max < getElementAmount()) {
				transfer = max - 1; // -1 to avoid draining the container
			} else {
				transfer = max; // we have enough element to finish the recipe, so we don't care if we drain the container
			}
		}
		return transfer;
	}

	@SuppressWarnings("unchecked")
	private int getElementAmount() {
		return recipe == null ? 0 : recipe.getElementAmount((T) this);
	}

	protected void renderProgressParticles() {}
	
	protected ElementType getRecipeElementType() {
		if (recipe instanceof IElementTypeProvider provider) {
			return provider.getElementType();
		}
		return ElementType.NONE;
	}

	@Override
	protected void assemble() {
		var inv = getInventory();
		var remainingItems = recipe.getRemainingItems(getContainerWrapper());

		inv.setItem(outputSlot, recipe.assemble(getContainerWrapper(), level.registryAccess()));
		for (int i = 0; i < inv.getContainerSize(); i++) {
			if (i != outputSlot) {
				inv.setItem(i, remainingItems.get(i));
			}
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
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putInt(ECNames.PROGRESS, progress);
	}

	@Override
	public void load(@Nonnull CompoundTag compound) {
		super.load(compound);
		progress = compound.getInt(ECNames.PROGRESS);
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
		return (float) progress / getElementAmount();
	}

	@Override
	public ISingleElementStorage getContainer() {
		if (containerCache == null) {
			containerCache = IInstrument.super.getContainer();
		}
		return containerCache;
	}

	@Override
	protected R lookupRecipe() {
		if (getContainerElementType() == ElementType.NONE) {
			return null;
		}
		return super.lookupRecipe();
	}
}
