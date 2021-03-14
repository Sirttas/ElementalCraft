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
import sirttas.elementalcraft.block.retriever.BlockRetriever;
import sirttas.elementalcraft.block.tile.AbstractTileECCrafting;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;
import sirttas.elementalcraft.rune.handler.CapabilityRuneHandler;
import sirttas.elementalcraft.rune.handler.RuneHandler;

public abstract class AbstractTileInstrument<T extends IInstrument, R extends IInstrumentRecipe<T>> extends AbstractTileECCrafting<T, R> implements IInstrument {

	private int progress = 0;
	private final RuneHandler runeHandler;

	protected AbstractTileInstrument(TileEntityType<?> tileEntityTypeIn, IRecipeType<R> recipeType, int transferSpeed, int maxRunes) {
		super(tileEntityTypeIn, recipeType, transferSpeed);
		runeHandler = maxRunes > 0 ? new RuneHandler(maxRunes) : null;
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.isPowered() && progressOnTick()) {
			makeProgress();
		}
		BlockRetriever.sendOutputToRetriever(world, pos, getInventory(), outputSlot);
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
		if (world.isRemote) {
			ParticleHelper.createElementFlowParticle(getElementType(), world, Vector3d.copyCentered(pos), Direction.UP, 1, world.rand);
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
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt(ECNames.PROGRESS, progress);
		compound.put(ECNames.RUNE_HANDLER, CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY.writeNBT(runeHandler, null));
		return compound;
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		progress = compound.getInt(ECNames.PROGRESS);
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY.readNBT(runeHandler, null, compound.get(ECNames.RUNE_HANDLER));
		}
	}

	@Override
	public void clear() {
		super.clear();
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
		if (!this.removed && cap == CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY) {
			return LazyOptional.of(runeHandler != null ? () -> runeHandler : null).cast();
		}
		return super.getCapability(cap, side);
	}
}
