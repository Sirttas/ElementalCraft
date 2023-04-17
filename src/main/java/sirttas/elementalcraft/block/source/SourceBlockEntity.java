package sirttas.elementalcraft.block.source;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.holder.ISourceTraitHolder;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SourceBlockEntity extends AbstractECBlockEntity implements IElementTypeProvider {

	private boolean analyzed = false;
	private boolean stabilized = false;
	private final SourceElementStorage elementStorage;
	private final SourceSourceTraitHolder traitHolder;

	public SourceBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.SOURCE, pos, state);
		elementStorage = new SourceElementStorage(this);
		elementStorage.setElementType(ElementType.getElementType(state));
		traitHolder = new SourceSourceTraitHolder(this);
	}

	@NotNull
	public ISingleElementStorage getElementStorage() {
		return elementStorage;
	}

	@NotNull
	public ISourceTraitHolder getTraitHolder() {
		return this.traitHolder;
	}

    public static void serverTick(Level level, BlockPos pos, BlockState state, SourceBlockEntity source) {
		if (source.traitHolder.isEmpty()) {
			source.initTraits(level, 0);
		}
        if (source.elementStorage.isExhausted()) {
			if (source.traitHolder.isArtificial()) {
				level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
			} else {
				source.elementStorage.insertElement(source.traitHolder.getRecoverRate(), false);
			}
        }
    }

    private void initTraits(@Nonnull Level level, int luck) {
        if (elementStorage.getElementType() == ElementType.NONE) {
            elementStorage.setElementType(ElementType.getElementType(this.getBlockState()));
			this.setChanged();
        }
		traitHolder.initTraits(level, this.worldPosition, luck);
		this.refreshCapacity();
    }

	public void resetTraits(@Nonnull ServerLevelAccessor level, int luck) {
		traitHolder.clear();
		this.initTraits(level.getLevel(), luck);
	}

	public boolean isExhausted() {
		return elementStorage.isExhausted();
	}

	public void exhaust() {
		if (!traitHolder.isArtificial()) {
			elementStorage.setElementAmount(0);
		}
	}

	@Override
	public ElementType getElementType() {
		return this.elementStorage.getElementType();
	}

	public float getRemainingRatio() {
		var capacity = this.elementStorage.getElementCapacity();

		if (capacity == 0) {
			return 0;
		}
		return this.elementStorage.getElementAmount() / (float) capacity;
	}

	public boolean isAnalyzed() {
		return analyzed;
	}

	public void setAnalyzed(boolean analyzed) {
		this.analyzed = analyzed;
		this.setChanged();
	}

	public boolean isStabilized() {
		return stabilized;
	}

	public void setStabilized(boolean stabilized) {
		this.stabilized = stabilized;
		this.setChanged();
	}

	private void refreshCapacity() {
		var oldCapacity = elementStorage.getElementCapacity();
		var capacity = traitHolder.getCapacity();
		var amount = elementStorage.getElementAmount();

		elementStorage.setElementCapacity(capacity);
		if (amount <= 0 || amount >= oldCapacity || amount >= capacity) {
			elementStorage.setElementAmount(capacity);
		}
		this.setChanged();
	}

	@Override
	public void load(@Nonnull CompoundTag compound) {
		super.load(compound);
		if (compound.contains(ECNames.ELEMENT_STORAGE)) {
			elementStorage.deserializeNBT(compound.getCompound(ECNames.ELEMENT_STORAGE));
		}
		analyzed = compound.getBoolean(ECNames.ANALYZED);
		if (compound.contains(ECNames.TRAITS_HOLDER)) {
			traitHolder.deserializeNBT(compound.getCompound(ECNames.TRAITS_HOLDER));
		}
		stabilized = compound.getBoolean(ECNames.STABILIZED);
		elementStorage.setExhausted(compound.getBoolean(ECNames.EXHAUSTED));
		refreshCapacity();
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);
		compound.put(ECNames.ELEMENT_STORAGE, elementStorage.serializeNBT());
		compound.putBoolean(ECNames.EXHAUSTED, elementStorage.isExhausted());
		compound.put(ECNames.TRAITS_HOLDER, traitHolder.serializeNBT());
		compound.putBoolean(ECNames.ANALYZED, analyzed);
		compound.putBoolean(ECNames.STABILIZED, stabilized);
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
		if (!this.remove) {
			if (cap == ElementalCraftCapabilities.ELEMENT_STORAGE) {
				return LazyOptional.of(this::getElementStorage).cast();
			} else if (cap == ElementalCraftCapabilities.SOURCE_TRAIT_HOLDER) {
				return LazyOptional.of(this::getTraitHolder).cast();
			}
		}
		return super.getCapability(cap, side);
	}
}
