package sirttas.elementalcraft.block.source;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.holder.ISourceTraitHolder;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.source.trait.holder.ItemSourceTraitHolder;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.container.IElementStorageBlocKEntity;

import javax.annotation.Nonnull;

public class SourceBlockEntity extends AbstractECBlockEntity implements IElementTypeProvider, IElementStorageBlocKEntity {

	private boolean stabilized = false;
	private final SourceElementStorage elementStorage;
	private final SourceSourceTraitHolder traitHolder;

	public SourceBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.SOURCE, pos, state);
		elementStorage = new SourceElementStorage(this);
		elementStorage.setElementType(ElementType.getElementType(state));
		traitHolder = new SourceSourceTraitHolder(this);
	}

	@Override
	@NotNull
	public ISingleElementStorage getElementStorage() {
		return elementStorage;
	}

	@NotNull
	public ISourceTraitHolder getTraitHolder() {
		return this.traitHolder;
	}

	@Override
	public void setLevel(@NotNull Level level) {
		super.setLevel(level);

		if (!(level instanceof ServerLevel serverLevel)) {
			return;
		}
		if (traitHolder.isEmpty()) {
			initTraits(serverLevel, 0);
		}
	}

    private void initTraits(@Nonnull ServerLevelAccessor level, int luck) {
        if (elementStorage.getElementType() == ElementType.NONE) {
            elementStorage.setElementType(ElementType.getElementType(this.getBlockState()));
			this.setChanged();
        }
		traitHolder.initTraits(level, this.worldPosition, luck);
		this.refreshCapacity();
    }

	public void resetTraits(@Nonnull ServerLevelAccessor level, int luck) {
		traitHolder.clear();
		this.initTraits(level, luck);
	}

	@Override
	public @NotNull ElementType getElementType() {
		return this.elementStorage.getElementType();
	}

	public float getRemainingRatio() {
		var capacity = this.elementStorage.getElementCapacity();

		if (capacity == 0) {
			return 0;
		}
		return this.elementStorage.getElementAmount() / (float) capacity;
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
	public void loadAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.loadAdditional(compound, provider);
		if (compound.contains(ECNames.ELEMENT_STORAGE)) {
			elementStorage.deserializeNBT(provider, compound.getCompound(ECNames.ELEMENT_STORAGE));
		}
		if (compound.contains(ECNames.SOURCE_TRAITS_HOLDER)) {
			traitHolder.deserializeNBT(provider, compound.getCompound(ECNames.SOURCE_TRAITS_HOLDER));
		}
		stabilized = compound.getBoolean(ECNames.STABILIZED);
		refreshCapacity();
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.saveAdditional(compound, provider);
		compound.put(ECNames.ELEMENT_STORAGE, elementStorage.serializeNBT(provider));
		compound.put(ECNames.SOURCE_TRAITS_HOLDER, traitHolder.serializeNBT(provider));
		compound.putBoolean(ECNames.STABILIZED, stabilized);
	}

	@Override
	protected void applyImplicitComponents(@NotNull DataComponentInput input) {
		super.applyImplicitComponents(input);
		elementStorage.setElementAmount(input.getOrDefault(ECDataComponents.ELEMENT_AMOUNT, 0));
		traitHolder.setTraits(input.getOrDefault(ECDataComponents.SOURCE_TRAITS_HOLDER, ItemSourceTraitHolder.EMPTY).getTraits());
	}

	@Override
	protected void collectImplicitComponents(@NotNull DataComponentMap.Builder builder) {
		super.collectImplicitComponents(builder);
		builder.set(ECDataComponents.ELEMENT_AMOUNT, elementStorage.getElementAmount());
		builder.set(ECDataComponents.SOURCE_TRAITS_HOLDER, ItemSourceTraitHolder.from(traitHolder));
	}

	@Override
	@Deprecated
	public void removeComponentsFromTag(@NotNull CompoundTag tag) {
		super.removeComponentsFromTag(tag);
		tag.remove(ECNames.ELEMENT_STORAGE);
		tag.remove(ECNames.SOURCE_TRAITS_HOLDER);
	}
}
