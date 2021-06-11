package sirttas.elementalcraft.block.instrument;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
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

	protected AbstractInstrumentBlockEntity(TileEntityType<?> tileEntityTypeIn, IRecipeType<R> recipeType, int transferSpeed, int maxRunes) {
		super(tileEntityTypeIn, recipeType, transferSpeed);
		runeHandler = maxRunes > 0 ? new RuneHandler(maxRunes) : null;
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.isPowered() && progressOnTick()) {
			makeProgress();
		}
		if (this.shouldRetriverExtractOutput()) {
			RetrieverBlock.sendOutputToRetriever(level, worldPosition, getInventory(), outputSlot);
		}
	}

	protected boolean shouldRetriverExtractOutput() {
		return true;
	}
	
	protected boolean progressOnTick() {
		return true;
	}

	protected boolean makeProgress() {
		ISingleElementStorage tank = getTank();

		if (recipe != null && progress >= recipe.getElementAmount()) {
			process();
			progress = 0;
			return true;
		} else if (this.isRecipeAvailable() && tank != null) {
			float preservation = runeHandler.getElementPreservation();
			int oldProgress = progress;

			progress += tank.extractElement(Math.min(Math.round(runeHandler.getTransferSpeed(this.transferSpeed) / preservation), tank.getElementAmount() - 1), getRecipeElementType(), false) * preservation;
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
			ParticleHelper.createElementFlowParticle(getElementType(), level, Vector3d.atCenterOf(worldPosition), Direction.UP, 1, level.random);
		}
	}

	@Override
	public ElementType getElementType() {
		ElementType tankType = this.getTankElementType();
		
		return tankType != ElementType.NONE || recipe == null ? tankType : getRecipeElementType();
	}

	@Override
	public boolean isRunning() {
		return progress > 0;
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.putInt(ECNames.PROGRESS, progress);
		compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
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
