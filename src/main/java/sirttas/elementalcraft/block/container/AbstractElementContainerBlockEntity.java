package sirttas.elementalcraft.block.container;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.container.IElementStorageBlocKEntity;

import javax.annotation.Nonnull;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractElementContainerBlockEntity extends AbstractECBlockEntity implements ElementContainer, IElementStorageBlocKEntity {

	protected final SingleElementStorage elementStorage;

	protected AbstractElementContainerBlockEntity(Supplier<? extends BlockEntityType<?>> blockEntityType, Function<AbstractElementContainerBlockEntity, SingleElementStorage> elementStorage, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
		this.elementStorage = elementStorage.apply(this);
	}

	@Override
	public void loadAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.loadAdditional(compound, provider);
		if (compound.contains(ECNames.ELEMENT_STORAGE)) {
			elementStorage.deserializeNBT(provider, compound.getCompound(ECNames.ELEMENT_STORAGE));
		}
	}

    @Override
	public void saveAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.saveAdditional(compound, provider);
		compound.put(ECNames.ELEMENT_STORAGE, elementStorage.serializeNBT(provider));
	}

	@Override
	public ISingleElementStorage getElementStorage() {
		return elementStorage;
	}

	protected abstract void setElementType(ElementType type);

	@Override
	protected void applyImplicitComponents(@NotNull DataComponentInput input) {
		super.applyImplicitComponents(input);
		setElementType(input.getOrDefault(ECDataComponents.ELEMENT_TYPE, ElementType.NONE));
		elementStorage.setElementAmount(input.getOrDefault(ECDataComponents.ELEMENT_AMOUNT, 0));
	}

	@Override
	protected void collectImplicitComponents(@NotNull DataComponentMap.Builder builder) {
		super.collectImplicitComponents(builder);
		builder.set(ECDataComponents.ELEMENT_TYPE, elementStorage.getElementType());
		builder.set(ECDataComponents.ELEMENT_AMOUNT, elementStorage.getElementAmount());
	}

	@Override
	@Deprecated
	public void removeComponentsFromTag(@NotNull CompoundTag tag) {
		super.removeComponentsFromTag(tag);
		tag.remove(ECNames.ELEMENT_STORAGE);
	}

	@Nonnull
	@Override
	public HolderSet<Block> getCompatibleTools() {
		return getProperties().compatibleTools();
	}

	@Nonnull
	public ElementContainerProperties getProperties() {
		return ((AbstractElementContainerBlock) getBlockState().getBlock()).getProperties();
	}
}
