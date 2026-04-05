package sirttas.elementalcraft.block.source;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
        }
		traitHolder.initTraits(level, this.worldPosition, luck);
		this.initStorageFromTraits();
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

	public boolean isAnalyzed() {
		return analyzed;
	}

	public void setAnalyzed() {
		if (!analyzed) {
			analyzed = true;
			this.setChanged();
		}
	}

	public void setStabilized(boolean stabilized) {
		this.stabilized = stabilized;
		this.setChanged();
	}

	private void initStorageFromTraits() {
		var capacity = traitHolder.getCapacity();

		elementStorage.setElementCapacity(capacity);
		elementStorage.setElementAmount(capacity);
		this.setChanged();
	}

	@Override
	public void loadAdditional(@Nonnull ValueInput input) {
		super.loadAdditional(input);
        input.readChild(ECNames.SOURCE_TRAITS_HOLDER, traitHolder);
        if (input.child(ECNames.ELEMENT_STORAGE).isPresent()) {
            input.readChild(ECNames.ELEMENT_STORAGE, elementStorage);
        } else {
            initStorageFromTraits();
        }
        analyzed = input.getBooleanOr(ECNames.ANALYZED, false);
        stabilized = input.getBooleanOr(ECNames.STABILIZED, false);
	}

	@Override
	public void saveAdditional(@Nonnull ValueOutput output) {
		super.saveAdditional(output);
        output.putChild(ECNames.SOURCE_TRAITS_HOLDER, traitHolder);
        output.putChild(ECNames.ELEMENT_STORAGE, elementStorage);
        output.putBoolean(ECNames.ANALYZED, analyzed);
        output.putBoolean(ECNames.STABILIZED, stabilized);
	}

	@Override
	protected void applyImplicitComponents(@NotNull DataComponentGetter getter) {
		super.applyImplicitComponents(getter);

		var traits = getter.getOrDefault(ECDataComponents.SOURCE_TRAITS_HOLDER, ItemSourceTraitHolder.EMPTY).getTraits();

		if (!traits.isEmpty()) {
			traitHolder.setTraits(traits);
			initStorageFromTraits();
			elementStorage.setElementAmount(getter.getOrDefault(ECDataComponents.ELEMENT_AMOUNT, elementStorage.getElementCapacity()));
		} else {
			initStorageFromTraits();
		}
		analyzed = getter.getOrDefault(ECDataComponents.SOURCE_ANALYZED, false);
	}

	@Override
	protected void collectImplicitComponents(@NotNull DataComponentMap.Builder builder) {
		super.collectImplicitComponents(builder);
		builder.set(ECDataComponents.SOURCE_TRAITS_HOLDER, ItemSourceTraitHolder.from(traitHolder));
		builder.set(ECDataComponents.ELEMENT_AMOUNT, elementStorage.getElementAmount());
		builder.set(ECDataComponents.SOURCE_ANALYZED, analyzed);
	}

	@Override
	@Deprecated
	public void removeComponentsFromTag(@NotNull ValueOutput output) {
		super.removeComponentsFromTag(output);
        output.discard(ECNames.ELEMENT_STORAGE);
        output.discard(ECNames.SOURCE_TRAITS_HOLDER);
        output.discard(ECNames.ANALYZED);
	}
}
